package com.bittokazi.oauth2.auth.server.app.services.user;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.RoleRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import com.bittokazi.oauth2.auth.server.app.services.base.RestResponseGenerator;
import com.bittokazi.oauth2.auth.server.app.services.user.helpers.UserAddHelper;
import com.bittokazi.oauth2.auth.server.app.services.user.helpers.UserHelpers;
import com.bittokazi.oauth2.auth.server.app.services.user.helpers.UserUpdateHelper;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @author Bitto Kazi
 */

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	private RoleRepository roleRepository;

	private OauthClientRepository oauthClientRepository;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, OauthClientRepository oauthClientRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.oauthClientRepository = oauthClientRepository;
	}

	public Object addUser(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Boolean self, Boolean regApi) {
		Map<String, List<String>> errors = UserAddHelper.validateUser(user, userRepository, oauthClientRepository);
		if (errors.size() > 0) {
			return RestResponseGenerator.inputError(httpServletResponse, errors);
		}
		user = userRepository.save(UserAddHelper.addDefaultValues(user));
		return user;
	}

	public Object getUser(String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			return userOptional.get();
		}
		return RestResponseGenerator.notFound(httpServletResponse);
	}

	public Object getUsers(int page, int count) {
		return UserHelpers.getUsers(page, count, userRepository);
	}

	public Object updateUser(User user, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (userOptional.isPresent()) {
			Map<String, List<String>> errors = UserUpdateHelper.validateUser(user, userOptional, userRepository);
			if (errors.size() > 0) {
				return RestResponseGenerator.inputError(httpServletResponse, errors);
			}
			return UserHelpers.setUserImage(userRepository.save(UserUpdateHelper.setDefaultValues(user, userOptional, false)));
		}
		return RestResponseGenerator.notFound(httpServletResponse);
	}

	public Object updateUserPassword(User user, HttpServletResponse httpServletResponse) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (userOptional.isPresent()) {
			User userDB = userOptional.get();
			userDB.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
			return ResponseEntity.ok(userRepository.save(userDB));
		}
		return ResponseEntity.status(404).build();
	}

	public ResponseEntity<?> whoAmI(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		if(Objects.nonNull(httpServletRequest.getUserPrincipal())) {
			String host = httpServletRequest.getHeader("host").replace("www.", "");
			if(host.equals(System.getenv().get("APPLICATION_BACKEND_URL")
					.replace("http://", "")
					.replace("https://", "")
					.replace("www", ""))) TenantContext.setCurrentDataTenant("public");
			Optional<User> useOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
			if (useOptional.isPresent()) {
				User user = useOptional.get();
				user = UserHelpers.setUserImage(user);
				user.setAdminTenantUser(TenantContext.getCurrentDataTenant().equals("public"));
				return ResponseEntity.ok(user);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	public ResponseEntity<?> updateMyProfile(User user, HttpServletRequest httpServletRequest) {
		Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
		if (userOptional.isPresent()) {
			Map<String, List<String>> errors = UserUpdateHelper.validateUser(user, userOptional, userRepository);
			if (errors.size() > 0) {
				return ResponseEntity.badRequest().body(errors);
			}
			return ResponseEntity
					.ok(UserHelpers.setUserImage(userRepository.save(UserUpdateHelper.setDefaultValues(user, userOptional, true))));
		}
		return ResponseEntity.status(404).build();
	}

	public ResponseEntity<?> updateMyPassword(User user, HttpServletRequest httpServletRequest) {
		Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
		if (userOptional.isPresent()) {
			Map<String, List<String>> errors = UserUpdateHelper.validatePassword(user, userOptional,
					userRepository);
			if (errors.size() > 0) {
				return ResponseEntity.badRequest().body(errors);
			}
			User userDB = userOptional.get();
			userDB.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
			return ResponseEntity.ok(userRepository.save(userDB));
		}
		return ResponseEntity.status(404).build();
	}

	public ResponseEntity<?> updateMyPasswordFromClient(User user, HttpServletRequest httpServletRequest) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (userOptional.isPresent()) {
			Map<String, List<String>> errors = UserUpdateHelper.validatePassword(user, userOptional,
					userRepository);
			if (errors.size() > 0) {
				return ResponseEntity.badRequest().body(errors);
			}
			User userDB = userOptional.get();
			userDB.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword()));
			return ResponseEntity.ok(userRepository.save(userDB));
		}
		return ResponseEntity.status(404).build();
	}

	public ResponseEntity<?> getByUsername(User user) {
		Optional<User> userOptional = userRepository.findOneByUsername(user.getUsername());
		if(userOptional.isPresent()) {
			return ResponseEntity.ok(userOptional.get());
		}
		return ResponseEntity.status(404).build();
	}

	public ResponseEntity<?> getByEmail(User user) {
		Optional<User> userOptional = userRepository.findOneByEmail(user.getEmail());
		if(userOptional.isPresent()) {
			return ResponseEntity.ok(userOptional.get());
		}
		return ResponseEntity.status(404).build();
	}

	@Override
	public Object verifyEmailOfUser(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Optional<User> userOptional = userRepository.findById(user.getId());
		if (userOptional.isPresent()) {
			User userDb = userOptional.get();
			userDb.setEmailVerified(true);
			return userRepository.save(userDb);
		}
		return RestResponseGenerator.notFound(httpServletResponse);
	}

}
