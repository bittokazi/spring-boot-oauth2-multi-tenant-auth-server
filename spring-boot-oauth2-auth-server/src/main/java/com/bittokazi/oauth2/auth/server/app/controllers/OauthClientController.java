package com.bittokazi.oauth2.auth.server.app.controllers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.services.client.ClientService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OauthClientController {

    private ClientService oauthService;

    public OauthClientController(ClientService clientService) {
        this.oauthService = clientService;
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_client:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    public Object getAllOauthClients() {
        return oauthService.getAllOauthClients();
    }

    @PostMapping("/clients")
    @PreAuthorize("hasAuthority('SCOPE_client:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    public Object addOauthClient(@RequestBody OauthClient oauthClient, HttpServletResponse httpServletResponse) {
        return oauthService.saveOauthClient(oauthClient, httpServletResponse);
    }

    @GetMapping("/clients/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    public Object getOauthClients(@PathVariable String id, HttpServletResponse httpServletResponse) {
        return oauthService.getOauthClient(id, httpServletResponse);
    }

    @PutMapping("/clients/{id}")
    public Object updateOauthClient(@RequestBody OauthClient oauthClient, HttpServletResponse httpServletResponse) {
        return oauthService.updateOauthClient(oauthClient, httpServletResponse);
    }

    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasAuthority('SCOPE_client:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    public Object deleteOauthClient(@PathVariable String id, HttpServletResponse httpServletResponse) {
        return oauthService.deleteOauthClient(id, httpServletResponse);
    }
}
