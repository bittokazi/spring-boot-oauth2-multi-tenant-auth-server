package com.bittokazi.oauth2.auth.server.app.controllers

import com.bittokazi.oauth2.auth.server.app.models.base.UploadObject
import com.bittokazi.oauth2.auth.server.app.models.master.Tenant
import com.bittokazi.oauth2.auth.server.app.services.tenant.TenantService
import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Hidden
@RestController
@RequestMapping("/api")
class TenantController(
    private val tenantService: TenantService
) {
    @GetMapping("/tenants")
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') and hasAuthority('SCOPE_SUPER_ADMIN')")
    fun allTenants(): ResponseEntity<*> = tenantService.allTenants()

    @PostMapping("/tenants")
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') and hasAuthority('SCOPE_SUPER_ADMIN')")
    fun addTenant(@RequestBody tenant: Tenant): ResponseEntity<*> = tenantService.addTenant(tenant!!)

    @GetMapping("/tenants/{id}")
    @PreAuthorize("hasAuthority('SCOPE_tenant:read') and hasAuthority('SCOPE_SUPER_ADMIN')")
    fun getTenant(
        @PathVariable id: String?,
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse?
    ): ResponseEntity<*> = tenantService.getTenant(httpServletRequest, httpServletResponse, id!!)

    @PutMapping("/tenants/{id}")
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') and hasAuthority('SCOPE_SUPER_ADMIN')")
    fun updateCompany(
        @RequestBody tenant: Tenant?,
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse?
    ): ResponseEntity<*> = tenantService.updateTenant(tenant!!, httpServletRequest, httpServletResponse)

    @PostMapping("/tenants/templates")
    @PreAuthorize("hasAuthority('SCOPE_tenant:write') and hasAuthority('SCOPE_SUPER_ADMIN')")
    fun addTemplate(
        @RequestPart(value = "file", required = false) file: MultipartFile,
        @RequestPart uploadObject: UploadObject
    ): ResponseEntity<*> = tenantService.addTemplate(file, uploadObject)
}
