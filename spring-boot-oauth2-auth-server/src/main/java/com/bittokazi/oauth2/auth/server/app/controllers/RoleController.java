package com.bittokazi.oauth2.auth.server.app.controllers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.services.role.RoleService;
import com.bittokazi.oauth2.auth.server.app.services.role.RoleServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author Bitto Kazi
 */

@RestController
@RequestMapping("/api")
public class RoleController {

	private RoleService roleService;

	public RoleController(RoleServiceImpl roleServiceImpl) {
		this.roleService = roleServiceImpl;
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@GetMapping("/roles")
	public Object getRoles(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
						   @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
		return roleService.getRoles(page, count);
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@GetMapping("/roles/all")
	public Object getAllRoles() {
		return roleService.getAllRoles();
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@PostMapping("/roles")
	public Object addRole(@RequestBody Role role, HttpServletRequest httpServletRequest,
						  HttpServletResponse httpServletResponse) {

		return roleService.addRole(role, httpServletRequest, httpServletResponse);
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@GetMapping("/roles/{id}")
	public Object getRole(@PathVariable String id) {
		return roleService.getRole(id);
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@PutMapping("/roles/{id}")
	public Object updateRole(@PathVariable String id, @RequestBody Role role,
									HttpServletResponse response) {
		return roleService.updateRole(role, id, response);
	}

	@PreAuthorize("hasAuthority('SCOPE_role:all') or hasAuthority('SCOPE_SUPER_ADMIN')")
	@PostMapping("/roles/search/name")
	public Object getRoleByName(@RequestBody Role role, HttpServletRequest httpServletRequest,
						  HttpServletResponse httpServletResponse) {
		return roleService.getRoleByName(role);
	}
}
