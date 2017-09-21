/**
 * 
 */
package org.jboss.tools.openshift.osio.core;

import java.util.function.Function;
import org.eclipse.core.resources.IResource;

/**
 * @author Jeff MAURY
 *
 */
public interface ITokenProvider extends Function<IResource, String> {
	default String getToken(IResource resource) {
		return apply(resource);
	}
}
