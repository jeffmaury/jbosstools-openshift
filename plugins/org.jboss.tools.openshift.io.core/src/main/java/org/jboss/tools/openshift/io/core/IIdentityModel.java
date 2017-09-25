package org.jboss.tools.openshift.io.core;

import java.util.List;

public interface IIdentityModel {

	void addIdentity(IIdentity identity);
	List<IIdentity> getIdentities();
	void removeIdentity(IIdentity identity);
	void save();
	
	void addListener(IIdentityModelListener listener);
	void removeListener(IIdentityModelListener listener);
	IIdentity createIdentity(String id);
}
