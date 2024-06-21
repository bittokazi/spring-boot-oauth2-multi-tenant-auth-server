package com.bittokazi.oauth2.auth.server.app.models.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bitto Kazi
 */

public class RestBadInput {

	private List<RestErrorMessage> errors = new ArrayList<RestErrorMessage>();

	public List<RestErrorMessage> getErrors() {
		return errors;
	}

	public void setErrors(List<RestErrorMessage> errors) {
		this.errors = errors;
	}

}
