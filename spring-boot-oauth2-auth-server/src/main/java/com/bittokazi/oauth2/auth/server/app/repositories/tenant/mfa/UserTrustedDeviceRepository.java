package com.bittokazi.oauth2.auth.server.app.repositories.tenant.mfa;

import java.util.List;

import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.UserTrustedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserTrustedDeviceRepository extends JpaRepository<UserTrustedDevice, Long> {

    @Query(value = "SELECT * FROM user_trusted_device WHERE user_id = :userId", nativeQuery = true)
    List<UserTrustedDevice> findAllByUserId(@Param("userId") String userId);

    @Query(value = "SELECT * FROM user_trusted_device WHERE user_id = :userId AND instance_id = :instanceId", nativeQuery = true)
    List<UserTrustedDevice> findAllByUserIdandInstanceId(@Param("userId") String userId,
                                                         @Param("instanceId") String instanceId);

    @Modifying
    @Query(value = "DELETE FROM user_trusted_device WHERE user_id = :userId AND instance_id = :instanceId", nativeQuery = true)
    void deleteAllByUserIdandInstanceId(@Param("userId") long userId, @Param("instanceId") String instanceId);

    @Modifying
    @Query(value = "DELETE FROM user_trusted_device WHERE user_id = :userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") String userId);

}
