package com.bittokazi.oauth2.auth.server.app.models.base;

/**
 * @author Bitto Kazi
 */

public class RestErrorMessage {
	private String identifier;
	private String message;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
