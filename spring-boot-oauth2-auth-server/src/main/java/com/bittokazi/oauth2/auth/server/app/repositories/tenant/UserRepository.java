package com.bittokazi.oauth2.auth.server.app.repositories.tenant;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByUsernameIgnoreCase(String name);

    Optional<User> findOneByUsername(String name);

    Optional<User> findOneByImageName(String name);

}
