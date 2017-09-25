/**
 * 
 */
package org.jboss.tools.openshift.io.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.jboss.tools.openshift.io.core.IIdentity;
import org.jboss.tools.openshift.io.core.IIdentityModel;
import org.jboss.tools.openshift.io.core.IIdentityModelListener;
import org.jboss.tools.openshift.io.core.OSIOCoreConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Jeff MAURY
 *
 */
public class IdentityModel implements IIdentityModel {

	private List<IIdentity> identities = new ArrayList<>();
	
	private List<String> removed = new ArrayList<>();
	
	private List<IIdentityModelListener> listeners = new ArrayList<>();

	private enum Event {
		IDENTITY_ADDED,
		IDENTITY_REMOVED
	}
	
	public IdentityModel() {
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
	
	private void fireEvent(Event event, IIdentity identity) {
		listeners.stream().forEach(listener -> {
			switch (event) {
			case IDENTITY_ADDED:
				listener.identityAdded(this, identity);
				break;
			case IDENTITY_REMOVED:
				listener.identityRemoved(this,  identity);
				break;
			}
		});
	}
	
	
	@Override
	public IIdentity createIdentity(String id) {
		return new Identity(id);
	}

	@Override
	public void addIdentity(IIdentity identity) {
		identities.add(identity);
		fireEvent(Event.IDENTITY_ADDED, identity);
	}

	@Override
	public List<IIdentity> getIdentities() {
		return identities;
	}

	@Override
	public void removeIdentity(IIdentity identity) {
		identities.remove(identity);
		fireEvent(Event.IDENTITY_REMOVED, identity);
		removed.add(identity.getId());
	}

	@Override
	public void save() {
		identities.stream().forEach(IIdentity::save);
		Preferences identityRoot = Identity.getIdentityPreferences();
		ISecurePreferences identitySecureRoot = Identity.getSecureIdentityPreferences();
		removed.stream().forEach(id -> {
			try {
				identityRoot.node(id).removeNode();
				identitySecureRoot.node(id).removeNode();
			} catch (BackingStoreException e) {
				OpenShiftOSIOCoreActivator.logError(e.getLocalizedMessage(), e);
			}
		});
		removed.clear();
	}

	@Override
	public void addListener(IIdentityModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IIdentityModelListener listener) {
		listeners.remove(listener);
	}


}
