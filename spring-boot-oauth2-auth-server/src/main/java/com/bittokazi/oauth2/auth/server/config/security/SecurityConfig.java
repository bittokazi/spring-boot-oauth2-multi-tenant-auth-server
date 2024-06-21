package com.bittokazi.oauth2.auth.server.config.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.master.TenantRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RegisteredClientRepositoryImpl;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import com.bittokazi.oauth2.auth.server.app.services.CustomUserDetailsService;
import com.bittokazi.oauth2.auth.server.config.*;
import com.bittokazi.oauth2.auth.server.config.interceptors.TenantContextListener;
import com.bittokazi.oauth2.auth.server.config.security.mfa.OtpFilter;
import com.bittokazi.oauth2.auth.server.config.security.oauth2.CustomHttpStatusReturningLogoutSuccessHandler;
import com.bittokazi.oauth2.auth.server.config.security.oauth2.CustomJdbcOAuth2AuthorizationConsentService;
import com.bittokazi.oauth2.auth.server.config.security.oauth2.CustomJdbcOAuth2AuthorizationService;
import com.bittokazi.oauth2.auth.server.database.MultiTenantConnectionProviderImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private DataSource dataSource;

    private OauthClientRepository oauthClientRepository;

    private UserRepository userRepository;

    private MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl;

    private TenantRepository tenantRepository;

    private CustomHttpStatusReturningLogoutSuccessHandler customHttpStatusReturningLogoutSuccessHandler;

    private OtpFilter otpFilter;


    public SecurityConfig(OauthClientRepository oauthClientRepository, DataSource dataSource, UserRepository userRepository,
                          MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl, TenantRepository tenantRepository,
                          CustomHttpStatusReturningLogoutSuccessHandler customHttpStatusReturningLogoutSuccessHandler, OtpFilter otpFilter) {
        this.oauthClientRepository = oauthClientRepository;
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.multiTenantConnectionProviderImpl = multiTenantConnectionProviderImpl;
        this.tenantRepository = tenantRepository;
        this.customHttpStatusReturningLogoutSuccessHandler = customHttpStatusReturningLogoutSuccessHandler;
        this.otpFilter = otpFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage("/oauth2/consent"))
                        .oidc(oidcConfigurer -> oidcConfigurer
                                .providerConfigurationEndpoint(providerConfigurationEndpoint ->
                                        providerConfigurationEndpoint
                                                .providerConfigurationCustomizer(builder -> {
                                                    builder
                                                            .issuer(TenantContext.getCurrentIssuer())
                                                            .authorizationEndpoint(TenantContext.getCurrentIssuer()+"/oauth2/authorize")
                                                            .deviceAuthorizationEndpoint(TenantContext.getCurrentIssuer()+"/oauth2/device_authorization")
                                                            .tokenEndpoint(TenantContext.getCurrentIssuer()+"/oauth2/token")
                                                            .jwkSetUrl(TenantContext.getCurrentIssuer()+"/oauth2/jwks")
                                                            .userInfoEndpoint(TenantContext.getCurrentIssuer()+"/userinfo")
                                                            .endSessionEndpoint(TenantContext.getCurrentIssuer()+"/connect/logout")
                                                            .tokenRevocationEndpoint(TenantContext.getCurrentIssuer()+"/oauth2/revoke")
                                                            .tokenIntrospectionEndpoint(TenantContext.getCurrentIssuer()+"/oauth2/introspect")
                                                            .build();
                                                })
                                ));
        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices)
            throws Exception {
        http.csrf().disable();
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/oauth2/login", "/oauth2/refresh/token",
                                "/authorize_user", "/login", "/assets/**", "/otp-login",
                                "/app/**", "/public/api/tenants/info").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(otpFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                            .loginPage("/login")
                        .successHandler(new LoginSuccessHandler())
                )
                .logout((logout) -> logout.logoutSuccessHandler(customHttpStatusReturningLogoutSuccessHandler))
                .rememberMe((remember) -> remember
                        .rememberMeServices(rememberMeServices)
                )
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository, oauthClientRepository, multiTenantConnectionProviderImpl, registeredClientRepository());
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new RegisteredClientRepositoryImpl(oauthClientRepository);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(AppConfig.KID.isEmpty()? UUID.randomUUID().toString(): AppConfig.KID)
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            if(AppConfig.CERT_PRIVATE_KEY_FILE.isEmpty() || AppConfig.CERT_PUBLIC_KEY_FILE.isEmpty()) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                keyPair = keyPairGenerator.generateKeyPair();
            } else {
                String privateKeyContent = new String(Files.readAllBytes(Paths.get(AppConfig.CERT_PRIVATE_KEY_FILE)));
                String publicKeyContent = new String(Files.readAllBytes(Paths.get(AppConfig.CERT_PUBLIC_KEY_FILE)));

                privateKeyContent = privateKeyContent.replaceAll("\\n", "")
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "");

                publicKeyContent = publicKeyContent.replaceAll("\\n", "")
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "");;

                KeyFactory kf = KeyFactory.getInstance("RSA");

                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
                PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
                RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

                keyPair = new KeyPair(pubKey, privKey);
            }
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(new JwtTimestampValidator(),
                new JwtClaimValidator<>("tenant", tenant -> tenant.equals(TenantContext.getCurrentTenant())));

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new CustomJdbcOAuth2AuthorizationService(new JdbcTemplate(dataSource), this.registeredClientRepository(), multiTenantConnectionProviderImpl);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new CustomJdbcOAuth2AuthorizationConsentService(new JdbcTemplate(dataSource), this.registeredClientRepository(), multiTenantConnectionProviderImpl);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return (context) -> {
            JwsHeader.Builder headers = context.getJwsHeader();
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims((claims) -> {
                    claims.put("iss", TenantContext.getCurrentIssuer());
                    if(claims.get("sub").toString().equals(context.getRegisteredClient().getClientId())) {
                        Optional<OauthClient> optionalOauthClient = oauthClientRepository.findOneByClientId(context.getRegisteredClient().getClientId());
                        Set<String> scopes = optionalOauthClient.isPresent() && Objects.nonNull(((Set<String>) claims.get("scope")))? ((Set<String>) claims.get("scope")).stream().collect(Collectors.toSet()): new HashSet<>();
                        if(optionalOauthClient.isPresent()) {
                            optionalOauthClient.get().getScope().forEach(s -> {
                                scopes.add(s);
                            });
                        }
                        claims.put("scope", scopes);
                    } else {
                        Optional<User> userOptional = userRepository.findOneByUsername(claims.get("sub").toString());
                        Set<String> scopes = userOptional.isPresent() && Objects.nonNull(((Set<String>) claims.get("scope")))? ((Set<String>) claims.get("scope")).stream().collect(Collectors.toSet()): new HashSet<>();
                        if(userOptional.isPresent()) {
                            userOptional.get().getRoles().forEach(role -> {
                                scopes.add(role.getName().replace("ROLE_",""));
                            });
                        }
                        claims.put("scope", scopes);
                    }
                    claims.put("tenant", TenantContext.getCurrentTenant());
                });
            }
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                context.getClaims().claims(claims -> {
                    claims.put("iss", TenantContext.getCurrentIssuer());
                });
            }
        };
    }

    @Bean
    public TenantContextListener tenantContextListener() {
        return new TenantContextListener(this.tenantRepository);
    }

    @Bean
    @Primary
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new ResourceTransactionManager() {
            @Override
            public Object getResourceFactory() {
                return null;
            }

            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                return null;
            }

            @Override
            public void commit(TransactionStatus status) throws TransactionException {

            }

            @Override
            public void rollback(TransactionStatus status) throws TransactionException {

            }
        };
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(AppConfig.REMEMBER_ME_KEY, userDetailsService, encodingAlgorithm);
        rememberMe.setTokenValiditySeconds(3600 * 24 * 365);
        rememberMe.setParameter("remember-me");
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }

}
