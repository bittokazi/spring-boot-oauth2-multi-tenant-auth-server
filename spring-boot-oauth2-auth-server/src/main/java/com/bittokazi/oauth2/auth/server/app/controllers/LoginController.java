package com.bittokazi.oauth2.auth.server.app.controllers;

import com.bittokazi.oauth2.auth.server.app.services.login.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/")
    public void index(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/login");
    }

    @GetMapping("/login")
    public Object loginPage(Model model, HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        return loginService.loginPage(model, request, response);
    }

    @GetMapping("/otp-login")
    public Object otpLoginPage(Model model, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        return loginService.otpLoginPage(model, request, response);
    }

    @GetMapping(value = "/oauth2/consent")
    public ModelAndView consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {
        return loginService.consentPage(principal, model, clientId, scope, state, userCode);
    }

    @GetMapping("/authorize_user")
    public ResponseEntity<?> authorizeUser(@RequestParam("code") String code, HttpServletResponse httpServletResponse) throws IOException {
        return loginService.authorizeUser(code, httpServletResponse);
    }

    @GetMapping("/oauth2/")
    public ResponseEntity<?> authorizeUserDefault(@RequestParam("code") String code, HttpServletResponse httpServletResponse) throws IOException {
        return loginService.authorizeUser(code, httpServletResponse);
    }

    @GetMapping("/oauth2/login")
    public void loginRedirect(HttpServletResponse httpServletResponse) throws IOException {
        loginService.loginRedirect(httpServletResponse);
    }

    @PostMapping("/oauth2/refresh/token")
    public ResponseEntity<?>  refreshToken(@RequestBody HashMap<String, String> payload, HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) throws IOException {
        return loginService.refreshToken(payload, httpServletRequest, httpServletResponse);
    }

    @GetMapping("/oauth2/logout/redirect")
    public void logout(HttpServletResponse httpServletResponse) throws IOException {
        loginService.logout(httpServletResponse);
    }
}
