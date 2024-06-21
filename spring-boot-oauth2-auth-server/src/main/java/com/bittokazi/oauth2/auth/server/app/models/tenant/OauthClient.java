package com.bittokazi.oauth2.auth.server.app.models.tenant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "oauth_client_details")
public class OauthClient implements Serializable  {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "client_secret")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "client_authentication_method")
    private String clientAuthenticationMethod;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "require_consent")
    private Boolean requireConsent;

    @Column(name = "post_logout_url")
    private String postLogoutUrl;

    @Column(name = "token_type")
    private String tokenType;

    @Transient
    private Boolean generateSecret = false;

    @Transient
    private String newSecret;

    public Set<String> getScope() {
        try {
            String[] scopes = this.scope.split(",");
            return new HashSet(Arrays.asList(scopes));
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> getAuthorizedGrantTypes() {
        try {
            String[] grantTypes = this.authorizedGrantTypes.split(",");
            return new HashSet(Arrays.asList(grantTypes));
        } catch (Exception e) {
            return null;
        }
    }

    public Set<String> getWebServerRedirectUri() {
        try {
            String[] webServerRedirectUris = this.webServerRedirectUri.split(",");
            return new HashSet(Arrays.asList(webServerRedirectUris));
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getAdditionalInformation() {
        return new Gson().fromJson(this.additionalInformation, HashMap.class);
    }

}

