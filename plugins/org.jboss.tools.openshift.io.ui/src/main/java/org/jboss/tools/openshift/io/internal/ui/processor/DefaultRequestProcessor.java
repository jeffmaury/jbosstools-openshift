package org.jboss.tools.openshift.io.internal.ui.processor;

import java.io.IOException;
import java.nio.charset.Charset;
import org.jboss.tools.openshift.io.core.LoginInfo;
import org.jboss.tools.openshift.io.core.OSIOCoreConstants;
import org.jboss.tools.openshift.io.core.OSIOUtils;

public class DefaultRequestProcessor implements RequestProcessor {

	private static final Charset UTF_8 = Charset.forName("UTF-8");
	
	@Override
	public LoginInfo getRequestInfo(String url, String content) {
		LoginInfo info = null;
		try {
			if (url.startsWith(OSIOCoreConstants.DEVSTUDIO_OSIO_LANDING_PAGE)) {
				String json = OSIOUtils.getTokenJSON(content);
				return OSIOUtils.decodeLoginInfo(json);
			}
		}
		catch (IOException | RuntimeException e) {
			e.printStackTrace();
		}
		return info;
	}

}
