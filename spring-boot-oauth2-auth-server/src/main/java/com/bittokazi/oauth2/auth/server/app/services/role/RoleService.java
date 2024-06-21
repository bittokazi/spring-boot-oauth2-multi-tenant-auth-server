package com.bittokazi.oauth2.auth.server.app.services.role;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.services.base.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface RoleService extends BaseService {
	public Object getRoles(Integer page, Integer count);

	public Object addRole(Role role, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

	public ResponseEntity<?> getRole(String id);

	public Object getAllRoles();

	Object updateRole(Role modifiedRole, String id, HttpServletResponse response);

	Object getRoleByName(Role role);
}
