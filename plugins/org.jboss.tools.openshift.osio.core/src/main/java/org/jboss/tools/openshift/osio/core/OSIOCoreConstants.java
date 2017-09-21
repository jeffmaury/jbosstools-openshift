package org.jboss.tools.openshift.osio.core;

import org.jboss.tools.openshift.osio.internal.core.OpenShiftOSIOCoreActivator;

public final class OSIOCoreConstants {

	public static final String OSIO_LOGIN_URL = "https://api.openshift.io/api/login/authorize?redirect=https://api.openshift.io/api/status";
	public static final String OSIO_REFRESH_URL = "https://auth.openshift.io/api/token/refresh";
	public static final String IDENTITY_BASE_KEY = OpenShiftOSIOCoreActivator.PLUGIN_ID + ".identities";
	
	public static final long DURATION_24_HOURS = 24 * 3600 * 1000L; 

}
