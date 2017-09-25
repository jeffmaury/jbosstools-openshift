/**
 * 
 */
package org.jboss.tools.openshift.io.internal.core;

import org.jboss.tools.foundation.core.plugin.BaseCorePlugin;

/**
 * @author Jeff MAURY
 *
 */
public class OpenShiftOSIOCoreActivator extends BaseCorePlugin {

	private static OpenShiftOSIOCoreActivator INSTANCE = null;
	
	public static final String PLUGIN_ID = "org.jboss.tools.openshift.io.core";
	
	public OpenShiftOSIOCoreActivator() {
		super();
		INSTANCE = this;
	}

	public static OpenShiftOSIOCoreActivator getDefault() {
		return INSTANCE;
	}
	
	public static void logError(String message, Throwable t) {
		getDefault().pluginLogInternal().logError(message, t);
	}

}
