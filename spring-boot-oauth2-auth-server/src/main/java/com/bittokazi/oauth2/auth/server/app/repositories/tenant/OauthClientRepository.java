package com.bittokazi.oauth2.auth.server.app.repositories.tenant;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClient, String> {

    @Query(value = "SELECT * FROM oauth_client_details WHERE id = :id", nativeQuery = true)
    Optional<OauthClient> findOneById(String id);

    @Query(value = "SELECT * FROM oauth_client_details WHERE client_id = :clientId", nativeQuery = true)
    Optional<OauthClient> findOneByClientId(String clientId);

}
