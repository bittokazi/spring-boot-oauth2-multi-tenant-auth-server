package com.bittokazi.oauth2.auth.server.app.services.role.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleHelpers {

    public static Object getRoles(int page, int count, RoleRepository roleRepository) {
        Map<String, Object> json = new HashMap<>();
        Pageable reqCount = PageRequest.of(page, count, Sort.by(Sort.Direction.DESC, "id"));
        Page<Role> pages = roleRepository.findAll(reqCount);
        json.put("roles", pages.getContent());
        json.put("pages", pages.getTotalPages());
        json.put("records", pages.getTotalElements());
        return json;
    }

    public static Object getAllRoles(RoleRepository roleRepository) {
        Map<String, Object> json = new HashMap<>();
        List<Role> roles = roleRepository.findAll();
        json.put("roles", roles);
        json.put("pages", 1);
        json.put("records", roles.size());
        return json;
    }
}
