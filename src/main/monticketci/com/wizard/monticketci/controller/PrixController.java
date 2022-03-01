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

import com.wizard.monticketci.dto.PrixDto;
import com.wizard.monticketci.entities.Prix;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.PrixService;
import com.wizard.monticketci.service.SuperviseurService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 03 June,21
 */

@Api(value = "Gestion des prix")
@RestController
@RequestMapping("/prix")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PrixController {
	
	@Log
	private Logger log;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	SuperviseurService superviseurService;
	
	@Autowired
	Environment env;
	
	@Autowired
	PrixService prixService;
	
	/**
	 * Fonction pour afficher les prix
	 * @return Prix
	 */
	@ApiOperation(value = "Afficher les prix", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Prix trouvé|OK"),
			@ApiResponse(code = 204, message = "Prix non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Prix>> ObtainPrixs() {

		try {
			log.debug("Debut de l'affichage des prixs");
			List<Prix> prix = prixService.findAll();
			if(prix.size() > 0) {
				
				log.debug("Prix obtenu taille: {}", prix.size());

				return new ResponseEntity<>(prix, HttpStatus.OK);

			}else {
				
				log.debug("Prix non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un prix,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour afficher les prix par la superviseurid
	 * @return Prix
	 */
	@ApiOperation(value = "Afficher un prix", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Prix trouvé|OK"),
			@ApiResponse(code = 204, message = "Prix non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}")
	public ResponseEntity<List<Prix>> ObtainPrixBySuperviseurId(@PathVariable("id") String id) {

		try {
			log.debug("Debut de l'affichage des prixs, id: {}", id);
			List<Prix> prix = prixService.findPrixBySuperviseurId(id);
			if(prix.size() > 0) {
				
				log.debug("Prix obtenu taille: {}", prix.size());

				return new ResponseEntity<>(prix, HttpStatus.OK);

			}else {
				
				log.debug("Prix non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un prix,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer un prix
	 * @return Prix
	 */
	@ApiOperation(value = "Enregistrer un prix", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Prix crée|OK"),
			@ApiResponse(code = 401, message = "Admin non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Prix> postPrix(@RequestBody PrixDto entity) {

		try {
			log.debug("Debut de la creation d'un prix: {}", entity.toString());
			
			log.debug("Recherche du superviseur");
			Superviseur superviseur = new Superviseur();
			superviseur = superviseurService.findOne(entity.getSuperviseurid());
			
			if(superviseur == null) {
				log.error("Impossible de trouvé le superviseur");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			Prix prix = new Prix();
			
			prix.setSuperviseur(superviseur);
				
			prix.setPrixvaleur(entity.getPrixvaleur());
			prix.setPrixlibelle(entity.getPrixlibelle());
			prix.setPrixdescription(entity.getPrixdescription());
			prix.setPrixenable(true);
			prix.setPrixstatut(env.getProperty("statut.actif"));
			prix.setPrixdatecreation(dateTools.getCurrentTimeStamp());
			
			prix = prixService.create(prix);
			log.debug("Prix crée avec succes");

			return new ResponseEntity<>(prix, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un prix,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un prix
	 * @return Prix
	 */
	@ApiOperation(value = "Mettre à jour un prix", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Prix updated|OK"),
			@ApiResponse(code = 401, message = "Prix non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Prix> updatePrix(@RequestBody PrixDto entity) {

		try {
			log.debug("Debut de la mise à jour d'un prix: {}", entity.toString());
			Prix prix = prixService.findOne(entity.getPrixid());
			if(prix == null) {
				log.debug("Prix non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Prix local = prixService.findByValeurAndSuperviseur(entity.getPrixvaleur(), entity.getSuperviseurid());
			if(local != null && local.getPrixvaleur() != prix.getPrixvaleur() ) {
				log.debug("Valeur existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			prix.setPrixvaleur(entity.getPrixvaleur());
			prix.setPrixlibelle(entity.getPrixlibelle());
			prix.setPrixdescription(entity.getPrixdescription());
			
			prix = prixService.update(prix);
			log.debug("Prix mis à jour avec succes");

			return new ResponseEntity<>(prix, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un prix,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer un prix
	 * @return Prix
	 */
	@ApiOperation(value = "Supprimer un prix", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Prix deleted|OK"),
			@ApiResponse(code = 204, message = "Prix non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Prix> deletePrixs(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour d'un prix id: {}", id);
			Prix prix = prixService.findOne(id);
			if(prix == null) {
				log.debug("Prix non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				
				prixService.delete(prix);
				log.debug("Prix mis à jour avec succes");

				return new ResponseEntity<>(prix, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un prix,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
