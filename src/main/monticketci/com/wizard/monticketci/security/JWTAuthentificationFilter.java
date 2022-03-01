package com.wizard.monticketci.security;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizard.monticketci.entities.Administrateur;
import com.wizard.monticketci.entities.Utilisateur;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private AuthenticationManager authenticationManager;

	public JWTAuthentificationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		Utilisateur user = null;
		Administrateur admin = null;
		try {

			log.trace(" ======================================================== ");

			Enumeration<String> enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String parameterName = enumeration.nextElement();
				log.trace(" {} :  {}", parameterName, request.getParameter(parameterName));

			}
			log.trace(" ======================================================== ");

			user = new ObjectMapper().readValue(request.getInputStream(), Utilisateur.class);
			admin = new ObjectMapper().readValue(request.getInputStream(), Administrateur.class);

			if (user.getUtilisateurtelephone() != null || user.getUtilisateuremail() != null) {
				log.debug("attempting authentication,user/password,{}/{}", user.getUtilisateurtelephone(),
						user.getUtilisateurpassword());
				return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(user.getUtilisateurtelephone(), user.getUtilisateurpassword()));

			} else if (admin.getAdministrateurtelephone() != null || admin.getAdministrateuremail() != null) {

				log.debug("attempting authentication,user/password,{}/{}", admin.getAdministrateurtelephone(),
						admin.getAdministrateurpassword());
				return authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(admin.getAdministrateurtelephone(), admin.getAdministrateurpassword()));

			} else {

				log.debug("no mapper value found");

				log.error("authentication failed, return null values");

				return null;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("authentication failed, return null values");

			return null;
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		User springUser = (User) authResult.getPrincipal();
		String jwt = Jwts.builder().setSubject(springUser.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.claim("roles", springUser.getAuthorities()).compact();

		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIXE + jwt);
	}
}
