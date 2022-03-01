package com.wizard.monticketci.security;

public class SecurityConstants {

	//24h = 86400 seconds = 86400000 milliseconds
	public static final String SECRET="visapay@wizard.com";
	 //1 days
	public static final long EXPIRATION_TIME= 8_6400_000;
	public static final String TOKEN_PREFIXE="Bearer ";
	public static final String HEADER_STRING="Authorization";
	// 2 days;
	public static final long EXPIRATION_TIME_PWDFORGET= 7_200_000; 
}
