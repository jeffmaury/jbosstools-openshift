package org.jboss.tools.openshift.io.core;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class OSIOUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Decode the login JSON as an object
	 * @param jsonString the login JSON string
	 * @return the decoded object.
	 * @throws IOException if error occurs
	 */
	public static LoginInfo decodeLoginInfo(String jsonString) throws IOException {
		return mapper.readValue(jsonString, LoginInfo.class);
		
	}
	
	public static String decodeEmailFromToken(String token) {
		String payloads[] = token.split("\\.");
		Claims claims = (Claims) Jwts.parser().parse(payloads[0] + '.' + payloads[1] + '.').getBody();
		return (String) claims.get("email");
	}

	public static String getTokenJSON(String content) {
		Document doc = Jsoup.parse(content);
		Element token_json = doc.getElementById("token_json");
		if (null != token_json) {
			return token_json.text();
		} else {
			throw new NoSuchElementException();
		}
	}
}
