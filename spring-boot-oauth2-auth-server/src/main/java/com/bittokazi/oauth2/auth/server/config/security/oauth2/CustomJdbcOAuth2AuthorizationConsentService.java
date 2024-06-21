package com.bittokazi.oauth2.auth.server.config.security.oauth2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.database.MultiTenantConnectionProviderImpl;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.*;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


@Transactional
public class CustomJdbcOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    private static final String COLUMN_NAMES = "registered_client_id, principal_name, authorities";
    private static final String TABLE_NAME = "oauth2_authorization_consent";
    private static final String PK_FILTER = "registered_client_id = ? AND principal_name = ?";
    private static final String LOAD_AUTHORIZATION_CONSENT_SQL = "SELECT registered_client_id, principal_name, authorities FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name = ?";
    private static final String SAVE_AUTHORIZATION_CONSENT_SQL = "INSERT INTO oauth2_authorization_consent (registered_client_id, principal_name, authorities) VALUES (?, ?, ?)";
    private static final String UPDATE_AUTHORIZATION_CONSENT_SQL = "UPDATE oauth2_authorization_consent SET authorities = ? WHERE registered_client_id = ? AND principal_name = ?";
    private static final String REMOVE_AUTHORIZATION_CONSENT_SQL = "DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name = ?";
    private final JdbcOperations jdbcOperations;
    private RowMapper<OAuth2AuthorizationConsent> authorizationConsentRowMapper;
    private Function<OAuth2AuthorizationConsent, List<SqlParameterValue>> authorizationConsentParametersMapper;

    private MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl;

    public CustomJdbcOAuth2AuthorizationConsentService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository, MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl) {
        Assert.notNull(jdbcOperations, "jdbcOperations cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        this.jdbcOperations = jdbcOperations;
        this.authorizationConsentRowMapper = new CustomJdbcOAuth2AuthorizationConsentService.OAuth2AuthorizationConsentRowMapper(registeredClientRepository);
        this.authorizationConsentParametersMapper = new CustomJdbcOAuth2AuthorizationConsentService.OAuth2AuthorizationConsentParametersMapper();
        this.multiTenantConnectionProviderImpl = multiTenantConnectionProviderImpl;
    }

    private String getTenantId() {
        return TenantContext.getCurrentTenant();
    }

    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        OAuth2AuthorizationConsent existingAuthorizationConsent = this.findById(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        if (existingAuthorizationConsent == null) {
            this.insertAuthorizationConsent(authorizationConsent);
        } else {
            this.updateAuthorizationConsent(authorizationConsent);
        }

    }

    private void updateAuthorizationConsent(OAuth2AuthorizationConsent authorizationConsent) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        List<SqlParameterValue> parameters = (List)this.authorizationConsentParametersMapper.apply(authorizationConsent);
        SqlParameterValue registeredClientId = (SqlParameterValue)parameters.remove(0);
        SqlParameterValue principalName = (SqlParameterValue)parameters.remove(0);
        parameters.add(registeredClientId);
        parameters.add(principalName);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        jdbcOperations.update("UPDATE oauth2_authorization_consent SET authorities = ? WHERE registered_client_id = ? AND principal_name = ?", pss);
    }

    private void insertAuthorizationConsent(OAuth2AuthorizationConsent authorizationConsent) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        List<SqlParameterValue> parameters = (List)this.authorizationConsentParametersMapper.apply(authorizationConsent);
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters.toArray());
        jdbcOperations.update("INSERT INTO oauth2_authorization_consent (registered_client_id, principal_name, authorities) VALUES (?, ?, ?)", pss);
    }

    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        SqlParameterValue[] parameters = new SqlParameterValue[]{new SqlParameterValue(12, authorizationConsent.getRegisteredClientId()), new SqlParameterValue(12, authorizationConsent.getPrincipalName())};
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters);
        jdbcOperations.update("DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name = ?", pss);
    }

    @Nullable
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        SqlParameterValue[] parameters = new SqlParameterValue[]{new SqlParameterValue(12, registeredClientId), new SqlParameterValue(12, principalName)};
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters);
        List<OAuth2AuthorizationConsent> result = jdbcOperations.query("SELECT registered_client_id, principal_name, authorities FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name = ?", pss, this.authorizationConsentRowMapper);
        return !result.isEmpty() ? (OAuth2AuthorizationConsent)result.get(0) : null;
    }

    public final void setAuthorizationConsentRowMapper(RowMapper<OAuth2AuthorizationConsent> authorizationConsentRowMapper) {
        Assert.notNull(authorizationConsentRowMapper, "authorizationConsentRowMapper cannot be null");
        this.authorizationConsentRowMapper = authorizationConsentRowMapper;
    }

    public final void setAuthorizationConsentParametersMapper(Function<OAuth2AuthorizationConsent, List<SqlParameterValue>> authorizationConsentParametersMapper) {
        Assert.notNull(authorizationConsentParametersMapper, "authorizationConsentParametersMapper cannot be null");
        this.authorizationConsentParametersMapper = authorizationConsentParametersMapper;
    }

    protected final JdbcOperations getJdbcOperations() {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        return jdbcOperations;
    }

    protected final RowMapper<OAuth2AuthorizationConsent> getAuthorizationConsentRowMapper() {
        return this.authorizationConsentRowMapper;
    }

    protected final Function<OAuth2AuthorizationConsent, List<SqlParameterValue>> getAuthorizationConsentParametersMapper() {
        return this.authorizationConsentParametersMapper;
    }

    public static class OAuth2AuthorizationConsentRowMapper implements RowMapper<OAuth2AuthorizationConsent> {
        private final RegisteredClientRepository registeredClientRepository;

        public OAuth2AuthorizationConsentRowMapper(RegisteredClientRepository registeredClientRepository) {
            Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
            this.registeredClientRepository = registeredClientRepository;
        }

        public OAuth2AuthorizationConsent mapRow(ResultSet rs, int rowNum) throws SQLException {
            String registeredClientId = rs.getString("registered_client_id");
            RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
            if (registeredClient == null) {
                throw new DataRetrievalFailureException("The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
            } else {
                String principalName = rs.getString("principal_name");
                OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(registeredClientId, principalName);
                String authorizationConsentAuthorities = rs.getString("authorities");
                if (authorizationConsentAuthorities != null) {
                    Iterator var8 = StringUtils.commaDelimitedListToSet(authorizationConsentAuthorities).iterator();

                    while(var8.hasNext()) {
                        String authority = (String)var8.next();
                        builder.authority(new SimpleGrantedAuthority(authority));
                    }
                }

                return builder.build();
            }
        }

        protected final RegisteredClientRepository getRegisteredClientRepository() {
            return this.registeredClientRepository;
        }
    }

    public static class OAuth2AuthorizationConsentParametersMapper implements Function<OAuth2AuthorizationConsent, List<SqlParameterValue>> {
        public OAuth2AuthorizationConsentParametersMapper() {
        }

        public List<SqlParameterValue> apply(OAuth2AuthorizationConsent authorizationConsent) {
            List<SqlParameterValue> parameters = new ArrayList();
            parameters.add(new SqlParameterValue(12, authorizationConsent.getRegisteredClientId()));
            parameters.add(new SqlParameterValue(12, authorizationConsent.getPrincipalName()));
            Set<String> authorities = new HashSet();
            Iterator var4 = authorizationConsent.getAuthorities().iterator();

            while(var4.hasNext()) {
                GrantedAuthority authority = (GrantedAuthority)var4.next();
                authorities.add(authority.getAuthority());
            }

            parameters.add(new SqlParameterValue(12, StringUtils.collectionToDelimitedString(authorities, ",")));
            return parameters;
        }
    }
}

