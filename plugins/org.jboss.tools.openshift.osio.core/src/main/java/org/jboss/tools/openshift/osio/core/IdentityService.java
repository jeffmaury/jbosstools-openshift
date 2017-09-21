package org.jboss.tools.openshift.osio.core;

import org.jboss.tools.openshift.osio.internal.core.IdentityModel;

public class IdentityService {

	public static IIdentityModel getModel() {
		return IdentityModel.getInstance();
	}
}
