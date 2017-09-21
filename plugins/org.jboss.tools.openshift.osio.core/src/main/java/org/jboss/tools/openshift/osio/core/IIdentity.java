package org.jboss.tools.openshift.osio.core;

/**
 * Represents an OSIO identity stored in database.
 */
public interface IIdentity {
	/**
	 * The unique identifier (email) for the identity.
	 * 
	 * @return the identity id
	 */
  String getId();
  
  /**
   * The URL used to initiate login (through a browser).
   * 
   * @return the login URL
   */
  String getLoginURL();
  
  /**
   * The URL used to refresh tokens.
   * 
   * @return the refresh URL
   */
  String getRefreshURL();
  
  /**
   * The access token used to access OSIO services.
   * 
   * @return the access token
   */
  String getAccessToken();
  
  void setAccessToken(String accessToken);
  
  /**
   * The refresh token used to renew tokens.
   * 
   * @return the refresh token
   */
  String getRefreshToken();

  void setRefreshToken(String refreshToken);
  
  /**
   * The expiry time for the access token.
   * 
   * @return the access token expiry time
   */
  long getAccessTokenExpiryTime();
  
  void setAccessTokenExpiryTime(long accessTokenExpiryTime);
  
  /**
   * The expiry time for the refresh token.
   * 
   * @return the access token refresh time
   */
  long getRefreshTokenExpiryTime();
  
  void setRefreshTokenExpiryTime(long refreshTokenExpiryTime);
  
  /**
   * The time the tokens have been refreshed.
   * 
   * @return the refresh time
   */
  long getLastRefreshedTime();
  
  void setLastRefreshedTime(long lastRefreshTime);
  
  /**
   * Save the identity in secure storage
   */
  void save();
}
