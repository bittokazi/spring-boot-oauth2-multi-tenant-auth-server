package com.bittokazi.oauth2.auth.server.app.controllers;


import com.bittokazi.oauth2.auth.server.app.models.master.Tenant;
import com.bittokazi.oauth2.auth.server.app.services.tenant.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TenantController {

    private TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PreAuthorize("hasAuthority('SCOPE_tenant:read') and hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/tenants")
    public ResponseEntity<?> getAllTenants() {
        return tenantService.getAllTenants();
    }

    @PreAuthorize("hasAuthority('SCOPE_tenant:write') and hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("/tenants")
    public ResponseEntity<?> addTenant(@RequestBody Tenant tenant) {
        return tenantService.addTenant(tenant);
    }

    @PreAuthorize("hasAuthority('SCOPE_tenant:read') and hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/tenants/{id}")
    public ResponseEntity<?> getTenant(@PathVariable String id, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        return tenantService.getTenant(httpServletRequest, httpServletResponse, id);
    }

    @PreAuthorize("hasAuthority('SCOPE_tenant:write') and hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/tenants/{id}")
    public ResponseEntity<?> updateCompany(@RequestBody Tenant tenant, HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {
        return tenantService.updateTenant(tenant, httpServletRequest, httpServletResponse);
    }
}
