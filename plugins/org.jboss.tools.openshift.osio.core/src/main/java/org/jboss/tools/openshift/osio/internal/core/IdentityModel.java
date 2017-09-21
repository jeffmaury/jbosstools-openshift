/**
 * 
 */
package org.jboss.tools.openshift.osio.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.jboss.tools.openshift.osio.core.IIdentity;
import org.jboss.tools.openshift.osio.core.IIdentityModel;
import org.jboss.tools.openshift.osio.core.IdentityStatus;
import org.jboss.tools.openshift.osio.core.OSIOCoreConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Jeff MAURY
 *
 */
public class IdentityModel implements IIdentityModel {

	private List<IIdentity> identities = new ArrayList<>();

	private static final IIdentityModel INSTANCE = new IdentityModel();
	
	public static IIdentityModel getInstance() {
		return INSTANCE;
	}

	protected IdentityModel() {
		loadModel();
	}
	
	private void loadModel() {
		ISecurePreferences secureRoot = SecurePreferencesFactory.getDefault();
		ISecurePreferences secureIdentityRoot = secureRoot.node(OSIOCoreConstants.IDENTITY_BASE_KEY);
		IEclipsePreferences root = InstanceScope.INSTANCE.getNode(OpenShiftOSIOCoreActivator.PLUGIN_ID);
		Preferences identityRoot = root.node(OSIOCoreConstants.IDENTITY_BASE_KEY);
		try {
			String[] ids = identityRoot.childrenNames();
			for(String id: ids) {
				Identity identity = new Identity(id);
				Preferences identityNode = identityRoot.node(id);
				ISecurePreferences secureIdentityNode = secureIdentityRoot.node(id);
				try {
					identity.load(identityNode, secureIdentityNode);
					identities.add(identity);
				} catch (StorageException e) {
					OpenShiftOSIOCoreActivator.logError(e.getLocalizedMessage(), e);
				}
			}
		} catch (BackingStoreException e) {
			OpenShiftOSIOCoreActivator.logError(e.getLocalizedMessage(), e);
		}
	}
	
	@Override
	public void addIdentity(IIdentity identity) {
		identities.add(identity);
	}

	@Override
	public List<IIdentity> getIdentities() {
		return identities;
	}

	@Override
	public void removeIdentity(IIdentity identity) {
		identities.remove(identity);
	}

	@Override
	public void save() {
		identities.stream().forEach(IIdentity::save);
	}

	@Override
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
