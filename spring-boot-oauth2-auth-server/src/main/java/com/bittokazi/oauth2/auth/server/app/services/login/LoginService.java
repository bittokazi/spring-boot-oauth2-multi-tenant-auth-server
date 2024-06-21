package com.bittokazi.oauth2.auth.server.app.services.login;

import com.bittokazi.oauth2.auth.server.app.models.base.Oauth2Response;
import com.bittokazi.oauth2.auth.server.app.models.master.Tenant;
import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.repositories.master.TenantRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.config.AppConfig;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.utils.CookieActionsProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class LoginService {

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private OAuth2AuthorizationConsentService customJdbcOAuth2AuthorizationConsentService;

    public Object loginPage(Model model, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        checkDeviceId(request, response);
        // if(Objects.nonNull(request.getUserPrincipal()))
        // response.sendRedirect("/oauth2/login");
        final SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (null != savedRequest) {
            // final String redirectUrl = savedRequest.getRedirectUrl();
            // final MultiValueMap parameters =
            // UriComponentsBuilder.fromUriString(redirectUrl).build().getQueryParams();
            // if (!parameters.containsKey("client_id")) {
            // return "Missing login information";
            // }
            String targetUrl = savedRequest.getRedirectUrl();
            if (Objects.nonNull(request.getUserPrincipal())) {
                response.sendRedirect(targetUrl);
                new HttpSessionRequestCache().removeRequest(request, response);
            }
        } else {
            if (Objects.nonNull(request.getUserPrincipal())) {
                Optional<Tenant> tenantOptional = tenantRepository
                        .findOneByCompanyKey(TenantContext.getCurrentTenant());
                if (tenantOptional.isPresent()) {
                    if (!tenantOptional.get().getDefaultRedirectUrl().equals("")) {
                        response.sendRedirect(tenantOptional.get().getDefaultRedirectUrl());
                    } else {
                        response.sendRedirect("/oauth2/login");
                    }
                } else {
                    response.sendRedirect("/oauth2/login");
                }
            }
        }
        Optional<Tenant> tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
        model.addAttribute("tenantName",
                tenantOptional.isPresent() ? tenantOptional.get().getName() : AppConfig.DEFAULT_APP_NAME);
        model.addAttribute("signInBtnColor",
                tenantOptional.isPresent() ? tenantOptional.get().getSignInBtnColor() : "#7367f0 !important");
        model.addAttribute("resetPasswordLink", tenantOptional.isPresent() ? tenantOptional.get().getResetPasswordLink()
                : System.getenv().get("APPLICATION_BACKEND_URL") + "/app/forget-password");
        model.addAttribute("createAccountLink",
                tenantOptional.isPresent() ? tenantOptional.get().getCreateAccountLink() : "");
        if (Objects.nonNull(request.getParameter("otp"))) {
            model.addAttribute("otpError", request.getParameter("otp"));
        }
        ModelAndView modelAndView = new ModelAndView("login", (Map<String, ?>) model);
        return modelAndView;
    }

    public Object otpLoginPage(Model model, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (Objects.isNull(session.getAttribute("otpRequired"))) {
            response.sendRedirect(request.getContextPath() + "/login?otp=error");
            return "";
        }
        Optional<Tenant> tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
        model.addAttribute("tenantName", tenantOptional.isPresent() ? tenantOptional.get().getName() : "AuthKit");
        model.addAttribute("signInBtnColor",
                tenantOptional.isPresent() ? tenantOptional.get().getSignInBtnColor() : "#7367f0 !important");
        setOtpParam(model, session, "otpRequiredUsername");
        setOtpParam(model, session, "otpRequiredPassword");
        setOtpParam(model, session, "otpRequiredRememberMe");
        setOtpParam(model, session, "otpRequiredTrustDevice");
        setOtpParam(model, session, "otpRequired");
        setOtpParam(model, session, "message");
        ModelAndView modelAndView = new ModelAndView("otp-login", (Map<String, ?>) model);
        return modelAndView;
    }

    public ModelAndView consentPage(Principal principal, Model model,
            @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
            @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
            @RequestParam(OAuth2ParameterNames.STATE) String state,
            @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        // Remove scopes that were already approved
        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();
        Optional<OauthClient> registeredClient = this.oauthClientRepository.findOneByClientId(clientId);
        OAuth2AuthorizationConsent currentAuthorizationConsent = this.customJdbcOAuth2AuthorizationConsentService
                .findById(registeredClient.get().getId(), principal.getName());
        Set<String> authorizedScopes;
        if (currentAuthorizationConsent != null) {
            authorizedScopes = currentAuthorizationConsent.getScopes();
        } else {
            authorizedScopes = Collections.emptySet();
        }
        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userCode", userCode);
        if (StringUtils.hasText(userCode)) {
            model.addAttribute("requestURI", "/oauth2/device_verification");
        } else {
            model.addAttribute("requestURI", "/oauth2/authorize");
        }

        ModelAndView modelAndView = new ModelAndView("consent", (Map<String, ?>) model);
        return modelAndView;
    }

    public ResponseEntity<?> authorizeUser(@RequestParam("code") String code, HttpServletResponse httpServletResponse)
            throws IOException {
        Optional<OauthClient> optionalOauthClient = Optional.empty();
        Optional<Tenant> tenantOptional = Optional.empty();
        if (Objects.equals(TenantContext.getCurrentTenant(), "public")) {
            optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
        } else {
            optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
            tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(optionalOauthClient.get().getClientId(), optionalOauthClient.get().getClientSecret());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", optionalOauthClient.get().getWebServerRedirectUri().stream().findFirst().get());
        if (!Objects.equals(TenantContext.getCurrentTenant(), "public") && AppConfig.USE_X_AUTH_TENANT) {
            headers.add("X-AUTH-TENANT", TenantContext.getCurrentTenant());
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            if (Objects.equals(TenantContext.getCurrentTenant(), "public")
                    || (!Objects.equals(TenantContext.getCurrentTenant(), "public") && AppConfig.USE_X_AUTH_TENANT)) {
                ResponseEntity<Oauth2Response> oauth2Response = restTemplate.postForEntity(
                        System.getenv().get("APPLICATION_BACKEND_URL") + "/oauth2/token", request,
                        Oauth2Response.class);
                updateCookies(httpServletResponse, oauth2Response.getBody());
                if (!Objects.equals(TenantContext.getCurrentTenant(), "public") && AppConfig.USE_X_AUTH_TENANT) {
                    httpServletResponse
                            .sendRedirect(AppConfig.HTTP_SCHEMA + tenantOptional.get().getDomain() + "/app/dashboard");
                } else {
                    httpServletResponse.sendRedirect(System.getenv().get("APPLICATION_BACKEND_URL") + "/app/dashboard");
                }
                return ResponseEntity.ok(oauth2Response.getBody());
                // return ResponseEntity.ok().build();
            } else {
                ResponseEntity<Oauth2Response> oauth2Response = restTemplate.postForEntity(
                        System.getenv().get("HTTP_SCHEMA") + tenantOptional.get().getDomain() + "/oauth2/token",
                        request, Oauth2Response.class);
                updateCookies(httpServletResponse, oauth2Response.getBody());
                httpServletResponse
                        .sendRedirect(AppConfig.HTTP_SCHEMA + tenantOptional.get().getDomain() + "/app/dashboard");
                return ResponseEntity.ok(oauth2Response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(401).body("{ message: \"Unauthorized Access\"}");
            } else if (e.getStatusCode().is5xxServerError()) {
                return ResponseEntity.status(500).body("{ message: \"Server Error\"}");
            }
            e.printStackTrace();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(503).body("{ message: \"Identity Service Unavailable\"}");
    }

    public void loginRedirect(HttpServletResponse httpServletResponse) throws IOException {
        if (Objects.equals(TenantContext.getCurrentTenant(), "public")) {
            Optional<OauthClient> optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
            httpServletResponse
                    .sendRedirect(System.getenv().get("APPLICATION_BACKEND_URL") + "/oauth2/authorize?client_id="
                            + optionalOauthClient.get().getClientId() + "&response_type=code&scope="
                            + String.join("+", optionalOauthClient.get().getScope()) + "&redirect_uri="
                            + optionalOauthClient.get().getWebServerRedirectUri().stream().findFirst().get());
        } else {
            Optional<OauthClient> optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
            Optional<Tenant> tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
            httpServletResponse.sendRedirect(System.getenv().get("HTTP_SCHEMA") + tenantOptional.get().getDomain()
                    + "/oauth2/authorize?client_id=" + optionalOauthClient.get().getClientId()
                    + "&response_type=code&scope=" + String.join("+", optionalOauthClient.get().getScope())
                    + "&redirect_uri="
                    + optionalOauthClient.get().getWebServerRedirectUri().stream().findFirst().get());
        }
    }

    public ResponseEntity<?> refreshToken(@RequestBody HashMap<String, String> payload,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws IOException {
        Optional<OauthClient> optionalOauthClient = Optional.empty();
        Optional<Tenant> tenantOptional = Optional.empty();
        if (Objects.equals(TenantContext.getCurrentTenant(), "public")) {
            optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
        } else {
            optionalOauthClient = oauthClientRepository.findOneById("user_login_service");
            tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(optionalOauthClient.get().getClientId(), optionalOauthClient.get().getClientSecret());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", payload.get("refresh_token"));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            if (Objects.equals(TenantContext.getCurrentTenant(), "public")) {
                ResponseEntity<Oauth2Response> oauth2Response = restTemplate.postForEntity(
                        System.getenv().get("APPLICATION_BACKEND_URL") + "/oauth2/token", request,
                        Oauth2Response.class);
                updateCookies(httpServletResponse, oauth2Response.getBody());
                return ResponseEntity.ok(oauth2Response.getBody());
            } else {
                ResponseEntity<Oauth2Response> oauth2Response = restTemplate.postForEntity(
                        System.getenv().get("HTTP_SCHEMA") + tenantOptional.get().getDomain() + "/oauth2/token",
                        request, Oauth2Response.class);
                updateCookies(httpServletResponse, oauth2Response.getBody());
                return ResponseEntity.ok(oauth2Response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(401).body("{ message: \"Unauthorized Access\"}");
            } else if (e.getStatusCode().is5xxServerError()) {
                return ResponseEntity.status(500).body("{ message: \"Server Error\"}");
            }
        } catch (RestClientException e) {
        }
        return ResponseEntity.status(503).body("{ message: \"Identity Service Unavailable\"}");
    }

    public void logout(HttpServletResponse httpServletResponse) throws IOException {
        if (Objects.equals(TenantContext.getCurrentTenant(), "public")) {
            httpServletResponse.sendRedirect(System.getenv().get("APPLICATION_BACKEND_URL") + "/app/login");
        } else {
            Optional<Tenant> tenantOptional = tenantRepository.findOneByCompanyKey(TenantContext.getCurrentTenant());
            httpServletResponse.sendRedirect(
                    System.getenv().get("HTTP_SCHEMA") + tenantOptional.get().getDomain() + "/app/login?");
        }
    }

    public void updateCookies(HttpServletResponse httpServletResponse, Oauth2Response oauth2Response) {
        CookieActionsProvider cookieActionsProvider = new CookieActionsProvider();
        cookieActionsProvider.setUpdateFunction(CookieActionsProvider.updateCookieFunc(httpServletResponse));
        cookieActionsProvider
                .updateCookie(new CookieActionsProvider.CookieValue("access_token", oauth2Response.getAccess_token()));
        cookieActionsProvider.updateCookie(
                new CookieActionsProvider.CookieValue("refresh_token", oauth2Response.getRefresh_token()));
        cookieActionsProvider
                .updateCookie(new CookieActionsProvider.CookieValue("token_type", oauth2Response.getToken_type()));
        cookieActionsProvider.updateCookie(
                new CookieActionsProvider.CookieValue("expires_in", String.valueOf(oauth2Response.getExpires_in())));
    }

    public void checkDeviceId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = httpServletRequest.getCookies();
        Optional<Cookie> deviceId = Optional.empty();
        if (Objects.nonNull(cookies)) {
            deviceId = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("deviceId"))
                    .findFirst();
        }
        if (!deviceId.isPresent()) {
            CookieActionsProvider cookieActionsProvider = new CookieActionsProvider();
            cookieActionsProvider.setUpdateFunction(CookieActionsProvider.updateCookieFunc(httpServletResponse));
            cookieActionsProvider
                    .updateCookie(new CookieActionsProvider.CookieValue("deviceId", UUID.randomUUID().toString()));
        }
    }

    public void setOtpParam(Model model, HttpSession session, String name) {
        if (Objects.nonNull(session.getAttribute(name))) {
            model.addAttribute(name, session.getAttribute(name));
            session.removeAttribute(name);
        }
    }

    private static Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        Set<ScopeWithDescription> scopeWithDescriptions = new HashSet<>();
        for (String scope : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(scope));

        }
        return scopeWithDescriptions;
    }

    public static class ScopeWithDescription {
        private static final String DEFAULT_DESCRIPTION = "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";
        private static final Map<String, String> scopeDescriptions = new HashMap<>();
        static {
            scopeDescriptions.put(
                    OidcScopes.PROFILE,
                    "This application will be able to read your profile information.");
            scopeDescriptions.put(
                    "tenant:write",
                    "Write tenant information");
            scopeDescriptions.put(
                    "tenant:read",
                    "Read tenant information");
            scopeDescriptions.put(
                    "trust",
                    "Trust the client");
        }

        public final String scope;
        public final String description;

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = scopeDescriptions.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }
    }
}
