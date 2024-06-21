package com.bittokazi.oauth2.auth.server.config.security.oauth2;

import com.bittokazi.oauth2.auth.server.app.models.tenant.security.RoleOauth;
import com.bittokazi.oauth2.auth.server.app.models.tenant.security.UserOauth;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.database.MultiTenantConnectionProviderImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.lang.Nullable;
import org.springframework.security.cas.jackson2.CasJackson2Module;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import org.springframework.security.web.server.jackson2.WebServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Transactional
public class CustomJdbcOAuth2AuthorizationService implements OAuth2AuthorizationService {
    private static final String COLUMN_NAMES = "id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at,authorization_code_metadata,access_token_value,access_token_issued_at,access_token_expires_at,access_token_metadata,access_token_type,access_token_scopes,oidc_id_token_value,oidc_id_token_issued_at,oidc_id_token_expires_at,oidc_id_token_metadata,refresh_token_value,refresh_token_issued_at,refresh_token_expires_at,refresh_token_metadata,user_code_value,user_code_issued_at,user_code_expires_at,user_code_metadata,device_code_value,device_code_issued_at,device_code_expires_at,device_code_metadata";
    private static final String TABLE_NAME = "oauth2_authorization";
    private static final String PK_FILTER = "id = ?";
    private static final String UNKNOWN_TOKEN_TYPE_FILTER = "state = ? OR authorization_code_value = ? OR access_token_value = ? OR oidc_id_token_value = ? OR refresh_token_value = ? OR user_code_value = ? OR device_code_value = ?";
    private static final String STATE_FILTER = "state = ?";
    private static final String AUTHORIZATION_CODE_FILTER = "authorization_code_value = ?";
    private static final String ACCESS_TOKEN_FILTER = "access_token_value = ?";
    private static final String ID_TOKEN_FILTER = "oidc_id_token_value = ?";
    private static final String REFRESH_TOKEN_FILTER = "refresh_token_value = ?";
    private static final String USER_CODE_FILTER = "user_code_value = ?";
    private static final String DEVICE_CODE_FILTER = "device_code_value = ?";
    private static final String LOAD_AUTHORIZATION_SQL = "SELECT id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at,authorization_code_metadata,access_token_value,access_token_issued_at,access_token_expires_at,access_token_metadata,access_token_type,access_token_scopes,oidc_id_token_value,oidc_id_token_issued_at,oidc_id_token_expires_at,oidc_id_token_metadata,refresh_token_value,refresh_token_issued_at,refresh_token_expires_at,refresh_token_metadata,user_code_value,user_code_issued_at,user_code_expires_at,user_code_metadata,device_code_value,device_code_issued_at,device_code_expires_at,device_code_metadata FROM oauth2_authorization WHERE ";
    private static final String SAVE_AUTHORIZATION_SQL = "INSERT INTO oauth2_authorization (id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at,authorization_code_metadata,access_token_value,access_token_issued_at,access_token_expires_at,access_token_metadata,access_token_type,access_token_scopes,oidc_id_token_value,oidc_id_token_issued_at,oidc_id_token_expires_at,oidc_id_token_metadata,refresh_token_value,refresh_token_issued_at,refresh_token_expires_at,refresh_token_metadata,user_code_value,user_code_issued_at,user_code_expires_at,user_code_metadata,device_code_value,device_code_issued_at,device_code_expires_at,device_code_metadata) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_AUTHORIZATION_SQL = "UPDATE oauth2_authorization SET registered_client_id = ?, principal_name = ?, authorization_grant_type = ?, authorized_scopes = ?, attributes = ?, state = ?, authorization_code_value = ?, authorization_code_issued_at = ?, authorization_code_expires_at = ?, authorization_code_metadata = ?, access_token_value = ?, access_token_issued_at = ?, access_token_expires_at = ?, access_token_metadata = ?, access_token_type = ?, access_token_scopes = ?, oidc_id_token_value = ?, oidc_id_token_issued_at = ?, oidc_id_token_expires_at = ?, oidc_id_token_metadata = ?, refresh_token_value = ?, refresh_token_issued_at = ?, refresh_token_expires_at = ?, refresh_token_metadata = ?, user_code_value = ?, user_code_issued_at = ?, user_code_expires_at = ?, user_code_metadata = ?, device_code_value = ?, device_code_issued_at = ?, device_code_expires_at = ?, device_code_metadata = ? WHERE id = ?";
    private static final String REMOVE_AUTHORIZATION_SQL = "DELETE FROM oauth2_authorization WHERE id = ?";
    private static Map<String, CustomJdbcOAuth2AuthorizationService.ColumnMetadata> columnMetadataMap;
    private final JdbcOperations jdbcOperations;
    private final LobHandler lobHandler;
    private RowMapper<OAuth2Authorization> authorizationRowMapper;
    private Function<OAuth2Authorization, List<SqlParameterValue>> authorizationParametersMapper;

