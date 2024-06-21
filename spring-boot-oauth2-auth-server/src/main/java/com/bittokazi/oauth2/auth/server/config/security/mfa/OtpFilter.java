package com.bittokazi.oauth2.auth.server.config.security.mfa;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import com.bittokazi.oauth2.auth.server.app.services.base.RestResponseGenerator;
import com.bittokazi.oauth2.auth.server.app.services.mfa.TwoFaService;
import com.bittokazi.oauth2.auth.server.utils.CookieActionsProvider;
import com.bittokazi.oauth2.auth.server.utils.HttpReqRespUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

@Component
public class OtpFilter implements Filter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwoFaService twoFaService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        System.out.println("OtpFilter -> Init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (request != null && request.getContentType() != null
                && Objects.equals(httpServletRequest.getServletPath(), "/login")) {

            Optional<User> user = userRepository
                    .findOneByUsernameIgnoreCase(httpServletRequest.getParameter("username"));
            if (user.isPresent()) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                if (bCryptPasswordEncoder.matches(httpServletRequest.getParameter("password"),
                        user.get().getPassword())) {
                    Cookie[] cookies = httpServletRequest.getCookies();
                    Optional<Cookie> deviceId = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("deviceId"))
                            .findFirst();
                    if(deviceId.isPresent()) {
                        if (Objects.nonNull(user.get().getTwoFaEnabled()) &&
                                user.get().getTwoFaEnabled() &&
                                !twoFaService.isTrustedDevice(deviceId.get().getValue(), user.get())) {

                            if (httpServletRequest.getParameterMap().containsKey("otp-code")) {
                                Integer code = null;
                                try {
                                    code = Integer.valueOf(httpServletRequest.getParameter("otp-code").toString());
                                } catch (Exception e) {

                                }
                                if (twoFaService.validate2FA(code, httpServletRequest, httpServletResponse)
                                        || twoFaService.validate2FAScratchCode(httpServletRequest.getParameter("otp-code"),
                                        httpServletRequest)
                                ) {
                                    if (httpServletRequest.getParameterMap().containsKey("trust-device")) {
                                        twoFaService.saveTrustedDevice(
                                                deviceId.get().getValue(), user.get(),
                                                HttpReqRespUtils.getUserAgent(httpServletRequest), HttpReqRespUtils
                                                        .getClientIpAddressIfServletRequestExist(httpServletRequest));
                                    }
                                    chain.doFilter(request, response);
                                    return;
                                } else {
                                    HttpSession session = httpServletRequest.getSession(true);
                                    session.setAttribute("otpRequiredUsername", httpServletRequest.getParameter("username"));
                                    session.setAttribute("otpRequiredPassword", httpServletRequest.getParameter("password"));
                                    session.setAttribute("otpRequiredRememberMe", httpServletRequest.getParameter("remember-me"));
                                    session.setAttribute("otpRequiredTrustDevice", httpServletRequest.getParameter("trust-device"));
                                    session.setAttribute("otpRequired", true);
                                    session.setAttribute("message", "Invalid OTP");
                                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/otp-login");
                                    return;
                                }
                            }
                            HttpSession session = httpServletRequest.getSession(true);
                            session.setAttribute("otpRequiredUsername", httpServletRequest.getParameter("username"));
                            session.setAttribute("otpRequiredPassword", httpServletRequest.getParameter("password"));
                            session.setAttribute("otpRequiredRememberMe", httpServletRequest.getParameter("remember-me"));
                            session.setAttribute("otpRequiredTrustDevice", httpServletRequest.getParameter("trust-device"));
                            session.setAttribute("otpRequired", true);
                            session.setAttribute("message", "Please Enter OTP");
                            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/otp-login");
                            return;
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
