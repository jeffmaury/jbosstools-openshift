package org.jboss.tools.openshift.io.core;

public interface IIdentityModelListener {
	void identityAdded(IIdentityModel source, IIdentity identity);
	void identityRemoved(IIdentityModel source, IIdentity identity);
}
