/**
 * 
 */
package org.jboss.tools.openshift.osio.internal.core;

import java.io.IOException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.jboss.tools.openshift.osio.core.IIdentity;
import org.jboss.tools.openshift.osio.core.OSIOCoreConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * @author Jeff MAURY
 *
 */
public class Identity implements IIdentity {
	
	private static final String LOGIN_URL_KEY = "loginURL";

	private static final String REFRESH_URL_KEY = "refreshURL";

	private static final String ACCESS_TOKEN_EXPIRY_TIME_KEY = "accessTokenExpiryTime";

	private static final String _KEYREFRESH_TOKEN_EXPIRY_TIME = "refreshTokenExpiryTime";

	private static final String LAST_REFRESHED_TIME_KEY = "lastRefreshTime";

	private static final String ACCESS_TOKEN_KEY = "accessToken";

	private static final String REFRESH_TOKEN_KEY = "refreshToken";

	private String id;
	
	private String loginURL = OSIOCoreConstants.OSIO_LOGIN_URL;
	
	private String refreshURL = OSIOCoreConstants.OSIO_REFRESH_URL;
	
	private String accessToken;
	
	private String refreshToken;
	
	private long accessTokenExpiryTime;
	
	private long refreshTokenExpiryTime;
	
	private long lastRefreshedTime;

	public Identity(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getLoginURL() {
		return loginURL;
	}

	@Override
	public String getRefreshURL() {
		return refreshURL;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}

	@Override
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public long getAccessTokenExpiryTime() {
		return accessTokenExpiryTime;
	}

	@Override
	public void setAccessTokenExpiryTime(long accessTokenExpiryTime) {
		this.accessTokenExpiryTime = accessTokenExpiryTime;
	}

	@Override
	public long getRefreshTokenExpiryTime() {
		return refreshTokenExpiryTime;
	}

	@Override
	public void setRefreshTokenExpiryTime(long refreshTokenExpiryTime) {
		this.refreshTokenExpiryTime = refreshTokenExpiryTime;
	}

	@Override
	public long getLastRefreshedTime() {
		return lastRefreshedTime;
	}

	@Override
	public void setLastRefreshedTime(long lastRefreshTime) {
		this.lastRefreshedTime = lastRefreshTime;
	}

	@Override
	public void save() {
		try {
			ISecurePreferences secureRoot = SecurePreferencesFactory.getDefault();
			ISecurePreferences secureIdentityRoot = secureRoot.node(OSIOCoreConstants.IDENTITY_BASE_KEY);
			IEclipsePreferences root = InstanceScope.INSTANCE.getNode(OpenShiftOSIOCoreActivator.PLUGIN_ID);
			Preferences identityRoot = root.node(OSIOCoreConstants.IDENTITY_BASE_KEY);
			ISecurePreferences secureIdentityNode = secureIdentityRoot.node(getId());
			Preferences identityNode = identityRoot.node(getId());
			
			identityNode.put(LOGIN_URL_KEY, getLoginURL());
			identityNode.put(REFRESH_URL_KEY, getRefreshURL());
			identityNode.putLong(ACCESS_TOKEN_EXPIRY_TIME_KEY, getAccessTokenExpiryTime());
			identityNode.putLong(_KEYREFRESH_TOKEN_EXPIRY_TIME, getRefreshTokenExpiryTime());
			identityNode.putLong(LAST_REFRESHED_TIME_KEY, getLastRefreshedTime());
			
			secureIdentityNode.put(ACCESS_TOKEN_KEY, getAccessToken(), true);
			secureIdentityNode.put(REFRESH_TOKEN_KEY, getRefreshToken(), true);
			
			identityRoot.flush();
			secureIdentityRoot.flush();
		} catch(StorageException se) {
			OpenShiftOSIOCoreActivator.logError("Error saving credentials in secure storage", se);
		} catch(IOException ioe) {
			OpenShiftOSIOCoreActivator.logError("Error saving credentials in secure storage", ioe);
		} catch(BackingStoreException bse) {
			OpenShiftOSIOCoreActivator.logError("Error saving credentials in secure storage", bse);
		}
	}

	public void load(Preferences identityNode, ISecurePreferences secureIdentityNode) throws StorageException {
		loginURL = identityNode.get(LOGIN_URL_KEY, OSIOCoreConstants.OSIO_LOGIN_URL);
		refreshURL = identityNode.get(REFRESH_URL_KEY, OSIOCoreConstants.OSIO_REFRESH_URL);
		accessTokenExpiryTime = identityNode.getLong(ACCESS_TOKEN_EXPIRY_TIME_KEY, -1L);
		refreshTokenExpiryTime = identityNode.getLong(_KEYREFRESH_TOKEN_EXPIRY_TIME, -1);
		lastRefreshedTime = identityNode.getLong(LAST_REFRESHED_TIME_KEY, -1);
		accessToken = secureIdentityNode.get(ACCESS_TOKEN_KEY, null);
		refreshToken = secureIdentityNode.get(REFRESH_TOKEN_KEY, null);
	}

}
