package com.bittokazi.oauth2.auth.server.config.security.oauth2;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomHttpStatusReturningLogoutSuccessHandler extends HttpStatusReturningLogoutSuccessHandler {

    private final HttpStatus httpStatusToReturn;

    private OauthClientRepository oauthClientRepository;

    public CustomHttpStatusReturningLogoutSuccessHandler(OauthClientRepository oauthClientRepository) {
        super(HttpStatus.OK);
        this.httpStatusToReturn = HttpStatus.OK;
        this.oauthClientRepository = oauthClientRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
        Optional<OauthClient> optionalOauthClient = oauthClientRepository.findOneByClientId(request.getParameter("id"));
        if(optionalOauthClient.isPresent()) {
            response.sendRedirect(optionalOauthClient.get().getPostLogoutUrl());
        } else {
            response.sendRedirect("/app/login");
        }
        response.getWriter().flush();
    }
}
