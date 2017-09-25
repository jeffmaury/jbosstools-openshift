package org.jboss.tools.openshift.io.core;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represent the data stored in the JSON blob returned when OSIO login has been
 * completed.
 * 
 * @author Jeff MAURY
 *
 */
public class LoginInfo {

	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expires_in")
	private long accessExpiresIn;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("refresh_expires_in")
	private long refreshExpiresIn;

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the accessExpiresIn
	 */
	public long getAccessExpiresIn() {
		return accessExpiresIn;
	}

	/**
	 * @param accessExpiresIn the accessExpiresIn to set
	 */
	public void setAccessExpiresIn(long accessExpiresIn) {
		this.accessExpiresIn = accessExpiresIn;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the refreshExpiresIn
	 */
	public long getRefreshExpiresIn() {
		return refreshExpiresIn;
	}

	/**
	 * @param refreshExpiresIn the refreshExpiresIn to set
	 */
	public void setRefreshExpiresIn(long refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}
	
	@JsonAnySetter
	public void setOther(String name, Object value) {
	}
}
