package com.bittokazi.oauth2.auth.server.app.repositories.tenant.mfa;

import java.util.Optional;

import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.UserTwoFaSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTwoFaSecretRepository extends JpaRepository<UserTwoFaSecret, Long> {

    @Query(value = "SELECT * FROM user_two_fa_secret WHERE user_id = :userId", nativeQuery = true)
    Optional<UserTwoFaSecret> findByUserId(@Param("userId") String userId);

}
