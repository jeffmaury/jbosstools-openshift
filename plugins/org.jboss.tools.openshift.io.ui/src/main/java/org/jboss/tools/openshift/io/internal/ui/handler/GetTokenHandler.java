package org.jboss.tools.openshift.io.internal.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.openshift.io.internal.ui.TokenProvider;

public class GetTokenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TokenProvider provider = new TokenProvider();
		String token = provider.getToken(null);
		MessageBox box = new MessageBox(HandlerUtil.getActiveShell(event));
		box.setText("OpenShift.io");
		box.setMessage("Token retrieved is:" + token.substring(0, 16));
		box.open();
		return null;
	}

}
