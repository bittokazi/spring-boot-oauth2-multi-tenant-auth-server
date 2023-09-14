package com.bittokazi.oauth2.auth.server.app.services.user.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/**
 * @author Bitto Kazi
 */

public class UserUpdateHelper {

	public static Map<String, List<String>> validateUser(User user, Optional<User> userOptional,
														 UserRepository userRepository) {
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		if (!userOptional.get().getEmail().equals(user.getEmail())
				&& userRepository.findOneByEmail(user.getEmail()).isPresent()) {
			errors.put("email", Arrays.asList("exist"));
		}
		return errors;
	}

	public static User setDefaultValues(User user, Optional<User> userOptional, boolean myProfile) {
		if(Objects.isNull(user.getImageAbsolutePath()) || Objects.equals(user.getImageAbsolutePath(), "")) {
			user.setImageAbsolutePath(userOptional.get().getImageAbsolutePath());
			user.setImageName(userOptional.get().getImageName());
		}
		user.setPassword(userOptional.get().getPassword());
		user.setUsername(userOptional.get().getUsername());
		user.setEmailVerified(userOptional.get().getEmailVerified());
		if (myProfile)
			user.setRoles(userOptional.get().getRoles());
		return user;
	}

	public static Map<String, List<String>> validatePassword(User user, Optional<User> userOptional,
			UserRepository userRepository, boolean fromAdmin) {
		Map<String, List<String>> errors = new HashMap<String, List<String>>();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		if (!bCryptPasswordEncoder.matches(user.getPassword(), userOptional.get().getPassword())) {
			errors.put("password", Arrays.asList("currentWrong"));
		}
		if (!fromAdmin && bCryptPasswordEncoder.matches(user.getNewPassword(), userOptional.get().getPassword())) {
			errors.put("newPassword", Arrays.asList("sameToPrevious"));
		}
		if (!fromAdmin && !user.getNewPassword().equals(user.getNewConfirmPassword())) {
			errors.put("newConfirmPassword", Arrays.asList("newDoNotMatch"));
		}
		return errors;
	}

}