    private MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl;

    private RegisteredClientRepository registeredClientRepository;

    public CustomJdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository, MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl) {
        this(jdbcOperations, registeredClientRepository, new DefaultLobHandler());
        this.multiTenantConnectionProviderImpl = multiTenantConnectionProviderImpl;
        this.registeredClientRepository = registeredClientRepository;
    }

    public CustomJdbcOAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository, LobHandler lobHandler) {
        Assert.notNull(jdbcOperations, "jdbcOperations cannot be null");
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(lobHandler, "lobHandler cannot be null");
        this.jdbcOperations = jdbcOperations;
        this.lobHandler = lobHandler;
        CustomJdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new CustomJdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        authorizationRowMapper.setLobHandler(lobHandler);
        this.authorizationRowMapper = authorizationRowMapper;
        this.authorizationParametersMapper = new CustomJdbcOAuth2AuthorizationService.OAuth2AuthorizationParametersMapper();
        initColumnMetadata(jdbcOperations);
    }

    private String getTenantId() {
        return TenantContext.getCurrentTenant();
    }

    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        OAuth2Authorization existingAuthorization = this.findById(authorization.getId());
        if (existingAuthorization == null) {
            this.insertAuthorization(authorization);
        } else {
            this.updateAuthorization(authorization);
        }

    }

    private void updateAuthorization(OAuth2Authorization authorization) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));





        List<SqlParameterValue> parameters = (List)this.authorizationParametersMapper.apply(authorization);
        SqlParameterValue id = (SqlParameterValue)parameters.remove(0);
        parameters.add(id);
        LobCreator lobCreator = this.lobHandler.getLobCreator();

        try {

            PreparedStatementSetter pss = new CustomJdbcOAuth2AuthorizationService.LobCreatorArgumentPreparedStatementSetter(lobCreator, parameters.toArray());
            jdbcOperations.update("UPDATE oauth2_authorization SET registered_client_id = ?, principal_name = ?, authorization_grant_type = ?, authorized_scopes = ?, attributes = ?, state = ?, authorization_code_value = ?, authorization_code_issued_at = ?, authorization_code_expires_at = ?, authorization_code_metadata = ?, access_token_value = ?, access_token_issued_at = ?, access_token_expires_at = ?, access_token_metadata = ?, access_token_type = ?, access_token_scopes = ?, oidc_id_token_value = ?, oidc_id_token_issued_at = ?, oidc_id_token_expires_at = ?, oidc_id_token_metadata = ?, refresh_token_value = ?, refresh_token_issued_at = ?, refresh_token_expires_at = ?, refresh_token_metadata = ?, user_code_value = ?, user_code_issued_at = ?, user_code_expires_at = ?, user_code_metadata = ?, device_code_value = ?, device_code_issued_at = ?, device_code_expires_at = ?, device_code_metadata = ? WHERE id = ?", pss);
        } catch (Throwable var8) {
            if (lobCreator != null) {
                try {
                    lobCreator.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (lobCreator != null) {
            lobCreator.close();
        }

    }

    private void insertAuthorization(OAuth2Authorization authorization) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));



        //        RegisteredClient registeredClient = registeredClientRepository.findById(authorization.getRegisteredClientId());
