package com.bittokazi.oauth2.auth.server.config.interceptors;

import com.bittokazi.oauth2.auth.server.app.repositories.master.TenantRepository;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 *
 * @author Bitto Kazi
 *
 */

@Component
public class TenantIdentifierInterceptorAdapter implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TenantRepository tenantRepository;

    private JwtDecoder jwtDecoder;

    public TenantIdentifierInterceptorAdapter(TenantRepository tenantRepository, JwtDecoder JwtDecoder) {
        this.tenantRepository = tenantRepository;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        return Objects.equals(jwtDecoder.decode(token).getClaims().get("tenant"), TenantContext.getCurrentTenant());
    }
}

