package com.wizard.monticketci.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.wizard.monticketci.dto.ActiveAdminDto;
import com.wizard.monticketci.dto.AdministrateurDto;
import com.wizard.monticketci.dto.AdministrateurLogedDto;
import com.wizard.monticketci.dto.SignForm;
import com.wizard.monticketci.dto.UpdatePasswordDto;
import com.wizard.monticketci.entities.Administrateur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.security.SecurityConstants;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.tools.DateTools;

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

@Api(value = "Gestion des administrateurs")
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdministrateurController {
	
	@Log
	private Logger log;
	
	@Autowired
	AdminService administrateurService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	Environment env;

	
	/**
	 * Fonction pour connexion administrateur
	 * @param caissier
	 */
	@ApiOperation(value = "Fonction pour connecter un administrateur")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "connexion reussi|OK"),
			@ApiResponse(code = 204, message = "administrateur non trouvé"),
			@ApiResponse(code = 401, message = "Mot de passe incorrect"),
			@ApiResponse(code = 406, message = "Compte inactif"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ResponseEntity<AdministrateurLogedDto> AdministrateurLogin(@RequestBody SignForm request) {
		try {
			
			log.debug("Debut de la connexion, login: {}", request.login);
			Administrateur administrateur = administrateurService.getAccountEnableByLogin(request.login) ;

			AdministrateurLogedDto administrateurLogedDto = new AdministrateurLogedDto();
			
			if(administrateur != null) {
				
				if(!administrateur.getAdministrateurstatut().equals(env.getProperty("statut.actif"))) {
					log.debug("Compte inactif");
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				}
				
				// Verifier si le mot de passe correspond
				if(passwordEncoder.matches(request.password, administrateur.getAdministrateurpassword())) {
				
				
				//Authentification
				String jwt = Jwts.builder().setSubject(administrateur.getAdministrateuremail())
						.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
						.claim("roles", new ArrayList<>()).compact();

				String token = SecurityConstants.TOKEN_PREFIXE + jwt;

				administrateurLogedDto.setToken(token);
				administrateurLogedDto.setAdministrateur(administrateur);

				log.debug("success administrateur found, email:{}, token:{}", administrateurLogedDto.getAdministrateur().getAdministrateuremail(), administrateurLogedDto.getToken());

				
				return new ResponseEntity<>(administrateurLogedDto, HttpStatus.OK);
				}else {
					log.debug("Mot de passe incorrect found");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

				}
			}else {
				log.debug("administrateur not found");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	
	/**
	 * Fonction pour enregistrer un administrateur
	 * @return Administrateur
	 */
	@ApiOperation(value = "Enregistrer un administrateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Administrateur crée|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Administrateur> postAdministrateurs(@RequestBody AdministrateurDto entity) {

		try {
			log.debug("Debut de la creation de l'administrateur: {}", entity.toString());
			Administrateur administrateur = new Administrateur();
				
			administrateur.setAdministrateurdatecreation(dateTools.getCurrentTimeStamp());
			administrateur.setAdministrateuremail(entity.getAdministrateuremail());
			administrateur.setAdministrateurenable(true);
			administrateur.setAdministrateurisdeleted(false);
			administrateur.setAdministrateurnom(entity.getAdministrateurnom());
			administrateur.setAdministrateurpassword(passwordEncoder.encode(entity.getAdministrateurpassword()));
			administrateur.setAdministrateurprenom(entity.getAdministrateurprenom());
			if(entity.isAdministrateurisactive()) {
				administrateur.setAdministrateurstatut(env.getProperty("statut.actif"));

			}else {
				administrateur.setAdministrateurstatut(env.getProperty("statut.attente"));

			}
			administrateur.setAdministrateurtelephone(entity.getAdministrateurtelephone());
			//administrateur.setAdministrateurtype(entity.getAdministrateurtype());
			
			administrateur = administrateurService.create(administrateur);
			log.debug("Administrateur crée avec succes");

			return new ResponseEntity<>(administrateur, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'administrateur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour activer/desactiver un administrateur
	 * @return Administrateur
	 */
	@ApiOperation(value = "activer/desactiver un administrateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Administrateur mis à jour|OK"),
			@ApiResponse(code = 204, message = "Administrateur introuvable"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping("/active")
	public ResponseEntity<Administrateur> activeAdministrateurs(@RequestBody ActiveAdminDto entity) {

		try {
			log.debug("Debut de l'activation/desactivation obj: {}", entity.toString());
			Administrateur administrateur = new Administrateur();
			administrateur = administrateurService.findOne(entity.getAdministrateurid());
			if(administrateur != null) {
				
				log.debug("Administrateur activé/desactivé");
				switch (entity.getStatutactive().toLowerCase()) {
				case "attente":
					log.debug("Statut attente");
					administrateur.setAdministrateurstatut(env.getProperty("statut.attente"));
					break;
				case "actif":
					log.debug("Statut actif");
					administrateur.setAdministrateurstatut(env.getProperty("statut.actif"));
					break;
					
				case "annuler":
					log.debug("Statut annuler");
					administrateur.setAdministrateurstatut(env.getProperty("statut.annulation"));
					break;

				default:
					log.debug("Statut defaut");
					administrateur.setAdministrateurstatut(env.getProperty("statut.attente"));
					break;
				}
				
				administrateur = administrateurService.update(administrateur);
				log.debug("Evenement(statut) mis à jour");

				return new ResponseEntity<>(administrateur, HttpStatus.CREATED);
			}else {
				log.debug("Administrateur non trouvé");
				return new ResponseEntity<>(administrateur, HttpStatus.NO_CONTENT);

			}

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'administrateur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un administrateur
	 * @return Administrateur
	 */
	@ApiOperation(value = "Mettre à jour un administrateur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Administrateur updated|OK"),
			@ApiResponse(code = 401, message = "Administrateur non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Administrateur> updateAdministrateurs(@RequestBody Administrateur entity) {

		try {
			log.debug("Debut de la mise à jour de l'administrateur: {}", entity.toString());
			Administrateur administrateur = administrateurService.findOne(entity.getAdministrateurid());
			if(administrateur == null) {
				log.debug("Administrateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Administrateur user = administrateurService.findByAdministrateuremail(entity.getAdministrateuremail());
			if(user != null && user.getAdministrateuremail() != administrateur.getAdministrateuremail() ) {
				log.debug("Email existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			//administrateur.setAdministrateuremail(entity.getAdministrateuremail());
			administrateur.setAdministrateurnom(entity.getAdministrateurnom());
			administrateur.setAdministrateurprenom(entity.getAdministrateurprenom());
			administrateur.setAdministrateurtelephone(entity.getAdministrateurtelephone());
			
			administrateur = administrateurService.update(administrateur);
			log.debug("Administrateur mis à jour avec succes");

			return new ResponseEntity<>(administrateur, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'administrateur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour mettre à jour mot de passe administrateur
	 * @param UpdatePasswordDto
	 */
	@ApiOperation(value = "Mettre à jour mot de passe administrateur")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Mot de passe reset|OK"),
			@ApiResponse(code = 204, message = "Admin non trouvé"),
			@ApiResponse(code = 401, message = "Ancien mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/updatepassword" }, method = RequestMethod.PUT)
	public ResponseEntity<Administrateur> AdministrateurUpdatePassword(@RequestBody UpdatePasswordDto request) {
		try {
			
			log.debug("Debut de la reinitialisation de mot de passe, Objet: {}", request);

			Administrateur admin = administrateurService.findByAdministrateuremail(request.getEmail());

			if (admin != null && passwordEncoder.matches(request.getAncienPassword(), admin.getAdministrateurpassword())) { // Si ancien mot de passe correspond

				admin.setAdministrateurpassword(passwordEncoder.encode(request.getNewPassword()));
				admin = adminService.update(admin);
				
				log.debug("Mot de passe mis à jour");
				return new ResponseEntity<>(admin, HttpStatus.CREATED);


			} else {
				log.debug("Admin inexistant, {} ", request);

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	/**
	 * Fonction pour lister les administrateurs
	 * @return Utilisateur
	 */
	@ApiOperation(value = "Lister les administrateurs", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Administrateur trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun administrateur trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Administrateur>> getAdministrateurs() {

		try {
			log.debug("Afficher les administrateurs");
			List<Administrateur> lsAdministrateur = administrateurService.findAll();

			if(lsAdministrateur.isEmpty()) {
				log.debug("Administrateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Utilisateur trouvé, nombre: {}", lsAdministrateur.size());
				return new ResponseEntity<>(lsAdministrateur, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des administrateurs,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
