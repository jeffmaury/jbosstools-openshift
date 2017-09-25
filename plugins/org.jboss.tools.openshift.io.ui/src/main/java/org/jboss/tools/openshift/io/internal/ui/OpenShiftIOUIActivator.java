/**
 * 
 */
package org.jboss.tools.openshift.io.internal.ui;

import org.jboss.tools.foundation.core.plugin.BaseCorePlugin;

/**
 * @author Jeff MAURY
 *
 */
public class OpenShiftIOUIActivator extends BaseCorePlugin {

	private static OpenShiftIOUIActivator INSTANCE = null;
	
	public static final String PLUGIN_ID = "org.jboss.tools.openshift.io.ui";
	
	public OpenShiftIOUIActivator() {
		super();
		INSTANCE = this;
	}

	public static OpenShiftIOUIActivator getDefault() {
		return INSTANCE;
	}
	
	public static void logError(String message, Throwable t) {
		getDefault().pluginLogInternal().logError(message, t);
	}

}
