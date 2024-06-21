package com.bittokazi.oauth2.auth.server.app.services.role.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleUpdateHelper {

    public static Map<String, List<String>> validate(Role role, Role modifiedRole, RoleRepository roleRepository) {

        Map<String, List<String>> errors = new HashMap<String, List<String>>();
        String existingRoleName = role.getName();

        if (modifiedRole.getName() == null || modifiedRole.getName().length() == 0) {
            errors.put("name", Arrays.asList("empty"));
        }
        if (modifiedRole.getTitle() == null || modifiedRole.getTitle().length() == 0) {
            errors.put("title", Arrays.asList("empty"));
        }
        if (!modifiedRole.getName().equals(existingRoleName)) {
            if (roleRepository.findOneByName(modifiedRole.getName()).isPresent()) {
                errors.put("name", Arrays.asList("exist"));
            }
        }
        return errors;
    }

}

