package org.jboss.tools.openshift.io.internal.ui.processor;

import org.eclipse.swt.browser.Browser;
import org.jboss.tools.openshift.io.core.LoginResponse;

public interface RequestProcessor {
	LoginResponse getRequestInfo(Browser browser, String url, String content);
}
