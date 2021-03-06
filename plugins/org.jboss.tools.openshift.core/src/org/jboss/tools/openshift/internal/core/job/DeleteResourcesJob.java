/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.internal.core.job;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.openshift.core.connection.ConnectionsRegistryUtil;
import org.jboss.tools.openshift.internal.common.core.OpenShiftCommonCoreActivator;

import com.openshift.restclient.NotFoundException;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IResource;

/**
 * Deletes the given list of resources.
 * 
 * @author Andre Dietisheim
 */
public class DeleteResourcesJob extends AbstractDeleteResourceJob {

	private List<IResource> resources;

	public DeleteResourcesJob(final List<IResource> resources) {
		super("Delete Resource Job");
		this.resources = resources;
	}

	@Override
	protected IStatus doRun(final IProgressMonitor monitor) {
		if (CollectionUtils.isEmpty(resources)) {
			return Status.CANCEL_STATUS;
		}

		List<IStatus> status = resources.stream()
				.sorted(new ResourcesRemovalOrdering())
				.map(resource -> {
						return delete(resource, ConnectionsRegistryUtil.getConnectionFor(resource), monitor);
					})
				.filter(s -> s.getSeverity() == IStatus.ERROR)
				// dont report not found exceptions
				.filter(s -> !(s.getException() instanceof NotFoundException))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(status)) {
			return Status.OK_STATUS;
		}
		return new MultiStatus(OpenShiftCommonCoreActivator.PLUGIN_ID, 0, status.toArray(new IStatus[status.size()]),
				"Could not remove certain resources", null);
	}

	/**
	 * for testing purposes
	 */
	public static class ResourcesRemovalOrdering implements Comparator<IResource> {
		
		private enum ResourceKindWeight {
			DC(ResourceKind.DEPLOYMENT_CONFIG, 1), 
			RC(ResourceKind.REPLICATION_CONTROLLER, 2),
			POD(ResourceKind.POD, 3), 
			DEFAULT("", 4);

			private String resourceKind;
			private int weight;

			ResourceKindWeight(String resourceKind, int weight) {
				this.resourceKind = resourceKind;
				this.weight = weight;
			}
		}

		private static final int HIGHER_PRIO = -1;
		private static final int LOWER_PRIO = 1;
		private static final int EQUAL_PRIO = 0;

		@Override
		public int compare(IResource resource1, IResource resource2) {
			if (resource1 == null) {
				if (resource2 == null) {
					return EQUAL_PRIO;
				} else {
					return LOWER_PRIO;
				}
			} else {
				if (resource2 == null) {
					return HIGHER_PRIO;
				}
			}
			// now null-safe

			return getWeight(resource1) - getWeight(resource2);
		}

		private int getWeight(IResource resource) {
			for (ResourceKindWeight resourceKindWeight : ResourceKindWeight.values()) {
				if (resourceKindWeight.resourceKind.equals(resource.getKind())) {
					return resourceKindWeight.weight;
				}
			}

			return ResourceKindWeight.DEFAULT.weight;
		}
	}

}
