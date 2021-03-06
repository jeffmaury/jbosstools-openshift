/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.reddeer.common.exception.RedDeerException;
import org.jboss.tools.cdk.reddeer.core.enums.CDKHypervisor;
import org.jboss.tools.cdk.reddeer.utils.CDKUtils;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;

public abstract class CDKAbstractTest {

	// Server adapter names
	public static final String SERVER_ADAPTER = "Container Development Environment";
	public static final String SERVER_ADAPTER_3 = "Container Development Environment 3";
	public static final String SERVER_ADAPTER_32 = "Container Development Environment 3.2+";
	public static final String SERVER_ADAPTER_MINISHIFT = "Minishift 1.7+";
	public static final String SERVER_ADAPTER_CRC = "CodeReady Containers 1.0";

	// General parameters
	public static final String FOLDER = CDKUtils.IS_LINUX ? "linux" : (CDKUtils.IS_WINDOWS ? "win" : "mac");
	public static final String separator = System.getProperty("file.separator");
	public static final String MINISHIFT_CONFIG_MINIMAL = " --disk-size 5GB --memory 2GB --cpus 1";
	public static final String MINISHIFT_CONFIG_DEFAULT = " --memory 4GB --cpus 2";
	public static final String MINISHIFT_CONFIG_OPTIMAL = " --memory 6GB --cpus 3";
	public static final String SKIP_REGISTRATION = "--skip-registration";
	public static final String RUNTIMES_DIRECTORY = System.getProperty("user.dir") + separator + "runtimes";

	// mock files
	protected static final String MOCK_CDK320 = getProjectAbsolutePath(
			"resources/cdk-files/" + FOLDER + "/cdk-3.2.0-mock" + (CDKUtils.IS_WINDOWS ? ".bat" : ""));
	protected static final String MOCK_CDK311 = getProjectAbsolutePath(
			"resources/cdk-files/" + FOLDER + "/cdk-3.1.1-mock" + (CDKUtils.IS_WINDOWS ? ".bat" : ""));
	protected static final String MOCK_MINISHIFT131 = getProjectAbsolutePath(
			"resources/cdk-files/" + FOLDER + "/minishift-1.3.1-mock" + (CDKUtils.IS_WINDOWS ? ".bat" : ""));
	protected static final String MOCK_MINISHIFT170 = getProjectAbsolutePath(
			"resources/cdk-files/" + FOLDER + "/minishift-1.7.0-mock" + (CDKUtils.IS_WINDOWS ? ".bat" : ""));

	// CDK Test suite parameters
	public static final String MINISHIFT_PROFILE = "minishift";
	public static final String USERNAME;
	public static final String PASSWORD;
	public static final String MINISHIFT_HYPERVISOR;
	public static final String MINISHIFT;
	public static final String CDK_MINISHIFT;
	public static final String CDK32_MINISHIFT;
	public static final String CRC_BINARY;
	public static final String DEFAULT_MINISHIFT_HOME;
	public static final String DEFAULT_CRC_HOME;
	public static final String CRC_SECRET_FILE;

	static {
		USERNAME = CDKUtils.getSystemProperty("developers.username");
		PASSWORD = CDKUtils.getSystemProperty("developers.password");
		MINISHIFT = CDKUtils.getSystemProperty("minishift");
		CDK_MINISHIFT = CDKUtils.getSystemProperty("cdk.minishift");
		CDK32_MINISHIFT = CDKUtils.getSystemProperty("cdk32.minishift");
		CRC_BINARY = CDKUtils.getSystemProperty("crc.binary");
		MINISHIFT_HYPERVISOR = CDKUtils.getSystemProperty("hypervisor");
		DEFAULT_MINISHIFT_HOME = System.getProperty("user.home") + separator + ".minishift";
		DEFAULT_CRC_HOME = System.getProperty("user.home") + separator + ".crc";
		CRC_SECRET_FILE = System.getProperty("crc.secret");
	}

	public static String assignMinishiftHypervisor() {
		String prop = CDKUtils.getSystemProperty("hypervisor");
		return prop == null || prop.isEmpty() ? CDKHypervisor.getDefaultHypervisor().toString() : prop;
	}

	public static void checkCDKParameters() {
		Map<String, String> dict = new HashMap<>();
		dict.put("CDK 3.X path", CDK_MINISHIFT);
		CDKTestUtils.checkParameterNotNull(dict);
	}

	public static void checkMinishiftParameters() {
		Map<String, String> dict = new HashMap<>();
		dict.put("Minishift path", MINISHIFT);
		CDKTestUtils.checkParameterNotNull(dict);
	}

	public static void checkCDK32Parameters() {
		Map<String, String> dict = new HashMap<>();
		dict.put("CDK 3.2+ path", CDK32_MINISHIFT);
		CDKTestUtils.checkParameterNotNull(dict);
	}
	
	public static void checkCRCParameters() {
		Map<String, String> dict = new HashMap<>();
		dict.put("CDK 3.2+ path", CRC_BINARY);
		dict.put("CDK 3.2+ path", CRC_SECRET_FILE);
		CDKTestUtils.checkParameterNotNull(dict);
	}

	public static void checkDevelopersParameters() {
		Map<String, String> dict = new HashMap<>();
		dict.put("Username", USERNAME);
		dict.put("Password", PASSWORD);
		CDKTestUtils.checkParameterNotNull(dict);
	}

	/**
	 * Provide resource absolute path in project directory
	 * 
	 * @param path - resource relative path
	 * @return resource absolute path
	 */
	public static String getProjectAbsolutePath(String... path) {

		// Construct path
		StringBuilder builder = new StringBuilder();
		for (String fragment : path) {
			builder.append("/" + fragment);
		}

		String filePath = "";
		filePath = System.getProperty("user.dir");
		File file = new File(filePath + builder.toString());
		if (!file.exists()) {
			throw new RedDeerException(
					"Resource file does not exists within project path " + filePath + builder.toString());
		}

		return file.getAbsolutePath();
	}
}
