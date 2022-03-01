/**
 * 
 */
package com.wizard.monticketci.tools;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author augustesoro
 * @create  Jan 15, 2021
 * hashPw class
 */
@Component
public class Hasher {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * this function encrypt a user's password
	 * 
	 * @param password
	 * @return enCrypted password
	 */
	public String enCrypter(String password) {
		log.trace("encpryting value plaintext", password);

		// Hash a password for the first time
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
		log.trace("encpryted values,plaintext", password, "hashed", hashed);

		return hashed;
	}

	/**
	 * this function encrypt a user's password
	 * 
	 * @param password
	 * @return enCrypted password
	 */
	public boolean checkUnEnCrypter(String plaintext, String hashed) {

		log.trace("checking values,plaintext", plaintext, "hashed", hashed);
		if (BCrypt.checkpw(plaintext, hashed)) {

			log.trace(plaintext, hashed, "matches");
			return true;

		} else {

			log.trace(plaintext, hashed, "not matches");
			return false;

		}

	}

}
