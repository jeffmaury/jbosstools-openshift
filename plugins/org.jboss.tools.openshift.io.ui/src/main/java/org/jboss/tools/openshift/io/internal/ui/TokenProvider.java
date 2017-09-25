/**
 * 
 */
package org.jboss.tools.openshift.io.internal.ui;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.openshift.io.core.IIdentity;
import org.jboss.tools.openshift.io.core.IIdentityModel;
import org.jboss.tools.openshift.io.core.ITokenProvider;
import org.jboss.tools.openshift.io.core.IdentityService;
import org.jboss.tools.openshift.io.core.IdentityStatus;
import org.jboss.tools.openshift.io.core.LoginInfo;
import org.jboss.tools.openshift.io.core.OSIOCoreConstants;
import org.jboss.tools.openshift.io.core.OSIOUtils;
import org.jboss.tools.openshift.io.internal.ui.dialog.BrowserBasedLoginDialog;

/**
 * @author Jeff MAURY
 *
 */
public class TokenProvider implements ITokenProvider {

	private IdentityService service = IdentityService.getDefault();
	
	@Override
	public String apply(IResource t) {
		String token = null;
		
		List<IIdentity> identities = service.getModel().getIdentities();
		if (identities.isEmpty()) {
			
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
	}

	private String performLogin(IIdentity identity) {
		String token = null;
		
		BrowserBasedLoginDialog dialog = new BrowserBasedLoginDialog(Display.getCurrent().getActiveShell(), OSIOCoreConstants.OSIO_LOGIN_URL);
		if (dialog.open() == Window.OK) {
			LoginInfo info = dialog.getInfo();
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

	void updateIdentity(LoginInfo info, IIdentity identity) {
		identity.setAccessToken(info.getAccessToken());
		identity.setRefreshToken(info.getRefreshToken());
		long now = System.currentTimeMillis();
		identity.setLastRefreshedTime(now);
		identity.setAccessTokenExpiryTime(now + info.getAccessExpiresIn() * 1000);
		identity.setRefreshTokenExpiryTime(now + info.getRefreshExpiresIn() * 1000);
		identity.save();
	}

	private String performRefresh(IIdentity identity) {
		// TODO Auto-generated method stub
		return null;
	}

}
