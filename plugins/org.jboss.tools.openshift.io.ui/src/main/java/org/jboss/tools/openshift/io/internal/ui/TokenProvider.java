/**
 * 
 */
package org.jboss.tools.openshift.io.internal.ui;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.openshift.io.core.IIdentity;
import org.jboss.tools.openshift.io.core.ITokenProvider;
import org.jboss.tools.openshift.io.core.IdentityService;
import org.jboss.tools.openshift.io.core.IdentityStatus;
import org.jboss.tools.openshift.io.core.LoginResponse;
import org.jboss.tools.openshift.io.core.OSIOCoreConstants;
import org.jboss.tools.openshift.io.core.OSIOUtils;
import org.jboss.tools.openshift.io.core.RefreshResponse;
import org.jboss.tools.openshift.io.internal.ui.dialog.BrowserBasedLoginDialog;

/**
 * @author Jeff MAURY
 *
 */
public class TokenProvider implements ITokenProvider {

	private IdentityService service = IdentityService.getDefault();
	
	private HttpClient client = HttpClients.createDefault();
	
	@Override
	public String apply(IResource t) {
		String token = null;
		
		try {
			List<IIdentity> identities = service.getModel().getIdentities();
			if (identities.isEmpty()) {
				token = performLogin(null);
			} else {
				IIdentity identity = identities.get(0);
				IdentityStatus status = service.getStatus(identity);
				switch (status) {
				case VALID:
					token = identity.getAccessToken();
					break;
				case NEEDS_REFRESH:
					token = performRefresh(identity);
					break;
				case NEEDS_LOGIN:
					token = performLogin(identity);
					break;
				}
				
			}
			return token;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String performLogin(IIdentity identity) {
		String token = null;
		
		BrowserBasedLoginDialog dialog = new BrowserBasedLoginDialog(Display.getCurrent().getActiveShell(), OSIOCoreConstants.OSIO_LOGIN_URL);
		if (dialog.open() == Window.OK) {
			LoginResponse info = dialog.getInfo();
			if (null != info) {
				if (null == identity) {
					String id = OSIOUtils.decodeEmailFromToken(info.getAccessToken());
					IIdentity newIdentity = service.getModel().createIdentity(id);
					updateIdentity(info, newIdentity);
					service.getModel().addIdentity(newIdentity);
				} else {
					updateIdentity(info, identity);
				}
				token = info.getAccessToken();
			}
		}
		return token;
	}

	void updateIdentity(LoginResponse info, IIdentity identity) {
		identity.setAccessToken(info.getAccessToken());
		identity.setRefreshToken(info.getRefreshToken());
		identity.setLastRefreshedTime(System.currentTimeMillis());
		identity.setAccessTokenExpiryTime(OSIOUtils.decodeExpiryFromToken(info.getAccessToken()));
		identity.setRefreshTokenExpiryTime(OSIOUtils.decodeExpiryFromToken(info.getRefreshToken()));
		identity.save();
	}

	private String performRefresh(IIdentity identity) throws IOException {
		HttpPost post = new HttpPost(identity.getEndpointURL() + OSIOCoreConstants.REFRESH_SUFFIX);
		HttpEntity entity = new StringEntity("{\"refresh_token\":\"" + identity.getRefreshToken() + "\"}");
		post.setEntity(entity);
		try (CloseableHttpResponse httpResp = (CloseableHttpResponse) client.execute(post)) {
			int status =httpResp.getStatusLine().getStatusCode(); 
			if (HttpStatus.SC_OK == status) {
				RefreshResponse response = OSIOUtils.decodeRefreshResponse(EntityUtils.toString(httpResp.getEntity()));
				identity.setAccessToken(response.getLoginResponse().getAccessToken());
				identity.setRefreshToken(response.getLoginResponse().getRefreshToken());
				identity.setAccessTokenExpiryTime(OSIOUtils.decodeExpiryFromToken(response.getLoginResponse().getAccessToken()));
				identity.setRefreshTokenExpiryTime(OSIOUtils.decodeExpiryFromToken(response.getLoginResponse().getRefreshToken()));
				identity.setLastRefreshedTime(System.currentTimeMillis());
				identity.save();
				return identity.getAccessToken();
			} else {
				throw new IOException("HTTP error status:" + status);
			}
		}
	}
}
