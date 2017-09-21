package org.jboss.tools.openshift.osio.core;

import java.util.List;

public interface IIdentityModel {

	void addIdentity(IIdentity identity);
	List<IIdentity> getIdentities();
	void removeIdentity(IIdentity identity);
	void save();
	IdentityStatus getStatus(IIdentity identity);
}
