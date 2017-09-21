/**
 * 
 */
package org.jboss.tools.openshift.osio.internal.ui;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.openshift.osio.core.IIdentity;
import org.jboss.tools.openshift.osio.core.IIdentityModel;
import org.jboss.tools.openshift.osio.core.ITokenProvider;
import org.jboss.tools.openshift.osio.core.IdentityService;
import org.jboss.tools.openshift.osio.core.IdentityStatus;
import org.jboss.tools.openshift.osio.core.OSIOCoreConstants;
import org.jboss.tools.openshift.osio.internal.ui.dialog.LoginDialog;

/**
 * @author Jeff MAURY
 *
 */
public class TokenProvider implements ITokenProvider {

	private IIdentityModel model = IdentityService.getModel();
	
	@Override
	public String apply(IResource t) {
		String token = null;
		
		List<IIdentity> identities = model.getIdentities();
		if (identities.isEmpty()) {
			LoginDialog dialog = new LoginDialog(Display.getCurrent().getActiveShell(), OSIOCoreConstants.OSIO_LOGIN_URL);
			dialog.open();
			
		} else {
			IIdentity identity = identities.get(0);
			IdentityStatus status = model.getStatus(identity);
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
		// TODO Auto-generated method stub
		return null;
	}

	private String performRefresh(IIdentity identity) {
		// TODO Auto-generated method stub
		return null;
	}

}
