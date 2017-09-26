package org.jboss.tools.openshift.io.core;

import org.jboss.tools.openshift.io.internal.core.OpenShiftOSIOCoreActivator;

public final class OSIOCoreConstants {

	public static final String DEVSTUDIO_OSIO_LANDING_PAGE = "https://openshift.io/_home";
	
	public static final String OSIO_ENDPOINT = "https://auth.openshift.io/api/";
	
	public static final String LOGIN_SUFFIX = "login?redirect=";
	
	public static final String REFRESH_SUFFIX = "token/refresh";
	
	public static final String OSIO_LOGIN_URL = OSIO_ENDPOINT + LOGIN_SUFFIX + DEVSTUDIO_OSIO_LANDING_PAGE;
	
	public static final String OSIO_REFRESH_URL = OSIO_ENDPOINT + REFRESH_SUFFIX;
	
	public static final String IDENTITY_BASE_KEY = OpenShiftOSIOCoreActivator.PLUGIN_ID + ".identities";
	
	public static final long DURATION_24_HOURS = 24 * 3600 * 1000L; 

}
