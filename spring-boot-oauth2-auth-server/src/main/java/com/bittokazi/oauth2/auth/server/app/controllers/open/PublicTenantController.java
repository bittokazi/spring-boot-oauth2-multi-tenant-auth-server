package com.bittokazi.oauth2.auth.server.app.controllers.open;

import com.bittokazi.oauth2.auth.server.app.services.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
@RequiredArgsConstructor
public class PublicTenantController {

    private final TenantService tenantService;

    @GetMapping("/tenants/info")
    public ResponseEntity<?> info() {
        return tenantService.info();
    }
}
