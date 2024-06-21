package com.bittokazi.oauth2.auth.server.app.services.user;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.services.base.BaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author Bitto Kazi
 */

public interface UserService extends BaseService {

	Object addUser(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Boolean self, Boolean regApi);

	Object updateUser(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

	Object getUsers(int page, int count);

	Object getUser(String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

	Object whoAmI(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

	Object updateUserPassword(User user, HttpServletResponse httpServletResponse);

	ResponseEntity<?> updateMyProfile(User user, HttpServletRequest httpServletRequest);

	ResponseEntity<?> updateMyPassword(User user, HttpServletRequest httpServletRequest);

	ResponseEntity<?> updateMyPasswordFromClient(User user, HttpServletRequest httpServletRequest);

	ResponseEntity<?> getByUsername(User user);

	ResponseEntity<?> getByEmail(User user);

	Object verifyEmailOfUser(User user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

}
