package com.bittokazi.oauth2.auth.server.app.services.user.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * @author Bitto Kazi
 */

public class UserAddHelper {

	public static Map<String, List<String>> validateUser(User user, UserRepository userRepository, OauthClientRepository oauthClientRepository) {
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		if (oauthClientRepository.findOneByClientId(user.getUsername()).isPresent()) {
			errors.put("username", Arrays.asList("notAllowed"));
		}
		if (userRepository.findOneByUsername(user.getUsername()).isPresent()) {
			errors.put("username", Arrays.asList("exist"));
		}
		if (userRepository.findOneByEmail(user.getEmail()).isPresent()) {
			errors.put("email", Arrays.asList("exist"));
		}
		if (user.getRoles().size() < 0) {
			errors.put("role", Arrays.asList("empty"));
		}
		return errors;
	}

	public static User addDefaultValues(User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
//		user.setEmailVerified(false);
		user.setEnabled(true);
		user.setTwoFaEnabled(false);
		return user;
	}
}
