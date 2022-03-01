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

import com.wizard.monticketci.dto.VilleDto;
import com.wizard.monticketci.entities.Localisation;
import com.wizard.monticketci.entities.Ville;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.service.VilleService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 11 June,21
 */

@Api(value = "Gestion des villes")
@RestController
@RequestMapping("/ville")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VilleController {
	
	@Log
	private Logger log;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	VilleService villeService;
	
	@Autowired
	Environment env;
	
	@Autowired
	LocalisationService localisationService;
	
	/**
	 * Fonction pour afficher une ville
	 * @return Ville
	 */
	@ApiOperation(value = "Afficher une ville", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ville trouvé|OK"),
			@ApiResponse(code = 204, message = "Ville non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Ville>> ObtainVilles() {

		try {
			log.debug("Debut de l'affichage des villes");
			List<Ville> ville = villeService.findAll();
			if(ville.size() > 0) {
				
				log.debug("Ville obtenu taille: {}", ville.size());

				return new ResponseEntity<>(ville, HttpStatus.OK);

			}else {
				
				log.debug("Ville non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la ville,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer une ville
	 * @return Ville
	 */
	@ApiOperation(value = "Enregistrer une ville", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Ville crée|OK"),
			@ApiResponse(code = 401, message = "Admin non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Ville> postVilles(@RequestBody VilleDto entity) {

		try {
			log.debug("Debut de la creation d'une ville: {}", entity.toString());
			
			log.debug("Recherche de la localisation par id: {}", entity.getLocalisationid());
			Localisation localisation = localisationService.findOne(entity.getLocalisationid());
			if(localisation == null) {
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}

			Ville ville = new Ville();
				
			ville.setLocalisation(localisation);;
			ville.setVillecode(entity.getVillecode());
			ville.setVilleicon(entity.getVilleicon());
			ville.setVilleenable(true);
			ville.setVillenom(entity.getVillenom());
			ville.setVillestatut(env.getProperty("statut.actif"));
			ville.setVilledatecreation(dateTools.getCurrentTimeStamp());
			
			ville = villeService.create(ville);
			log.debug("Ville crée avec succes");

			return new ResponseEntity<>(ville, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la ville,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour une ville
	 * @return Ville
	 */
	@ApiOperation(value = "Mettre à jour une ville", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Ville updated|OK"),
			@ApiResponse(code = 401, message = "Ville non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Ville> updateVilles(@RequestBody Ville entity) {

		try {
			log.debug("Debut de la mise à jour de la ville: {}", entity.toString());
			Ville ville = villeService.findOne(entity.getVilleid());
			if(ville == null) {
				log.debug("Ville non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Ville local = villeService.findByName(entity.getVillenom());
			if(local != null && local.getVillenom() != ville.getVillenom() ) {
				log.debug("Nom existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			ville.setVillenom(entity.getVillenom());
			ville.setVillecode(entity.getVillecode());
			ville.setVilleicon(entity.getVilleicon());
			
			ville = villeService.update(ville);
			log.debug("Ville mis à jour avec succes");

			return new ResponseEntity<>(ville, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'ville,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer une ville
	 * @return Ville
	 */
	@ApiOperation(value = "Supprimer une ville", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ville deleted|OK"),
			@ApiResponse(code = 204, message = "Ville non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Ville> deleteVilles(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour de la ville id: {}", id);
			Ville ville = villeService.findOne(id);
			if(ville == null) {
				log.debug("Ville non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				
				villeService.delete(ville);
				log.debug("Ville mis à jour avec succes");

				return new ResponseEntity<>(ville, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la ville,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
