package org.jboss.tools.openshift.osio.internal.ui.processor;

public interface RequestProcessor {
	RequestInfo getRequestInfo(String url, String content);
}
