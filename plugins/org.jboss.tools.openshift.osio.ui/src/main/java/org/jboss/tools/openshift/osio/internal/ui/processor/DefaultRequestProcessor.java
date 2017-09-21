package org.jboss.tools.openshift.osio.internal.ui.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.eclipsesource.json.Json;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

public class DefaultRequestProcessor implements RequestProcessor {

	private static final Charset US_ASCII = Charset.forName("US-ASCII");
	
	@Override
	public RequestInfo getRequestInfo(String url, String content) {
		RequestInfo info = null;
		try {
			URL url1 = new URL(url);
			List<NameValuePair> parameters = URLEncodedUtils.parse(url1.getQuery(), US_ASCII);
			Optional<NameValuePair> parameter = parameters.stream().filter(p -> p.getName().equals("token_json")).findFirst();
			if (parameter.isPresent()) {
				JsonObject root = parse(parameter.get().getValue());
				info = new RequestInfo() {
					
					@Override
					public long getRefreshTokenExpiresIn() {
						return root.getLong("refresh_expires_in", -1L);
					}
					
					@Override
					public String getRefreshToken() {
						return root.getString("refresh_token", null);
					}
					
					@Override
					public long getAccessTokenExpiresIn() {
						return root.getLong("expires_in", -1L);
					}
					
					@Override
					public String getAccessToken() {
						return root.getString("access_token", null);
					}
				};
			}
		} catch (MalformedURLException e) {
		} catch (ParseException e) {
		}
		return info;
	}

	private JsonObject parse(String value) {
		return (JsonObject) Json.parse(value);
	}
}
