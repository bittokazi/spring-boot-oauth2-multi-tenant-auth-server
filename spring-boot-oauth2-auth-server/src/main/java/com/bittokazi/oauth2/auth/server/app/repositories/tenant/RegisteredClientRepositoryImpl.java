package com.bittokazi.oauth2.auth.server.app.repositories.tenant;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Optional;

public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private OauthClientRepository oauthClientRepository;

    public RegisteredClientRepositoryImpl(OauthClientRepository oauthClientRepository) {
        this.oauthClientRepository = oauthClientRepository;
    }
    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<OauthClient> registeredClientOptional = oauthClientRepository.findOneById(id);
        if(registeredClientOptional.isPresent()) {
            return generateRegisteredClient(registeredClientOptional.get());
        }
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<OauthClient> registeredClientOptional = oauthClientRepository.findOneByClientId(clientId);
        if(registeredClientOptional.isPresent()) {
            return generateRegisteredClient(registeredClientOptional.get());
        }
        return null;
    }

    private RegisteredClient generateRegisteredClient(OauthClient oauthClient) {
        RegisteredClient.Builder oidcClient = RegisteredClient.withId(oauthClient.getId())
                .clientId(oauthClient.getClientId())
                .clientSecret(BCrypt.hashpw(oauthClient.getClientSecret(), BCrypt.gensalt()))
                .clientAuthenticationMethod(new ClientAuthenticationMethod(oauthClient.getClientAuthenticationMethod()))
                .postLogoutRedirectUri(oauthClient.getPostLogoutUrl())
//                .scope(String.join(",", oauthClient.getScope()))
                .clientSettings(ClientSettings.builder()
//                        .requireProofKey(true)
                        .requireAuthorizationConsent(oauthClient.getRequireConsent()).build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(oauthClient.getTokenType().equals("jwt") ? OAuth2TokenFormat.SELF_CONTAINED: OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(oauthClient.getAccessTokenValidity()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(oauthClient.getRefreshTokenValidity())).build());
        oauthClient.getAuthorizedGrantTypes().forEach(s -> {
            oidcClient.authorizationGrantType(new AuthorizationGrantType(s));
        });
        oauthClient.getScope().forEach(s -> {
            oidcClient.scope(s);
        });
        oauthClient.getWebServerRedirectUri().forEach(s -> {
            oidcClient.redirectUri(s);
        });
        return oidcClient.build();
    }
}
