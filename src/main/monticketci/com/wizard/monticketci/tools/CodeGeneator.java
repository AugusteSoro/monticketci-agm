/**
 * 
 */
package com.wizard.monticketci.tools;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * @author ars
 * created on Jul 30, 2019
 */

@Component
public class CodeGeneator {

	
	public String generateRegisterCode() {
		
		
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1485) + 1;
		return randomInt + "" ;
				
	}
	
	/**
	 * Generer code secret carte à 4 chiffres
	 * @return String
	 */
	public String generateSecretCode() {
		
		int min = 1000;
		int max = 9999;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(max - min + 1) + min;
		return randomInt + "" ;
				
	}
	
	
	
	/**
	 * Generer code secret carte à 6 chiffres
	 * @return String
	 */
	public String generateOtpCarte() {
		
		int min = 100000;
		int max = 999999;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(max - min + 1) + min;
		return randomInt + "" ;
				
	}
	
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public String randomString(int len){
	   StringBuilder sb = new StringBuilder(len);
	   for(int i = 0; i < len; i++)
	      sb.append(AB.charAt(rnd.nextInt(AB.length())));
	   return sb.toString();
	}
}
