package com.wizard.monticketci.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.ActiveSuperviseurDto;
import com.wizard.monticketci.dto.SignForm;
import com.wizard.monticketci.dto.SuperviseurDto;
import com.wizard.monticketci.dto.SuperviseurLogedDto;
import com.wizard.monticketci.dto.UpdatePasswordDto;
import com.wizard.monticketci.entities.Administrateur;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.security.SecurityConstants;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.SuperviseurService;
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

@Api(value = "Gestion des superviseurs")
@RestController
@RequestMapping("/superviseur")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SuperviseurController {
	
	@Log
	private Logger log;
	
	@Autowired
	SuperviseurService superviseurService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	Environment env;

	
	/**
	 * Fonction pour connexion superviseur
	 * @param superviseur
	 */
	@ApiOperation(value = "Fonction pour connecter un superviseur")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "connexion reussi|OK"),
			@ApiResponse(code = 204, message = "superviseur non trouvé"),
			@ApiResponse(code = 401, message = "Mot de passe incorrect"),
			@ApiResponse(code = 406, message = "Compte inactif"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ResponseEntity<SuperviseurLogedDto> SuperviseurLogin(@RequestBody SignForm request) {
		try {
			
			log.debug("Debut de la connexion, login: {}", request.login);
			Superviseur superviseur = superviseurService.getAccountEnableByLogin(request.login) ;

			SuperviseurLogedDto superviseurLogedDto = new SuperviseurLogedDto();
			
			if(superviseur != null) {
				
				if(!superviseur.getSuperviseurstatut().equals(env.getProperty("statut.actif"))) {
					log.debug("Compte inactif");
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
				}
				
				// Verifier si le mot de passe correspond
				if(passwordEncoder.matches(request.password, superviseur.getSuperviseurpassword())) {
				
				
				//Authentification
				String jwt = Jwts.builder().setSubject(superviseur.getSuperviseuremail())
						.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
						.claim("roles", new ArrayList<>()).compact();

				String token = SecurityConstants.TOKEN_PREFIXE + jwt;

				superviseurLogedDto.setToken(token);
				superviseurLogedDto.setSuperviseur(superviseur);

				log.debug("success superviseur found, email:{}, token:{}", superviseurLogedDto.getSuperviseur().getSuperviseuremail(), superviseurLogedDto.getToken());

				
				return new ResponseEntity<>(superviseurLogedDto, HttpStatus.OK);
				}else {
					log.debug("Mot de passe incorrect found");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

				}
			}else {
				log.debug("superviseur not found");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	
	/**
	 * Fonction pour enregistrer un superviseur
	 * @return Superviseur
	 */
	@ApiOperation(value = "Enregistrer un superviseur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Superviseur crée|OK"),
			@ApiResponse(code = 401, message = "Administrateur non existant"),
			@ApiResponse(code = 409, message = "Superviseur existe deja"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Superviseur> postSuperviseurs(@RequestBody SuperviseurDto entity) {

		try {
			log.debug("Debut de la creation du superviseur: {}", entity.toString());
			
			log.debug("Recherche de l'admin");
			Administrateur administrateur = adminService.findOne(entity.getAdministrateurid());
			if(administrateur == null) {
				log.debug("Administrateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Superviseur superviseurInDb = superviseurService.findBySuperviseuremail(entity.getSuperviseuremail());
			if(superviseurInDb != null) {
				log.error("Adresse email existe deja en base");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			Superviseur superviseur = new Superviseur();
				
			superviseur.setAdministrateurid(administrateur);
			superviseur.setSuperviseurdatecreation(dateTools.getCurrentTimeStamp());
			superviseur.setSuperviseuremail(entity.getSuperviseuremail());
			superviseur.setSuperviseurresidence(entity.getSuperviseurresidence());
			superviseur.setSuperviseurenable(true);
			superviseur.setSuperviseurisdeleted(false);
			superviseur.setSuperviseurnom(entity.getSuperviseurnom());
			superviseur.setSuperviseurpassword(passwordEncoder.encode(entity.getSuperviseurpassword()));
			superviseur.setSuperviseurprenom(entity.getSuperviseurprenom());
			if(entity.isSuperviseurisactive()) {
				superviseur.setSuperviseurstatut(env.getProperty("statut.actif"));

			}else {
				superviseur.setSuperviseurstatut(env.getProperty("statut.attente"));

			}
			superviseur.setSuperviseurtelephone(entity.getSuperviseurtelephone());
			superviseur.setSuperviseurtype(entity.getSuperviseurtype());
			superviseur.setSuperviseurraisonsocial(entity.getSuperviseurraisonsocial());
			
			/*
			private String superviseursigle;
			
			private String superviseurtitre;
			
			private String superviseuractivite;
			
			private String superviseurformejuridique;
			
			private String superviseurpays;
			
			private String superviseurville;
			
			private String superviseuremailrepresentant;
			
			private String superviseurtelephonerepresentant;
			
			private String superviseurfonction;
			
			private boolean superviseuractivitehorsci;
			
			private String superviseurpaysactivite;
			
			private boolean superviseurmonticketcihorsci;
			*/
			
			superviseur.setSuperviseursigle(entity.getSuperviseursigle());
			superviseur.setSuperviseurtitre(entity.getSuperviseurtitre());
			superviseur.setSuperviseuractivite(entity.getSuperviseuractivite());
			superviseur.setSuperviseurformejuridique(entity.getSuperviseurformejuridique());
			superviseur.setSuperviseurpays(entity.getSuperviseurpays());
			superviseur.setSuperviseurville(entity.getSuperviseurville());
			superviseur.setSuperviseuremailrepresentant(entity.getSuperviseuremailrepresentant());
			superviseur.setSuperviseurtelephonerepresentant(entity.getSuperviseurtelephonerepresentant());
			superviseur.setSuperviseurfonction(entity.getSuperviseurfonction());
			String paysSelected = "";
			for (String iterate : entity.getSuperviseurpaysactivite()) {
				paysSelected += iterate + ',';
			}
			superviseur.setSuperviseurpaysactivite(paysSelected);

			superviseur.setSuperviseuractivitehorsci(entity.isSuperviseuractivitehorsci());
			superviseur.setSuperviseurmonticketcihorsci(entity.isSuperviseurmonticketcihorsci());

			
			superviseur = superviseurService.create(superviseur);
			log.debug("Superviseur crée avec succes");

			return new ResponseEntity<>(superviseur, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'superviseur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour activer/desactiver un superviseur
	 * @return Superviseur
	 */
	@ApiOperation(value = "activer/desactiver un superviseur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Superviseur mis à jour|OK"),
			@ApiResponse(code = 204, message = "Superviseur introuvable"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping("/active")
	public ResponseEntity<Superviseur> activeSuperviseurs(@RequestBody ActiveSuperviseurDto entity) {

		try {
			log.debug("Debut de l'activation/desactivation obj: {}", entity.toString());
			Superviseur superviseur = new Superviseur();
			superviseur = superviseurService.findOne(entity.getSuperviseurid());
			if(superviseur != null) {
				
				log.debug("Superviseur activé/desactivé");
				switch (entity.getStatutactive().toLowerCase()) {
				case "attente":
					log.debug("Statut attente");
					superviseur.setSuperviseurstatut(env.getProperty("statut.attente"));
					break;
				case "actif":
					log.debug("Statut actif");
					superviseur.setSuperviseurstatut(env.getProperty("statut.actif"));
					break;
					
				case "annuler":
					log.debug("Statut annuler");
					superviseur.setSuperviseurstatut(env.getProperty("statut.annulation"));
					break;

				default:
					log.debug("Statut defaut");
					superviseur.setSuperviseurstatut(env.getProperty("statut.attente"));
					break;
				}
				superviseur = superviseurService.update(superviseur);
				log.debug("Superviseur(statut) mis à jour");
				
				return new ResponseEntity<>(superviseur, HttpStatus.CREATED);
			}else {
				log.debug("Superviseur non trouvé");
				return new ResponseEntity<>(superviseur, HttpStatus.NO_CONTENT);

			}

		} catch (Exception e) {

			log.error("erreur lors de la creation du superviseur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un superviseur
	 * @return Superviseur
	 */
	@ApiOperation(value = "Mettre à jour un superviseur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Superviseur updated|OK"),
			@ApiResponse(code = 401, message = "Superviseur non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Superviseur> updateSuperviseurs(@RequestBody SuperviseurDto entity) {

		try {
			log.debug("Debut de la mise à jour de l'superviseur: {}", entity.toString());
			Superviseur superviseur = superviseurService.findOne(entity.getSuperviseurid());
			if(superviseur == null) {
				log.debug("Superviseur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Superviseur user = superviseurService.findBySuperviseuremail(entity.getSuperviseuremail());
			if(user != null && user.getSuperviseuremail() != superviseur.getSuperviseuremail() ) {
				log.debug("Email existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			//superviseur.setSuperviseuremail(entity.getSuperviseuremail());
			superviseur.setSuperviseurnom(entity.getSuperviseurnom());
			superviseur.setSuperviseurprenom(entity.getSuperviseurprenom());
			superviseur.setSuperviseurtelephone(entity.getSuperviseurtelephone());
			superviseur.setSuperviseurresidence(entity.getSuperviseurresidence());
			superviseur.setSuperviseurraisonsocial(entity.getSuperviseurraisonsocial());
			
			
			superviseur.setSuperviseursigle(entity.getSuperviseursigle());
			superviseur.setSuperviseurtitre(entity.getSuperviseurtitre());
			superviseur.setSuperviseuractivite(entity.getSuperviseuractivite());
			superviseur.setSuperviseurformejuridique(entity.getSuperviseurformejuridique());
			superviseur.setSuperviseurpays(entity.getSuperviseurpays());
			superviseur.setSuperviseurville(entity.getSuperviseurville());
			superviseur.setSuperviseuremailrepresentant(entity.getSuperviseuremailrepresentant());
			superviseur.setSuperviseurtelephonerepresentant(entity.getSuperviseurtelephonerepresentant());
			superviseur.setSuperviseurfonction(entity.getSuperviseurfonction());
			superviseur.setSuperviseuractivitehorsci(entity.isSuperviseuractivitehorsci());
			//superviseur.setSuperviseurpaysactivite(entity.getSuperviseurpaysactivite());
			superviseur.setSuperviseurmonticketcihorsci(entity.isSuperviseurmonticketcihorsci());


			superviseur = superviseurService.update(superviseur);
			log.debug("Superviseur mis à jour avec succes");

			return new ResponseEntity<>(superviseur, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'superviseur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour mettre à jour mot de passe superviseur
	 * @param UpdatePasswordDto
	 */
	@ApiOperation(value = "Mettre à jour mot de passe superviseur")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Mot de passe reset|OK"),
			@ApiResponse(code = 204, message = "Admin non trouvé"),
			@ApiResponse(code = 401, message = "Ancien mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/updatepassword" }, method = RequestMethod.PUT)
	public ResponseEntity<Superviseur> SuperviseurUpdatePassword(@RequestBody UpdatePasswordDto request) {
		try {
			
			log.debug("Debut de la reinitialisation de mot de passe, Objet: {}", request);

			Superviseur superviseur = superviseurService.findBySuperviseuremail(request.getEmail());

			if (superviseur != null && passwordEncoder.matches(request.getAncienPassword(), superviseur.getSuperviseurpassword())) { // Si ancien mot de passe correspond

				superviseur.setSuperviseurpassword(passwordEncoder.encode(request.getNewPassword()));
				superviseur = superviseurService.update(superviseur);
				
				log.debug("Mot de passe mis à jour");
				return new ResponseEntity<>(superviseur, HttpStatus.CREATED);


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
	 * Fonction pour supprimer les superviseurs
	 * @return Superviseur
	 */
	@ApiOperation(value = "Supprimer les superviseurs", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Superviseur supprimé|OK"),
			@ApiResponse(code = 204, message = "Aucun superviseur trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping(value = { "/{id}" })
	public ResponseEntity<Superviseur> delSuperviseurs(@PathVariable("id") @NotBlank String id) {

		try {
			log.debug("Rechercher le superviseur");
			Superviseur superviseur = superviseurService.findOne(id);

			if(superviseur == null) {
				log.debug("Superviseur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Superviseur trouvé");
				superviseurService.delete(superviseur);
				return new ResponseEntity<>(HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des utilisateurs,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour lister les superviseurs
	 * @return Superviseur
	 */
	@ApiOperation(value = "Lister les superviseurs", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Superviseur trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun superviseur trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Superviseur>> getSuperviseurs() {

		try {
			log.debug("Afficher les superviseurs");
			List<Superviseur> lsSuperviseur = superviseurService.findAll();

			if(lsSuperviseur.isEmpty()) {
				log.debug("Superviseur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Superviseur trouvé, nombre: {}", lsSuperviseur.size());
				return new ResponseEntity<>(lsSuperviseur, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des utilisateurs,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
}
