package com.wizard.monticketci.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.LocalisationDto;
import com.wizard.monticketci.entities.Administrateur;
import com.wizard.monticketci.entities.Devise;
import com.wizard.monticketci.entities.Localisation;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.DeviseService;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.tools.DateTools;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 03 June,21
 */

@Api(value = "Gestion des localisations")
@RestController
@RequestMapping("/localisation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LocalisationController {
	
	@Log
	private Logger log;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	Environment env;
	
	@Autowired
	LocalisationService localisationService;
	
	@Autowired
	DeviseService deviseService;
	
	
	/**
	 * Fonction pour afficher une localisation
	 * @return Localisation
	 */
	@ApiOperation(value = "Afficher une localisation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Localisation trouvé|OK"),
			@ApiResponse(code = 204, message = "Localisation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Localisation>> ObtainLocalisations() {

		try {
			log.debug("Debut de l'affichage des localisations");
			List<Localisation> localisation = localisationService.findAll();
			if(localisation.size() > 0) {
				
				log.debug("Localisation obtenu taille: {}", localisation.size());

				return new ResponseEntity<>(localisation, HttpStatus.OK);

			}else {
				
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la localisation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer une localisation
	 * @return Localisation
	 */
	@ApiOperation(value = "Enregistrer une localisation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Localisation crée|OK"),
			@ApiResponse(code = 401, message = "Admin / Devise non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Localisation> postLocalisations(@RequestBody LocalisationDto entity) {

		try {
			log.debug("Debut de la creation d'une localisation: {}", entity.toString());
			
			log.debug("Recherche de l'admin par id: {}", entity.getAdministrateurid());
			Administrateur admin = adminService.findOne(entity.getAdministrateurid());
			if(admin == null) {
				log.debug("Admin non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			log.debug("Recherche de la devise par id: {}", entity.getDeviseid());
			Devise devise = deviseService.findOne(entity.getDeviseid());
			if(devise == null) {
				log.debug("Devise non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			

			Localisation localisation = new Localisation();
				
			localisation.setAdministrateur(admin);
			localisation.setDevise(devise);
			localisation.setLocalisationcode(entity.getLocalisationcode());
			localisation.setLocalisationicon(entity.getLocalisationicon());
			localisation.setLocalisationnbredigittel(entity.getLocalisationnbredigittel());
			localisation.setLocalisationenable(true);
			localisation.setLocalisationnom(entity.getLocalisationnom());
			localisation.setLocalisationstatut(env.getProperty("statut.actif"));
			localisation.setLocalisationdatecreation(dateTools.getCurrentTimeStamp());
			
			localisation = localisationService.create(localisation);
			log.debug("Localisation crée avec succes");

			return new ResponseEntity<>(localisation, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la localisation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour une localisation
	 * @return Localisation
	 */
	@ApiOperation(value = "Mettre à jour une localisation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Localisation updated|OK"),
			@ApiResponse(code = 401, message = "Localisation non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Localisation> updateLocalisations(@RequestBody LocalisationDto entity) {

		try {
			log.debug("Debut de la mise à jour de la localisation: {}", entity.toString());
			Localisation localisation = localisationService.findOne(entity.getLocalisationid());
			if(localisation == null) {
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			log.debug("Recherche de la devise par id: {}", entity.getDeviseid());
			Devise devise = deviseService.findOne(entity.getDeviseid());
			if(devise == null) {
				log.debug("Devise non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			Localisation local = localisationService.findByName(entity.getLocalisationnom());
			if(local != null && local.getLocalisationnom() != localisation.getLocalisationnom() ) {
				log.debug("Nom existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			localisation.setLocalisationnom(entity.getLocalisationnom());
			localisation.setLocalisationcode(entity.getLocalisationcode());
			localisation.setLocalisationicon(entity.getLocalisationicon());
			localisation.setLocalisationnbredigittel(entity.getLocalisationnbredigittel());
			
			localisation.setDevise(devise);
			
			localisation = localisationService.update(localisation);
			log.debug("Localisation mis à jour avec succes");

			return new ResponseEntity<>(localisation, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'localisation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer une localisation
	 * @return Localisation
	 */
	@ApiOperation(value = "Supprimer une localisation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Localisation deleted|OK"),
			@ApiResponse(code = 204, message = "Localisation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Localisation> deleteLocalisations(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour de la localisation id: {}", id);
			Localisation localisation = localisationService.findOne(id);
			if(localisation == null) {
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				
				localisationService.delete(localisation);
				log.debug("Localisation mis à jour avec succes");

				return new ResponseEntity<>(localisation, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la localisation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	


}
