package org.jboss.tools.openshift.io.internal.ui.processor;

import org.jboss.tools.openshift.io.core.LoginInfo;

public interface RequestProcessor {
	LoginInfo getRequestInfo(String url, String content);
}
