package com.wizard.monticketci.tools;


import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtsTools {
	
	@Log
	private static Logger log;
	
	/**
	 * Fonction pour decoder jwt
	 * @param jwt
	 * @return
	 */
	public static Claims decodeJWT(String jwt) {
    //This line will throw an exception if it is not a signed JWS (as expected)
    Claims claims = Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(SecurityConstants.SECRET))
            .parseClaimsJws(jwt).getBody();
    return claims;
}
	
	/**
	 * Fonction pour decoder jwt base Base64
	 * @param jwt
	 * @return payload
	 */
	public static String decodeManualJWT(String jwt) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String[] parts = jwt.split("\\."); //splitting header, payload and signature
		log.debug("Header: {}", new String(decoder.decode(parts[0])));
		log.debug("Payload: {}", new String(decoder.decode(parts[1])));

    return new String(decoder.decode(parts[1]));
}

}
