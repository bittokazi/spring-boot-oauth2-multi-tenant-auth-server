package com.bittokazi.oauth2.auth.server.app.repositories.master;

import com.bittokazi.oauth2.auth.server.app.models.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {

    Optional<Tenant> findOneByCompanyKey(String companyKey);

    Optional<Tenant> findOneByLogo(String logo);

    Optional<Tenant> findOneByDomain(String domain);

}