//
//        if(registeredClient.getClientSettings().isRequireAuthorizationConsent()) {
//            SqlParameterValue[] _parameters = new SqlParameterValue[]{new SqlParameterValue(12, registeredClient.getId()), new SqlParameterValue(12, authorization.getPrincipalName())};
//            PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(_parameters);
//            CustomJdbcOAuth2AuthorizationConsentService.OAuth2AuthorizationConsentRowMapper authorizationConsentRowMapper  = authorizationConsentRowMapper = new CustomJdbcOAuth2AuthorizationConsentService.OAuth2AuthorizationConsentRowMapper(registeredClientRepository);
//            List<OAuth2AuthorizationConsent> result = jdbcOperations.query("SELECT registered_client_id, principal_name, authorities FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name = ?", pss, authorizationConsentRowMapper);
//
//            if((UsernamePasswordAuthenticationToken) authorization.getAttributes().get("java.security.Principal") instanceof UsernamePasswordAuthenticationToken) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authorization.getAttributes().get("java.security.Principal");
//                List<GrantedAuthority> rls = usernamePasswordAuthenticationToken.getAuthorities().stream().collect(Collectors.toList());
//                if(!result.isEmpty()) {
//                    OAuth2AuthorizationConsent oAuth2AuthorizationConsent = (OAuth2AuthorizationConsent)result.get(0);
//                    oAuth2AuthorizationConsent.getScopes().forEach(s -> {
//                        if(!rls.contains(new SimpleGrantedAuthority("SCOPE_"+s))) rls.add(new SimpleGrantedAuthority("SCOPE_"+s));
//                    });
//                    usernamePasswordAuthenticationToken.getPrincipal();
//                    UserOauth userOauth = ((UserOauth) usernamePasswordAuthenticationToken.getPrincipal());
//                    userOauth.setAuthorities(rls);
//                    UsernamePasswordAuthenticationToken _token = new UsernamePasswordAuthenticationToken(userOauth,
//                            usernamePasswordAuthenticationToken.getCredentials(),
//                            rls);
////                        _token.setAuthenticated(usernamePasswordAuthenticationToken.isAuthenticated());
//                    _token.setDetails(usernamePasswordAuthenticationToken.getDetails());
////                    authorization.getAttributes().put("java.security.Principal", _token);
//
//                    authorization = OAuth2Authorization.from(authorization)
//                            .attribute("java.security.Principal", _token)
//                            .build();
//                }
//            }
//        }


        List<SqlParameterValue> parameters = (List)this.authorizationParametersMapper.apply(authorization);
        LobCreator lobCreator = this.lobHandler.getLobCreator();

        try {
            PreparedStatementSetter pss = new CustomJdbcOAuth2AuthorizationService.LobCreatorArgumentPreparedStatementSetter(lobCreator, parameters.toArray());
            jdbcOperations.update("INSERT INTO oauth2_authorization (id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at,authorization_code_metadata,access_token_value,access_token_issued_at,access_token_expires_at,access_token_metadata,access_token_type,access_token_scopes,oidc_id_token_value,oidc_id_token_issued_at,oidc_id_token_expires_at,oidc_id_token_metadata,refresh_token_value,refresh_token_issued_at,refresh_token_expires_at,refresh_token_metadata,user_code_value,user_code_issued_at,user_code_expires_at,user_code_metadata,device_code_value,device_code_issued_at,device_code_expires_at,device_code_metadata) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", pss);
        } catch (Throwable var7) {
            if (lobCreator != null) {
                try {
                    lobCreator.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }

            throw var7;
        }

        if (lobCreator != null) {
            lobCreator.close();
        }

    }

    public void remove(OAuth2Authorization authorization) {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));

        Assert.notNull(authorization, "authorization cannot be null");
        SqlParameterValue[] parameters = new SqlParameterValue[]{new SqlParameterValue(12, authorization.getId())};
        PreparedStatementSetter pss = new ArgumentPreparedStatementSetter(parameters);
        jdbcOperations.update("DELETE FROM oauth2_authorization WHERE id = ?", pss);
    }

    @Nullable
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        List<SqlParameterValue> parameters = new ArrayList();
        parameters.add(new SqlParameterValue(12, id));
        return this.findBy("id = ?", parameters);
    }

    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        List<SqlParameterValue> parameters = new ArrayList();
        if (tokenType == null) {
            parameters.add(new SqlParameterValue(12, token));
            parameters.add(mapToSqlParameter("authorization_code_value", token));
            parameters.add(mapToSqlParameter("access_token_value", token));
            parameters.add(mapToSqlParameter("oidc_id_token_value", token));
            parameters.add(mapToSqlParameter("refresh_token_value", token));
            parameters.add(mapToSqlParameter("user_code_value", token));
            parameters.add(mapToSqlParameter("device_code_value", token));
            return this.findBy("state = ? OR authorization_code_value = ? OR access_token_value = ? OR oidc_id_token_value = ? OR refresh_token_value = ? OR user_code_value = ? OR device_code_value = ?", parameters);
        } else if ("state".equals(tokenType.getValue())) {
            parameters.add(new SqlParameterValue(12, token));
            return this.findBy("state = ?", parameters);
        } else if ("code".equals(tokenType.getValue())) {
            parameters.add(mapToSqlParameter("authorization_code_value", token));
            return this.findBy("authorization_code_value = ?", parameters);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            parameters.add(mapToSqlParameter("access_token_value", token));
            return this.findBy("access_token_value = ?", parameters);
        } else if ("id_token".equals(tokenType.getValue())) {
            parameters.add(mapToSqlParameter("oidc_id_token_value", token));
            return this.findBy("oidc_id_token_value = ?", parameters);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            parameters.add(mapToSqlParameter("refresh_token_value", token));
            return this.findBy("refresh_token_value = ?", parameters);
        } else if ("user_code".equals(tokenType.getValue())) {
            parameters.add(mapToSqlParameter("user_code_value", token));
            return this.findBy("user_code_value = ?", parameters);
        } else if ("device_code".equals(tokenType.getValue())) {
            parameters.add(mapToSqlParameter("device_code_value", token));
            return this.findBy("device_code_value = ?", parameters);
        } else {
            return null;
        }
    }

    private OAuth2Authorization findBy(String filter, List<SqlParameterValue> parameters) {
        LobCreator lobCreator = this.getLobHandler().getLobCreator();

        OAuth2Authorization var6;
        try {
            PreparedStatementSetter pss = new CustomJdbcOAuth2AuthorizationService.LobCreatorArgumentPreparedStatementSetter(lobCreator, parameters.toArray());
            List<OAuth2Authorization> result = this.getJdbcOperations().query("SELECT id, registered_client_id, principal_name, authorization_grant_type, authorized_scopes, attributes, state, authorization_code_value, authorization_code_issued_at, authorization_code_expires_at,authorization_code_metadata,access_token_value,access_token_issued_at,access_token_expires_at,access_token_metadata,access_token_type,access_token_scopes,oidc_id_token_value,oidc_id_token_issued_at,oidc_id_token_expires_at,oidc_id_token_metadata,refresh_token_value,refresh_token_issued_at,refresh_token_expires_at,refresh_token_metadata,user_code_value,user_code_issued_at,user_code_expires_at,user_code_metadata,device_code_value,device_code_issued_at,device_code_expires_at,device_code_metadata FROM oauth2_authorization WHERE " + filter, pss, this.getAuthorizationRowMapper());
            var6 = !result.isEmpty() ? (OAuth2Authorization)result.get(0) : null;
        } catch (Throwable var8) {
            if (lobCreator != null) {
                try {
                    lobCreator.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }

            throw var8;
        }

        if (lobCreator != null) {
            lobCreator.close();
        }

        return var6;
    }

    public final void setAuthorizationRowMapper(RowMapper<OAuth2Authorization> authorizationRowMapper) {
        Assert.notNull(authorizationRowMapper, "authorizationRowMapper cannot be null");
        this.authorizationRowMapper = authorizationRowMapper;
    }

    public final void setAuthorizationParametersMapper(Function<OAuth2Authorization, List<SqlParameterValue>> authorizationParametersMapper) {
        Assert.notNull(authorizationParametersMapper, "authorizationParametersMapper cannot be null");
        this.authorizationParametersMapper = authorizationParametersMapper;
    }

    protected final JdbcOperations getJdbcOperations() {
        String tenantId = getTenantId();
        JdbcOperations jdbcOperations = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource(tenantId));
        return jdbcOperations;
    }

    protected final LobHandler getLobHandler() {
        return this.lobHandler;
    }

    protected final RowMapper<OAuth2Authorization> getAuthorizationRowMapper() {
        return this.authorizationRowMapper;
    }

    protected final Function<OAuth2Authorization, List<SqlParameterValue>> getAuthorizationParametersMapper() {
        return this.authorizationParametersMapper;
    }

    private static void initColumnMetadata(JdbcOperations jdbcOperations) {
        columnMetadataMap = new HashMap();
        CustomJdbcOAuth2AuthorizationService.ColumnMetadata columnMetadata = getColumnMetadata(jdbcOperations, "attributes", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "authorization_code_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "authorization_code_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "access_token_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "access_token_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "oidc_id_token_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "oidc_id_token_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "refresh_token_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "refresh_token_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "user_code_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "user_code_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "device_code_value", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
        columnMetadata = getColumnMetadata(jdbcOperations, "device_code_metadata", 2004);
        columnMetadataMap.put(columnMetadata.getColumnName(), columnMetadata);
    }

    private static CustomJdbcOAuth2AuthorizationService.ColumnMetadata getColumnMetadata(JdbcOperations jdbcOperations, String columnName, int defaultDataType) {
        Integer dataType = (Integer)jdbcOperations.execute((ConnectionCallback<Integer>) (conn) -> {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            ResultSet rs = databaseMetaData.getColumns((String)null, (String)null, "oauth2_authorization", columnName);
            if (rs.next()) {
                return rs.getInt("DATA_TYPE");
            } else {
                rs = databaseMetaData.getColumns((String)null, (String)null, "oauth2_authorization".toUpperCase(), columnName.toUpperCase());
                return rs.next() ? rs.getInt("DATA_TYPE") : null;
            }
        });
        return new CustomJdbcOAuth2AuthorizationService.ColumnMetadata(columnName, dataType != null ? dataType : defaultDataType);
    }

    private static SqlParameterValue mapToSqlParameter(String columnName, String value) {
        CustomJdbcOAuth2AuthorizationService.ColumnMetadata columnMetadata = (CustomJdbcOAuth2AuthorizationService.ColumnMetadata)columnMetadataMap.get(columnName);
        return 2004 == columnMetadata.getDataType() && StringUtils.hasText(value) ? new SqlParameterValue(2004, value.getBytes(StandardCharsets.UTF_8)) : new SqlParameterValue(columnMetadata.getDataType(), value);
    }

    public static class OAuth2AuthorizationRowMapper implements RowMapper<OAuth2Authorization> {
        private final RegisteredClientRepository registeredClientRepository;
        private LobHandler lobHandler = new DefaultLobHandler();
        private ObjectMapper objectMapper = new ObjectMapper();

        public OAuth2AuthorizationRowMapper(RegisteredClientRepository registeredClientRepository) {
            Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
            this.registeredClientRepository = registeredClientRepository;
            ClassLoader classLoader = CustomJdbcOAuth2AuthorizationService.class.getClassLoader();
            List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
            this.objectMapper.registerModules(securityModules);
            this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
            this.objectMapper.registerModule(new Hibernate6Module());
            this.objectMapper.registerModule(new CoreJackson2Module());
            this.objectMapper.registerModule(new CasJackson2Module());
            this.objectMapper.registerModule(new WebJackson2Module());
            this.objectMapper.registerModule(new WebServletJackson2Module());
            this.objectMapper.registerModule(new WebServerJackson2Module());
            this.objectMapper.addMixIn(UserOauth.class, UserDetails.class);
            this.objectMapper.addMixIn(RoleOauth.class, UserDetails.class);
            this.objectMapper.addMixIn(RoleOauth.class, UserOauth.class);
        }

        @Transactional
        public OAuth2Authorization mapRow(ResultSet rs, int rowNum) throws SQLException {
            String registeredClientId = rs.getString("registered_client_id");
            RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
            if (registeredClient == null) {
                throw new DataRetrievalFailureException("The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
            } else {
                OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient);
                String id = rs.getString("id");
                String principalName = rs.getString("principal_name");
                String authorizationGrantType = rs.getString("authorization_grant_type");
                Set<String> authorizedScopes = Collections.emptySet();
                String authorizedScopesString = rs.getString("authorized_scopes");
                if (authorizedScopesString != null) {
                    authorizedScopes = StringUtils.commaDelimitedListToSet(authorizedScopesString);
                }

                Map<String, Object> attributes = this.parseMap(this.getLobValue(rs, "attributes"));
                builder.id(id).principalName(principalName).authorizationGrantType(new AuthorizationGrantType(authorizationGrantType)).authorizedScopes(authorizedScopes).attributes((attrs) -> {
                    attrs.putAll(attributes);
                });
                String state = rs.getString("state");
                if (StringUtils.hasText(state)) {
                    builder.attribute("state", state);
                }

                String authorizationCodeValue = this.getLobValue(rs, "authorization_code_value");
                Instant tokenIssuedAt;
                Instant tokenExpiresAt;
                if (StringUtils.hasText(authorizationCodeValue)) {
                    tokenIssuedAt = rs.getTimestamp("authorization_code_issued_at").toInstant();
                    tokenExpiresAt = rs.getTimestamp("authorization_code_expires_at").toInstant();
                    Map<String, Object> authorizationCodeMetadata = this.parseMap(this.getLobValue(rs, "authorization_code_metadata"));
                    OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(authorizationCodeValue, tokenIssuedAt, tokenExpiresAt);
                    builder.token(authorizationCode, (metadata) -> {
                        metadata.putAll(authorizationCodeMetadata);
                    });
                }

                String accessTokenValue = this.getLobValue(rs, "access_token_value");
                String deviceCodeValue;
                if (StringUtils.hasText(accessTokenValue)) {
                    tokenIssuedAt = rs.getTimestamp("access_token_issued_at").toInstant();
                    tokenExpiresAt = rs.getTimestamp("access_token_expires_at").toInstant();
                    Map<String, Object> accessTokenMetadata = this.parseMap(this.getLobValue(rs, "access_token_metadata"));
                    OAuth2AccessToken.TokenType tokenType = null;
                    if (TokenType.BEARER.getValue().equalsIgnoreCase(rs.getString("access_token_type"))) {
                        tokenType = TokenType.BEARER;
                    }

                    Set<String> scopes = Collections.emptySet();
                    deviceCodeValue = rs.getString("access_token_scopes");
                    if (deviceCodeValue != null) {
                        scopes = StringUtils.commaDelimitedListToSet(deviceCodeValue);
                    }

                    OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, accessTokenValue, tokenIssuedAt, tokenExpiresAt, scopes);
                    builder.token(accessToken, (metadata) -> {
                        metadata.putAll(accessTokenMetadata);
                    });
                }

                String oidcIdTokenValue = this.getLobValue(rs, "oidc_id_token_value");
                if (StringUtils.hasText(oidcIdTokenValue)) {
                    tokenIssuedAt = rs.getTimestamp("oidc_id_token_issued_at").toInstant();
                    tokenExpiresAt = rs.getTimestamp("oidc_id_token_expires_at").toInstant();
                    Map<String, Object> oidcTokenMetadata = this.parseMap(this.getLobValue(rs, "oidc_id_token_metadata"));
                    OidcIdToken oidcToken = new OidcIdToken(oidcIdTokenValue, tokenIssuedAt, tokenExpiresAt, (Map)oidcTokenMetadata.get(Token.CLAIMS_METADATA_NAME));
                    builder.token(oidcToken, (metadata) -> {
                        metadata.putAll(oidcTokenMetadata);
                    });
                }

                String refreshTokenValue = this.getLobValue(rs, "refresh_token_value");
                Map userCodeMetadata;
                if (StringUtils.hasText(refreshTokenValue)) {
                    tokenIssuedAt = rs.getTimestamp("refresh_token_issued_at").toInstant();
                    tokenExpiresAt = null;
                    Timestamp refreshTokenExpiresAt = rs.getTimestamp("refresh_token_expires_at");
                    if (refreshTokenExpiresAt != null) {
                        tokenExpiresAt = refreshTokenExpiresAt.toInstant();
                    }

                    userCodeMetadata = this.parseMap(this.getLobValue(rs, "refresh_token_metadata"));
                    OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, tokenIssuedAt, tokenExpiresAt);
                    Map finalUserCodeMetadata = userCodeMetadata;
                    builder.token(refreshToken, (metadata) -> {
                        metadata.putAll(finalUserCodeMetadata);
                    });
                }

                String userCodeValue = this.getLobValue(rs, "user_code_value");
                if (StringUtils.hasText(userCodeValue)) {
                    tokenIssuedAt = rs.getTimestamp("user_code_issued_at").toInstant();
                    tokenExpiresAt = rs.getTimestamp("user_code_expires_at").toInstant();
                    userCodeMetadata = this.parseMap(this.getLobValue(rs, "user_code_metadata"));
                    OAuth2UserCode userCode = new OAuth2UserCode(userCodeValue, tokenIssuedAt, tokenExpiresAt);
                    Map finalUserCodeMetadata1 = userCodeMetadata;
                    builder.token(userCode, (metadata) -> {
                        metadata.putAll(finalUserCodeMetadata1);
                    });
                }

                deviceCodeValue = this.getLobValue(rs, "device_code_value");
                if (StringUtils.hasText(deviceCodeValue)) {
                    tokenIssuedAt = rs.getTimestamp("device_code_issued_at").toInstant();
                    tokenExpiresAt = rs.getTimestamp("device_code_expires_at").toInstant();
                    Map<String, Object> deviceCodeMetadata = this.parseMap(this.getLobValue(rs, "device_code_metadata"));
                    OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(deviceCodeValue, tokenIssuedAt, tokenExpiresAt);
                    builder.token(deviceCode, (metadata) -> {
                        metadata.putAll(deviceCodeMetadata);
                    });
                }

                return builder.build();
            }
        }

        private String getLobValue(ResultSet rs, String columnName) throws SQLException {
            String columnValue = null;
            CustomJdbcOAuth2AuthorizationService.ColumnMetadata columnMetadata = (CustomJdbcOAuth2AuthorizationService.ColumnMetadata) CustomJdbcOAuth2AuthorizationService.columnMetadataMap.get(columnName);
            if (2004 == columnMetadata.getDataType()) {
                byte[] columnValueBytes = this.lobHandler.getBlobAsBytes(rs, columnName);
                if (columnValueBytes != null) {
                    columnValue = new String(columnValueBytes, StandardCharsets.UTF_8);
                }
            } else if (2005 == columnMetadata.getDataType()) {
                columnValue = this.lobHandler.getClobAsString(rs, columnName);
            } else {
                columnValue = rs.getString(columnName);
            }

            return columnValue;
        }

        public final void setLobHandler(LobHandler lobHandler) {
            Assert.notNull(lobHandler, "lobHandler cannot be null");
            this.lobHandler = lobHandler;
        }

        public final void setObjectMapper(ObjectMapper objectMapper) {
            Assert.notNull(objectMapper, "objectMapper cannot be null");
            this.objectMapper = objectMapper;
        }

        protected final RegisteredClientRepository getRegisteredClientRepository() {
            return this.registeredClientRepository;
        }

        protected final LobHandler getLobHandler() {
            return this.lobHandler;
        }

        protected final ObjectMapper getObjectMapper() {
            return this.objectMapper;
        }

        private Map<String, Object> parseMap(String data) {
            try {
                return (Map)this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception var3) {
                throw new IllegalArgumentException(var3.getMessage(), var3);
            }
        }
    }

    public static class OAuth2AuthorizationParametersMapper implements Function<OAuth2Authorization, List<SqlParameterValue>> {
        private ObjectMapper objectMapper = new ObjectMapper();

        public OAuth2AuthorizationParametersMapper() {
            ClassLoader classLoader = CustomJdbcOAuth2AuthorizationService.class.getClassLoader();
            List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
            this.objectMapper.registerModules(securityModules);
            this.objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        }

        public List<SqlParameterValue> apply(OAuth2Authorization authorization) {
            List<SqlParameterValue> parameters = new ArrayList();
            parameters.add(new SqlParameterValue(12, authorization.getId()));
            parameters.add(new SqlParameterValue(12, authorization.getRegisteredClientId()));
            parameters.add(new SqlParameterValue(12, authorization.getPrincipalName()));
            parameters.add(new SqlParameterValue(12, authorization.getAuthorizationGrantType().getValue()));
            String authorizedScopes = null;
            if (!CollectionUtils.isEmpty(authorization.getAuthorizedScopes())) {
                authorizedScopes = StringUtils.collectionToDelimitedString(authorization.getAuthorizedScopes(), ",");
            }

            parameters.add(new SqlParameterValue(12, authorizedScopes));
            String attributes = this.writeMap(authorization.getAttributes());
            parameters.add(CustomJdbcOAuth2AuthorizationService.mapToSqlParameter("attributes", attributes));
            String state = null;
            String authorizationState = (String)authorization.getAttribute("state");
            if (StringUtils.hasText(authorizationState)) {
                state = authorizationState;
            }

            parameters.add(new SqlParameterValue(12, state));
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
            List<SqlParameterValue> authorizationCodeSqlParameters = this.toSqlParameterList("authorization_code_value", "authorization_code_metadata", authorizationCode);
            parameters.addAll(authorizationCodeSqlParameters);
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
            List<SqlParameterValue> accessTokenSqlParameters = this.toSqlParameterList("access_token_value", "access_token_metadata", accessToken);
            parameters.addAll(accessTokenSqlParameters);
            String accessTokenType = null;
            String accessTokenScopes = null;
            if (accessToken != null) {
                accessTokenType = ((OAuth2AccessToken)accessToken.getToken()).getTokenType().getValue();
                if (!CollectionUtils.isEmpty(((OAuth2AccessToken)accessToken.getToken()).getScopes())) {
                    accessTokenScopes = StringUtils.collectionToDelimitedString(((OAuth2AccessToken)accessToken.getToken()).getScopes(), ",");
                }
            }

            parameters.add(new SqlParameterValue(12, accessTokenType));
            parameters.add(new SqlParameterValue(12, accessTokenScopes));
            OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
            List<SqlParameterValue> oidcIdTokenSqlParameters = this.toSqlParameterList("oidc_id_token_value", "oidc_id_token_metadata", oidcIdToken);
            parameters.addAll(oidcIdTokenSqlParameters);
            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
            List<SqlParameterValue> refreshTokenSqlParameters = this.toSqlParameterList("refresh_token_value", "refresh_token_metadata", refreshToken);
            parameters.addAll(refreshTokenSqlParameters);
            OAuth2Authorization.Token<OAuth2UserCode> userCode = authorization.getToken(OAuth2UserCode.class);
            List<SqlParameterValue> userCodeSqlParameters = this.toSqlParameterList("user_code_value", "user_code_metadata", userCode);
            parameters.addAll(userCodeSqlParameters);
            OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode = authorization.getToken(OAuth2DeviceCode.class);
            List<SqlParameterValue> deviceCodeSqlParameters = this.toSqlParameterList("device_code_value", "device_code_metadata", deviceCode);
            parameters.addAll(deviceCodeSqlParameters);
            return parameters;
        }

        public final void setObjectMapper(ObjectMapper objectMapper) {
            Assert.notNull(objectMapper, "objectMapper cannot be null");
            this.objectMapper = objectMapper;
        }

        protected final ObjectMapper getObjectMapper() {
            return this.objectMapper;
        }

        private <T extends OAuth2Token> List<SqlParameterValue> toSqlParameterList(String tokenColumnName, String tokenMetadataColumnName, OAuth2Authorization.Token<T> token) {
            List<SqlParameterValue> parameters = new ArrayList();
            String tokenValue = null;
            Timestamp tokenIssuedAt = null;
            Timestamp tokenExpiresAt = null;
            String metadata = null;
            if (token != null) {
                tokenValue = token.getToken().getTokenValue();
                if (token.getToken().getIssuedAt() != null) {
                    tokenIssuedAt = Timestamp.from(token.getToken().getIssuedAt());
                }

                if (token.getToken().getExpiresAt() != null) {
                    tokenExpiresAt = Timestamp.from(token.getToken().getExpiresAt());
                }

                metadata = this.writeMap(token.getMetadata());
            }

            parameters.add(CustomJdbcOAuth2AuthorizationService.mapToSqlParameter(tokenColumnName, tokenValue));
            parameters.add(new SqlParameterValue(93, tokenIssuedAt));
            parameters.add(new SqlParameterValue(93, tokenExpiresAt));
            parameters.add(CustomJdbcOAuth2AuthorizationService.mapToSqlParameter(tokenMetadataColumnName, metadata));
            return parameters;
        }

        private String writeMap(Map<String, Object> data) {
            try {
                return this.objectMapper.writeValueAsString(data);
            } catch (Exception var3) {
                throw new IllegalArgumentException(var3.getMessage(), var3);
            }
        }
    }

    private static final class LobCreatorArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter {
        private final LobCreator lobCreator;

        private LobCreatorArgumentPreparedStatementSetter(LobCreator lobCreator, Object[] args) {
            super(args);
            this.lobCreator = lobCreator;
        }

        protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
            if (argValue instanceof SqlParameterValue paramValue) {
                if (paramValue.getSqlType() == 2004) {
                    if (paramValue.getValue() != null) {
                        Assert.isInstanceOf(byte[].class, paramValue.getValue(), "Value of blob parameter must be byte[]");
                    }

                    byte[] valueBytes = (byte[])paramValue.getValue();
                    this.lobCreator.setBlobAsBytes(ps, parameterPosition, valueBytes);
                    return;
                }

                if (paramValue.getSqlType() == 2005) {
                    if (paramValue.getValue() != null) {
                        Assert.isInstanceOf(String.class, paramValue.getValue(), "Value of clob parameter must be String");
                    }

                    String valueString = (String)paramValue.getValue();
                    this.lobCreator.setClobAsString(ps, parameterPosition, valueString);
                    return;
                }
            }

            super.doSetValue(ps, parameterPosition, argValue);
        }
    }

    private static final class ColumnMetadata {
        private final String columnName;
        private final int dataType;

        private ColumnMetadata(String columnName, int dataType) {
            this.columnName = columnName;
            this.dataType = dataType;
        }

        private String getColumnName() {
            return this.columnName;
        }

        private int getDataType() {
            return this.dataType;
        }
    }
}

