package org.jboss.tools.openshift.osio.internal.ui.processor;

public interface RequestInfo {
	String getAccessToken();
	String getRefreshToken();
	long getAccessTokenExpiresIn();
	long getRefreshTokenExpiresIn();
}
