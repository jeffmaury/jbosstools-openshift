package org.jboss.tools.openshift.osio.internal.ui.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jboss.tools.openshift.osio.internal.ui.TokenProvider;

public class GetTokenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TokenProvider provider = new TokenProvider();
		provider.getToken(null);
		return null;
	}

}
