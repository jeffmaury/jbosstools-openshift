package org.jboss.tools.openshift.io.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class OSIOUtilsTest {
	private static final String LOGIN_RESPONSE = "{\"access_token\":\"a\",\"expires_in\":1,\"refresh_token\":\"b\",\"refresh_expires_in\":2}";
	
	private static final String REFRESH_RESPONSE = "{\r\n" + 
			"  \"token\": {\r\n" + 
			"    \"access_token\": \"Beatae fuga enim suscipit sapiente vitae eligendi.\",\r\n" + 
			"    \"expires_in\": 5476746541684266001,\r\n" + 
			"    \"not-before-policy\": \"0f968573-530f-4287-a500-ddb1ac80e7eb\",\r\n" + 
			"    \"refresh_expires_in\": 5476746541684266000,\r\n" + 
			"    \"refresh_token\": \"Eum sed nobis provident aut quae occaecati.\",\r\n" + 
			"    \"token_type\": \"Consequatur quasi voluptatem non accusamus.\"\r\n" + 
			"  }\r\n" + 
			"}";
	
	private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwbEwwdlhzOVlSVnFaTW93eXc4dU5MUl95cjBpRmFvemRRazlyenEyT1ZVIn0.eyJqdGkiOiJkYTNhOGRlMS05MzNiLTRkMzQtYmRkYi0zZDM0ZTRjNGZkMWEiLCJleHAiOjE1MDg4NjQwOTksIm5iZiI6MCwiaWF0IjoxNTA2MjcyMDk5LCJpc3MiOiJodHRwczovL3Nzby5vcGVuc2hpZnQuaW8vYXV0aC9yZWFsbXMvZmFicmljOCIsImF1ZCI6ImZhYnJpYzgtb25saW5lLXBsYXRmb3JtIiwic3ViIjoiM2Y3YWExZWMtZjNiOC00NmEzLThlMjEtNzFmYzJkNDkyMDRhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZmFicmljOC1vbmxpbmUtcGxhdGZvcm0iLCJhdXRoX3RpbWUiOjE1MDYyNzIwOTgsInNlc3Npb25fc3RhdGUiOiI4MDQ4ZGJkNC00NzlmLTQ3NzktYjljMC04Y2RiNDIxNWU3Y2IiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYnJva2VyIjp7InJvbGVzIjpbInJlYWQtdG9rZW4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sImFwcHJvdmVkIjp0cnVlLCJuYW1lIjoiSmVmZiBNQVVSWSIsImNvbXBhbnkiOiJSZWRIYXQiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqbWF1cnlAcmVkaGF0LmNvbSIsImdpdmVuX25hbWUiOiJKZWZmIiwiZmFtaWx5X25hbWUiOiJNQVVSWSIsImVtYWlsIjoiam1hdXJ5QHJlZGhhdC5jb20ifQ.U-CWlIrxXUHMPlWD4HgrPJquhYcycnDbF8GP6uz0f7AqiXwATbTD_GoNFdDXIGoHruPb5rIVe5eMGdYQGN4oynbIy9OBMa26_GnrUrIYJFCURskaplP-JVocMiVN687RTPzwF7X2sot11mQSYEFGtluhfSIilFHJXNxVWLU2ET7YTwaisSJO6qtpoeJxujQdwaRyV0RjdYJgqZFDRZwJYi7glzC8CdsMJSGVr9f1Q9LbQTOciqq2ISAt39YLwoDYp26ORZ98rwDj_Jbyry9Nga4HsGNUBoFjmZP9WwvXJ2bPhFvcp5NDLmtdA1ECJo8jem9CEbdGSnzUr4nsnYeQQg";
	
	@Test
	public void checkThatAccessTokenIsReturned() throws IOException {
		LoginResponse info = OSIOUtils.decodeLoginInfo(LOGIN_RESPONSE);
		assertEquals("a", info.getAccessToken());
		
	}


	@Test
	public void checkThatRefreshTokenIsReturned() throws IOException {
		LoginResponse info = OSIOUtils.decodeLoginInfo(LOGIN_RESPONSE);
		assertEquals("b", info.getRefreshToken());
		
	}

	@Test
	public void checkEmailIsReturnedFromToken() {
		assertEquals("jmaury@redhat.com", OSIOUtils.decodeEmailFromToken(TOKEN));
	}
	
	@Test
	public void checkRefreshResponse() throws IOException {
		RefreshResponse response = OSIOUtils.decodeRefreshResponse(REFRESH_RESPONSE);
		assertNotNull(response.getLoginResponse());
		assertEquals("Beatae fuga enim suscipit sapiente vitae eligendi.", response.getLoginResponse().getAccessToken());
		assertEquals("Eum sed nobis provident aut quae occaecati.", response.getLoginResponse().getRefreshToken());
	}
}
