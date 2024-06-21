package com.bittokazi.oauth2.auth.server.app.services.user.helpers;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import com.bittokazi.oauth2.auth.server.utils.Utils;
import org.apache.coyote.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Bitto Kazi
 */

public class UserHelpers {

	public static Object getUsers(int page, int count, UserRepository userRepository) {
		Map<String, Object> json = new HashMap<>();
		Pageable reqCount = PageRequest.of(page, count, Sort.by(Direction.DESC, "id"));
		Page<User> pages = userRepository.findAll(reqCount);
		json.put("users", UserHelpers.setUsersImage(pages.getContent()));
		json.put("pages", pages.getTotalPages());
		json.put("records", pages.getTotalElements());
		return json;
	}

	public static User setUserImage(User user) {
		try {
			user.setAvatarImage("https://www.gravatar.com/avatar/" + Utils.getMD5(user.getEmail()) + "?d=identicon");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static List<User> setUsersImage(List<User> users) {
		return users.stream().map(user -> {
			return UserHelpers.setUserImage(user);
		}).collect(Collectors.toList());
	}

}
