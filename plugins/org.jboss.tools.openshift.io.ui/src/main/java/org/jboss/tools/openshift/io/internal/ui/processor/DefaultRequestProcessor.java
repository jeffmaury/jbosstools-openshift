package org.jboss.tools.openshift.io.internal.ui.processor;

import org.eclipse.swt.browser.Browser;
import org.jboss.tools.openshift.io.core.LoginResponse;
import org.jboss.tools.openshift.io.core.OSIOCoreConstants;

public class DefaultRequestProcessor implements RequestProcessor {

	@Override
	public LoginResponse getRequestInfo(Browser browser, String url, String content) {
		LoginResponse response = null;
		try {
			if (url.startsWith(OSIOCoreConstants.DEVSTUDIO_OSIO_LANDING_PAGE)) {
				String accessToken = (String) browser.evaluate("return localStorage.getItem(\"auth_token\");");
				String refreshToken = (String) browser.evaluate("return localStorage.getItem(\"refresh_token\");");
				response = new LoginResponse();
				response.setAccessToken(accessToken);
				response.setRefreshToken(refreshToken);
			} else if (url.equals("https://openshift.io/")) {
				String accessToken = (String) browser.evaluate("return localStorage.getItem(\"auth_token\");");
				String refreshToken = (String) browser.evaluate("return localStorage.getItem(\"refresh_token\");");
				response = new LoginResponse();
				response.setAccessToken(accessToken);
				response.setRefreshToken(refreshToken);
			}
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		return response;
	}

}
