package com.bittokazi.oauth2.auth.server.app.repositories.tenant

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role?, String?> {
    fun findOneByName(name: String?): Optional<Role?>
}
