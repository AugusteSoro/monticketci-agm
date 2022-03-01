package com.wizard.monticketci.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.SignForm;
import com.wizard.monticketci.dto.UpdatePasswordDto;
import com.wizard.monticketci.dto.UtilisateurDto;
import com.wizard.monticketci.dto.UtilisateurLogedDto;
import com.wizard.monticketci.entities.Localisation;
import com.wizard.monticketci.entities.Utilisateur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.security.SecurityConstants;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.service.UtilisateurService;
import com.wizard.monticketci.tools.DateTools;
import com.wizard.monticketci.tools.JwtsTools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



/**
 * @author augustesoro
 * @create 23 fev,21
 */

@Api(value = "Gestion des utilisateurs")
@RestController
@RequestMapping("/utilisateur")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UtilisateurController {
	
	
	@Log
	private Logger log;
	
	@Autowired
	UtilisateurService utilisateurService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	LocalisationService localisationService;
	
	@Autowired
	Environment env;
	
	/**
	 * Obtenir un token
	 * @return Info
	 */
	@ApiOperation(value = "Obtenir un token", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Token generé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/token")
	public ResponseEntity<String> getToken() {

		try {
			log.debug("Obtenir un token");
			String jwt = Jwts.builder().setSubject("0708863980")
					.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
					.claim("roles", new ArrayList<>()).compact();

			String token = SecurityConstants.TOKEN_PREFIXE + jwt;
			return new ResponseEntity<>(token, HttpStatus.OK);


		} catch (Exception e) {

			log.error("erreur lors de la recuperation du token,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour connexion utilisateur
	 * @param UtilisateurLogedDto
	 */
	@ApiOperation(value = "Fonction pour connecter un utilisateur")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "connexion reussi|OK"),
			@ApiResponse(code = 204, message = "utilisateur non trouvé"),
			@ApiResponse(code = 401, message = "Mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ResponseEntity<UtilisateurLogedDto> UtilisateurLogin(@RequestBody SignForm request) {
		try {
			
			log.debug("Debut de la connexion, login: {}", request.login);
			Utilisateur utilisateur = utilisateurService.getAccountEnableByLogin(request.login) ;

			UtilisateurLogedDto utilisateurLogedDto = new UtilisateurLogedDto();
			
			if(utilisateur != null) {
				
				// Verifier si le mot de passe correspond
				if(passwordEncoder.matches(request.password, utilisateur.getUtilisateurpassword())) {
				
				
				//Authentification
				String jwt = Jwts.builder().setSubject(utilisateur.getUtilisateurtelephone())
						.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
						.claim("roles", new ArrayList<>()).compact();

				String token = SecurityConstants.TOKEN_PREFIXE + jwt;

				utilisateurLogedDto.setToken(token);
				utilisateurLogedDto.setUtilisateur(utilisateur);

				log.debug("success utilisateur found, email:{}, token:{}", utilisateurLogedDto.getUtilisateur().getUtilisateurtelephone(), utilisateurLogedDto.getToken());

				
				return new ResponseEntity<>(utilisateurLogedDto, HttpStatus.OK);
				}else {
					log.debug("Mot de passe incorrect found");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

				}
			}else {
				log.debug("utilisateur not found");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	/**
	 * Fonction pour obtenir un token
	 * @param UtilisateurLogedDto
	 */
	@ApiOperation(value = "Fonction pour obtenir un token")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Connexion reussi|OK"),
			@ApiResponse(code = 204, message = "Utilisateur non trouvé"),
			@ApiResponse(code = 401, message = "Verification token echoué"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/refreshtoken" }, method = RequestMethod.POST)
	public ResponseEntity<UtilisateurLogedDto> UtilisateurRefreshToken(@RequestBody SignForm request) {
		try {
			
			log.debug("Debut du raffraichissement de token, login: {}, token: {}", request.login, request.password);
			Utilisateur utilisateur = utilisateurService.findByUtilisateurtelephone(request.login) ;

			UtilisateurLogedDto utilisateurLogedDto = new UtilisateurLogedDto();
			
			if(utilisateur != null) {				
				
				String payload = JwtsTools.decodeManualJWT(request.password.replace("Bearer ", ""));
				log.debug("Payload: {}", payload);
				/**
				 * Cette methode ne verifie que lorsque le token est valide
				 */
				//Claims claimsJwt = JwtsTools.decodeJWT(request.password.replace("Bearer ", ""));
				//log.debug("claimsJwt: {}", claimsJwt);
				String sub = "";
				try {

					JSONObject payloadJson = new JSONObject(payload);
					sub = (String) payloadJson.get("sub");

				}catch (Exception e) {
					log.debug("Une erreur est survenue: {}", e.getMessage());
				}

				log.debug("SUB: {}", sub);
				if(request.login.equals(sub)) {
					log.debug("Token valide pour ce utilisateur");
					//Authentification
					String jwt = Jwts.builder().setSubject(utilisateur.getUtilisateurtelephone())
							.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
							.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
							.claim("roles", new ArrayList<>()).compact();

					String token = SecurityConstants.TOKEN_PREFIXE + jwt;

					utilisateurLogedDto.setToken(token);
					utilisateurLogedDto.setUtilisateur(utilisateur);

					log.debug("success utilisateur found, email:{}, token:{}", utilisateurLogedDto.getUtilisateur().getUtilisateurtelephone(), utilisateurLogedDto.getToken());
					return new ResponseEntity<>(utilisateurLogedDto, HttpStatus.OK);

				}else {
					log.debug("Token non valide pour ce utilisateur");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

				}
				

			}else {
				log.debug("utilisateur not found");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	
	/**
	 * Fonction pour enregistrer et logguer un utilisateur
	 * @return UtilisateurLogedDto
	 */
	@ApiOperation(value = "Enregistrer et logguer un utilisateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utilisateur Existe deja|OK"),
			@ApiResponse(code = 201, message = "Utilisateur crée|CREATED"),
			@ApiResponse(code = 401, message = "Localisation introuvable"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<UtilisateurLogedDto> postEtLoginUtilisateurs(@RequestBody UtilisateurDto entity) {

		try {
			log.debug("Debut de la creation de l'utilisateur: {}", entity.toString());
			
			log.debug("Recherche de la localisation par id: {}", entity.getLocalisationid());
			Localisation localisation = localisationService.findOne(entity.getLocalisationid());
			if(localisation == null) {
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			UtilisateurLogedDto utilisateurLogedDto = new UtilisateurLogedDto();

			Utilisateur userInDb = utilisateurService.findByUtilisateurtelephone(entity.getUtilisateurtelephone());
			
			
			if(userInDb != null) {
				log.debug("Utilisateur trouvé dans la BD");
				
				//Authentification
				String jwt = Jwts.builder().setSubject(userInDb.getUtilisateurtelephone())
						.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
						.claim("roles", new ArrayList<>()).compact();

				String token = SecurityConstants.TOKEN_PREFIXE + jwt;

				utilisateurLogedDto.setToken(token);
				utilisateurLogedDto.setUtilisateur(userInDb);

				log.debug("success utilisateur found, email:{}, token:{}", utilisateurLogedDto.getUtilisateur().getUtilisateurtelephone(), utilisateurLogedDto.getToken());
										
				return new ResponseEntity<>(utilisateurLogedDto, HttpStatus.OK);
			}
			Utilisateur utilisateur = new Utilisateur();
				
			utilisateur.setLocalisation(localisation);
			utilisateur.setUtilisateurdatecreation(dateTools.getCurrentTimeStamp());
			utilisateur.setUtilisateuremail(entity.getUtilisateuremail());
			utilisateur.setUtilisateurenable(true);
			utilisateur.setUtilisateurisdeleted(false);
			utilisateur.setUtilisateurnom(entity.getUtilisateurnom());
			//utilisateur.setUtilisateurpassword(passwordEncoder.encode(entity.getUtilisateurpassword()));
			utilisateur.setUtilisateurprenom(entity.getUtilisateurprenom());
			utilisateur.setUtilisateursexe(entity.getUtilisateursexe());
			utilisateur.setUtilisateurstatut(env.getProperty("statut.actif"));
			utilisateur.setUtilisateurtelephone(entity.getUtilisateurtelephone());
			utilisateur.setUtilisateurcode(env.getProperty("prefix.utilisateur") + dateTools.generateTransactionIdonTime());
			/*utilisateur.setUtilisateursoldeinitial(0);
			utilisateur.setUtilisateursoldeavant(0);
			utilisateur.setUtilisateursoldeactuel(0);*/
			
			utilisateur = utilisateurService.create(utilisateur);
			log.debug("Utilisateur crée avec succes");
			
			//Authentification
			String jwt = Jwts.builder().setSubject(utilisateur.getUtilisateurtelephone())
					.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
					.claim("roles", new ArrayList<>()).compact();

			String token = SecurityConstants.TOKEN_PREFIXE + jwt;

			utilisateurLogedDto.setToken(token);
			utilisateurLogedDto.setUtilisateur(utilisateur);

			log.debug("success utilisateur found, email:{}, token:{}", utilisateurLogedDto.getUtilisateur().getUtilisateurtelephone(), utilisateurLogedDto.getToken());

			return new ResponseEntity<>(utilisateurLogedDto, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'utilisateur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un utilisateur
	 * @return Utilisateur
	 */
	@ApiOperation(value = "Mettre à jour un utilisateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Utilisateur updated|OK"),
			@ApiResponse(code = 401, message = "Utilisateur non trouvé"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Utilisateur> updateUtilisateurs(@RequestBody UtilisateurDto entity) {

		try {
			log.debug("Debut de la mise à jour de l'utilisateur: {}", entity.toString());
			Utilisateur utilisateur = utilisateurService.findOne(entity.getUtilisateurid());
			if(utilisateur == null) {
				log.debug("Utilisateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Utilisateur user = utilisateurService.findByUtilisateurtelephone(entity.getUtilisateurtelephone());
			if(user != null && user.getUtilisateurtelephone() != utilisateur.getUtilisateurtelephone() ) {
				log.debug("Email existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			utilisateur.setUtilisateuremail(entity.getUtilisateuremail());
			utilisateur.setUtilisateurnom(entity.getUtilisateurnom());
			utilisateur.setUtilisateurprenom(entity.getUtilisateurprenom());
			//utilisateur.setUtilisateurtelephone(entity.getUtilisateurtelephone());
			
			utilisateur = utilisateurService.update(utilisateur);
			log.debug("Utilisateur mis à jour avec succes");

			return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'utilisateur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour mot de passe Utilisateur
	 * @param UpdatePasswordDto
	 */
	@ApiOperation(value = "Mettre à jour mot de passe Utilisateur")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Mot de passe reset|OK"),
			@ApiResponse(code = 204, message = "Utilisateur non trouvé"),
			@ApiResponse(code = 401, message = "Ancien mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/updatepassword" }, method = RequestMethod.PUT)
	public ResponseEntity<Utilisateur> UtilisateurUpdatePassword(@RequestBody UpdatePasswordDto request) {
		try {
			
			log.debug("Debut de la reinitialisation de mot de passe, Objet: {}", request);

			Utilisateur utilisateur = utilisateurService.findByUtilisateuremail(request.getEmail());

			if (utilisateur != null && passwordEncoder.matches(request.getAncienPassword(), utilisateur.getUtilisateurpassword())) { // Si ancien mot de passe correspond

				utilisateur.setUtilisateurpassword(passwordEncoder.encode(request.getNewPassword()));
				utilisateur = utilisateurService.update(utilisateur);
				
				log.debug("Mot de passe mis à jour");
				return new ResponseEntity<>(utilisateur, HttpStatus.CREATED);


			} else {
				log.debug("Utilisateur inexistant, {} ", request);

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * Fonction pour lister les utilisateurs
	 * @return Utilisateur
	 */
	@ApiOperation(value = "Lister les utilisateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utilisateur trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun utilisateur trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Utilisateur>> getUtilisateurs() {

		try {
			log.debug("Afficher les utilisateurs");
			List<Utilisateur> lsUtilisateur = utilisateurService.findAll();

			if(lsUtilisateur.isEmpty()) {
				log.debug("Utilisateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Utilisateur trouvé, nombre: {}", lsUtilisateur.size());
				return new ResponseEntity<>(lsUtilisateur, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des utilisateurs,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	

}
