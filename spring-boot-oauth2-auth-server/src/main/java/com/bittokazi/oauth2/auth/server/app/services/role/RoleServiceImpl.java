package com.bittokazi.oauth2.auth.server.app.services.role;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;
import com.bittokazi.oauth2.auth.server.app.services.base.RestResponseGenerator;
import com.bittokazi.oauth2.auth.server.app.services.role.helpers.RoleAddHelper;
import com.bittokazi.oauth2.auth.server.app.services.role.helpers.RoleHelpers;
import com.bittokazi.oauth2.auth.server.app.services.role.helpers.RoleUpdateHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Bitto Kazi
 */

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Object getRoles(Integer page, Integer count) {
        return RoleHelpers.getRoles(page, count, roleRepository);
    }

    @Override
    public Object addRole(Role role, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Map<String, List<String>> errors = RoleAddHelper.validateRole(role, roleRepository);
        if (errors.size() > 0) {
            return RestResponseGenerator.inputError(httpServletResponse, errors);
        }
        role = roleRepository.save(role);
        return role;
    }

    @Override
    public Object updateRole(Role modifiedRole, String id, HttpServletResponse response) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            Map<String, List<String>> errors = RoleUpdateHelper.validate(role, modifiedRole, roleRepository);
            if (errors.size() > 0) {
                return RestResponseGenerator.inputError(response, errors);
            }

            modifiedRole.setId(role.getId());
			return roleRepository.save(modifiedRole);
        }
		return RestResponseGenerator.notFound(response);
    }

    @Override
    public ResponseEntity<?> getRoleByName(Role role) {
        Optional<Role> roleOptional = roleRepository.findOneByName(role.getName());
        if (roleOptional.isPresent()) {
            return ResponseEntity.ok(roleOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> getRole(String id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return ResponseEntity.ok(roleOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public Object getAllRoles() {
        return RoleHelpers.getAllRoles(roleRepository);
    }

}
