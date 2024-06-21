package com.bittokazi.oauth2.auth.server.app.services.role.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleAddHelper{

    public static Map<String, List<String>> validateRole(Role role, RoleRepository roleRepository) {
        Map<String, List<String>> errors = new HashMap<String, List<String>>();
        if (roleRepository.findOneByName(role.getName()).isPresent()) {
            errors.put("name", Arrays.asList("exist"));
        }
        if (role.getTitle()==null || role.getTitle().length()==0) {
            errors.put("title", Arrays.asList("empty"));
        }
        return errors;
    }
}
