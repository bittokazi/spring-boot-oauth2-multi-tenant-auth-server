package com.bittokazi.oauth2.auth.server.app.services.tenant;

import com.bittokazi.oauth2.auth.server.app.models.master.Tenant;
import com.bittokazi.oauth2.auth.server.app.models.master.TenantInfo;
import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.repositories.master.TenantRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.config.AppConfig;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.database.MultiTenantConnectionProviderImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantService {

    private TenantRepository tenantRepository;

    private OauthClientRepository oauthClientRepository;

    private MultiTenantConnectionProviderImpl multiTenantConnectionProvider;

    public TenantService(TenantRepository tenantRepository,
            MultiTenantConnectionProviderImpl multiTenantConnectionProvider,
            OauthClientRepository oauthClientRepository) {
        this.tenantRepository = tenantRepository;
        this.oauthClientRepository = oauthClientRepository;
        this.multiTenantConnectionProvider = multiTenantConnectionProvider;
    }

    public ResponseEntity<?> addTenant(Tenant tenant) {
        if (!TenantContext.getCurrentDataTenant().equals("public"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        tenant = tenantRepository.save(tenant);
        multiTenantConnectionProvider.singleTenantCreation(tenant);
        return ResponseEntity.ok(tenant);
    }

    public ResponseEntity<?> getAllTenants() {
        if (!TenantContext.getCurrentDataTenant().equals("public"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(tenantRepository.findAll());
    }

    public ResponseEntity<?> getTenantInfo(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (!TenantContext.getCurrentDataTenant().equals("public"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String host = httpServletRequest.getHeader("host").replace("www.", "");
        if (System.getenv().get("DOMAIN").equals(host)) {
            return ResponseEntity.ok("{\"cpanel\": \"true\"}");
        } else {
            Optional<Tenant> tenant = tenantRepository.findOneByCompanyKey(host);
            if (tenant.isPresent())
                return ResponseEntity.ok(tenant.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");
    }

    public ResponseEntity<?> getTenant(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            String id) {
        if (!TenantContext.getCurrentDataTenant().equals("public"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional<Tenant> optional = tenantRepository.findById(id);
        return ResponseEntity.ok(optional.get());
    }

    public ResponseEntity<?> updateTenant(Tenant tenant, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        if (!TenantContext.getCurrentDataTenant().equals("public"))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(tenantRepository.save(tenant));
    }

    public ResponseEntity<?> info() {
        if (TenantContext.getCurrentDataTenant().equals("public")) {
            return ResponseEntity.ok(
                    TenantInfo
                            .builder()
                            .cpanel(true)
                            .enabledConfigPanel(true)
                            .name(AppConfig.DEFAULT_APP_NAME)
                            .build());
        }

        Optional<Tenant> tenant = tenantRepository
                .findOneByCompanyKey(
                        TenantContext
                                .getCurrentDataTenant());

        if (tenant.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                TenantInfo
                        .builder()
                        .name(
                                tenant.get()
                                        .getName())
                        .cpanel(false)
                        .enabledConfigPanel(
                                tenant.get()
                                        .getEnableConfigPanel())
                        .build());
    }
}
