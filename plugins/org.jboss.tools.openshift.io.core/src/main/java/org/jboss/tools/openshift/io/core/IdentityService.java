package org.jboss.tools.openshift.io.core;

import org.jboss.tools.openshift.io.internal.core.IdentityModel;

public class IdentityService {

	private static final IdentityService INSTANCE = new IdentityService();
	
	private IIdentityModel model;
	
	private IdentityService() {
	}
	
	public static IdentityService getDefault() {
		return INSTANCE;
	}
	
	public IIdentityModel getModel() {
		if (null == model) {
			model = new IdentityModel();
		}
		return model;
	}
	
	public IdentityStatus getStatus(IIdentity identity) {
		if (identity.getAccessToken() == null) {
			return IdentityStatus.NEEDS_LOGIN;
		}
		long lastRefreshed = identity.getLastRefreshedTime();
		long current = System.currentTimeMillis();
		if (current > identity.getAccessTokenExpiryTime()) {
			if (current > identity.getRefreshTokenExpiryTime()) {
				return IdentityStatus.NEEDS_LOGIN;
			} else {
				return IdentityStatus.NEEDS_REFRESH;
			}
		}
		if (((current - lastRefreshed) > OSIOCoreConstants.DURATION_24_HOURS) ||
		    ((current - lastRefreshed) > ((identity.getAccessTokenExpiryTime() - current) / 2))) {
		    	return IdentityStatus.NEEDS_REFRESH;
		    }
		return IdentityStatus.VALID;
	}

}
