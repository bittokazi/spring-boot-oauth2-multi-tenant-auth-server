package com.bittokazi.oauth2.auth.server.app.controllers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.TwoFASecretPayload;
import com.bittokazi.oauth2.auth.server.app.services.mfa.TwoFaService;
import com.bittokazi.oauth2.auth.server.app.services.user.UserService;
import com.bittokazi.oauth2.auth.server.app.services.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Bitto Kazi
 */

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    private TwoFaService twoFaService;

    public UserController(UserServiceImpl userService, TwoFaService twoFaService) {
        this.userService = userService;
        this.twoFaService = twoFaService;
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/users")
    public Object getUsers(int page, int count) {
        return userService.getUsers(page, count);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("/users")
    public Object addUser(@RequestBody User user, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        return userService.addUser(user, httpServletRequest, httpServletResponse, false, false);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/users/{id}")
    public Object getUser(@PathVariable String id, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        return userService.getUser(id, httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/users/{id}")
    public Object updateUser(@RequestBody User user, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        return userService.updateUser(user, httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/users/{id}/update/password")
    public Object updateUserPassword(@RequestBody User user, HttpServletResponse httpServletResponse) {
        return userService.updateUserPassword(user, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_profile') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @GetMapping("/users/whoami")
    public Object whoAmI(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return userService.whoAmI(httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("/users/search/username")
    public Object getByUsername(@RequestBody User user) {
        return userService.getByUsername(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PostMapping("/users/search/email")
    public Object getByEmail(@RequestBody User user) {
        return userService.getByEmail(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @PutMapping("/users/whoami")
    public Object updateMyAccount(@RequestBody User user, HttpServletRequest httpServletRequest) {
        return userService.updateMyProfile(user, httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @PutMapping("/users/whoami/password")
    public Object updateMyPassword(@RequestBody User user, HttpServletRequest httpServletRequest) {
        return userService.updateMyPassword(user, httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all')")
    @PutMapping("/users/whoami/password/client")
    public Object updateMyPasswordFromClient(@RequestBody User user, HttpServletRequest httpServletRequest) {
        return userService.updateMyPasswordFromClient(user, httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @GetMapping("/users/whoami/mfa/otp/generate-secret")
    public Object userGenerateOtpSecret(HttpServletRequest httpServletRequest) {
        return twoFaService.generateSecret(httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @PostMapping("/users/whoami/mfa/otp/enable")
    public Object userEnableOtpSecret(@RequestBody TwoFASecretPayload twoFASecretPayload,
                                      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return twoFaService.enable2FA(twoFASecretPayload, httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @GetMapping("/users/whoami/mfa/otp/disable")
    public Object disable2FA(HttpServletRequest httpServletRequest) {
        return twoFaService.disable2FA(httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @GetMapping("/users/whoami/mfa/trusted-devices")
    public Object user2FaTrustedDeviceList(HttpServletRequest httpServletRequest) {
        return twoFaService.selfGetAllTrustedDeviceOfUser(httpServletRequest);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @DeleteMapping("/users/whoami/mfa/trusted-devices/{id}")
    public Object user2FaTrustedDeviceDeleteByID(@PathVariable long id, HttpServletRequest httpServletRequest,
                                                 HttpServletResponse httpServletResponse) {
        return twoFaService.selfDeleteTrustedDeviceById(id, httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_profile')")
    @GetMapping("/users/whoami/mfa/generate-scratch-codes")
    public Object regenerateScratchCode(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) {
        return twoFaService.regenerateScratchCode(httpServletRequest, httpServletResponse);
    }

    @PreAuthorize("hasAuthority('SCOPE_user:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
    @PutMapping("/users/{id}/verify/email")
    public Object verifyEmailOfUser(@RequestBody User user, HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) {
        return userService.verifyEmailOfUser(user, httpServletRequest, httpServletResponse);
    }

}

