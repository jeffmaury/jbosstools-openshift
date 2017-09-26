package org.jboss.tools.openshift.io.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IdentityServiceTest {

	private IdentityService service = IdentityService.getDefault();
	
	@Test
	public void checkModelIsAvailable() {
		assertNotNull(service.getModel());
	}
	
	@Test
	public void checkLoginIsRequiredIfNoAccessToken() {
		IIdentity identity = service.getModel().createIdentity("myid");
		IdentityStatus status = service.getStatus(identity);
		assertEquals(IdentityStatus.NEEDS_LOGIN, status);
	}


	@Test
	public void checkRefreshIsRequiredIfAccessTokenExpired() {
		IIdentity identity = service.getModel().createIdentity("myid");
		identity.setAccessToken("at");
		identity.setRefreshToken("rt");
		identity.setAccessTokenExpiryTime(System.currentTimeMillis() - 1000);
		IdentityStatus status = service.getStatus(identity);
		assertEquals(IdentityStatus.NEEDS_REFRESH, status);
	}
	
	@Test
	public void checkRefreshIsRequiredIfLastAccessed2DaysAgo() {
		IIdentity identity = service.getModel().createIdentity("myid");
		identity.setAccessToken("at");
		identity.setRefreshToken("rt");
		identity.setLastRefreshedTime(System.currentTimeMillis() - 24 * 3600 * 2 * 1000);
		IdentityStatus status = service.getStatus(identity);
		assertEquals(IdentityStatus.NEEDS_REFRESH, status);
	}
	
	@Test
	public void checkValidIfAccessedRecentry() {
		IIdentity identity = service.getModel().createIdentity("myid");
		identity.setAccessToken("at");
		identity.setRefreshToken("rt");
		identity.setLastRefreshedTime(System.currentTimeMillis());
		IdentityStatus status = service.getStatus(identity);
		assertEquals(IdentityStatus.VALID, status);
	}

}
