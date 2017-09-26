/**
 * 
 */
package org.jboss.tools.openshift.io.core;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jeff MAURY
 *
 */
public class RefreshResponse {

	@JsonProperty("token")
	private LoginResponse loginResponse;

	/**
	 * @return the loginResponse
	 */
	public LoginResponse getLoginResponse() {
		return loginResponse;
	}


	@JsonAnySetter
	public void setOther(String name, Object value) {
	}
}
