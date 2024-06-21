package com.bittokazi.oauth2.auth.server.database;

import com.bittokazi.oauth2.auth.server.config.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * CurrentTenantResolverImpl implements CurrentTenantIdentifierResolver of
 * hibernate to get information about current tenant from attribute.
 *
 * @author Bitto Kazi
 */
@Component
public class CurrentTenantResolverImpl implements CurrentTenantIdentifierResolver {
    private final Logger logger = LoggerFactory.getLogger(CurrentTenantResolverImpl.class);

    /**
     * @return Current tenant information from attribute called
     *         CURRENT_TENANT_IDENTIFIER. It is called in every request as it is
     *         called in interceptor.
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        if (TenantContext.getCurrentDataTenant() != null) {
            return TenantContext.getCurrentDataTenant();
        }
        return "UNKNOWN_TENANT";
    }

    /**
     * Just an override method of CurrentTenantIdentifierResolver
     *
     * @return true allways.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

