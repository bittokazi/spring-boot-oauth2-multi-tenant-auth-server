package com.bittokazi.oauth2.auth.server.app.services.base;

import com.bittokazi.oauth2.auth.server.app.models.base.RestAccessDenied;
import com.bittokazi.oauth2.auth.server.app.models.base.RestNotFound;
import com.bittokazi.oauth2.auth.server.app.models.base.RestUnprocessableEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Bitto Kazi
 */

public class RestResponseGenerator {
	public static RestNotFound notFound(HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
		RestNotFound restNotFound = new RestNotFound();
		restNotFound.setMessage("Resource Not Found");
		return restNotFound;
	}

	public static RestAccessDenied accessDenied(HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
		RestAccessDenied restAccessDenied = new RestAccessDenied();
		restAccessDenied.setMessage("403 Resource Access Denied");
		return restAccessDenied;
	}

	public static RestAccessDenied internalError(HttpServletResponse httpServletResponse) {
		httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		RestAccessDenied restAccessDenied = new RestAccessDenied();
		restAccessDenied.setMessage("500");
		return restAccessDenied;
	}

	public static RestUnprocessableEntity unprocessableEntity(HttpServletResponse httpServletResponse, String message) {
		httpServletResponse.setStatus(422);
		RestUnprocessableEntity restUnprocessableEntity = new RestUnprocessableEntity();
		restUnprocessableEntity.setMessage(message);
		return restUnprocessableEntity;
	}

	public static Map<String, List<String>> inputError(HttpServletResponse httpServletResponse,
			Map<String, List<String>> errors) {
		httpServletResponse.setContentType("application/json");
		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return errors;
	}

}
