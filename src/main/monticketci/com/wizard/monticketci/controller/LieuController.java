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

import com.wizard.monticketci.dto.LieuDto;
import com.wizard.monticketci.entities.Lieu;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.LieuService;
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

@Api(value = "Gestion des lieux")
@RestController
@RequestMapping("/lieu")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LieuController {
	
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
	LieuService lieuService;
	
	
	/**
	 * Fonction pour afficher les lieux
	 * @return Lieu
	 */
	@ApiOperation(value = "Afficher les lieux", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lieu trouvé|OK"),
			@ApiResponse(code = 204, message = "Lieu non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}")
	public ResponseEntity<List<Lieu>> ObtainLieuBySuperviseurId(@PathVariable("id") String id) {

		try {
			log.debug("Debut de l'affichage des lieux");
			List<Lieu> lieu = lieuService.findPrixBySuperviseurId(id);
			if(lieu.size() > 0) {
				
				log.debug("Lieu obtenu taille: {}", lieu.size());

				return new ResponseEntity<>(lieu, HttpStatus.OK);

			}else {
				
				log.debug("Lieu non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un lieu,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un lieu
	 * @return Lieu
	 */
	@ApiOperation(value = "Afficher un lieu", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lieu trouvé|OK"),
			@ApiResponse(code = 204, message = "Lieu non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Lieu>> ObtainLieu() {

		try {
			log.debug("Debut de l'affichage des lieux");
			List<Lieu> lieu = lieuService.findAll();
			if(lieu.size() > 0) {
				
				log.debug("Lieu obtenu taille: {}", lieu.size());

				return new ResponseEntity<>(lieu, HttpStatus.OK);

			}else {
				
				log.debug("Lieu non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un lieu,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer un lieu
	 * @return Lieu
	 */
	@ApiOperation(value = "Enregistrer un lieu", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lieu crée|OK"),
			@ApiResponse(code = 401, message = "Admin non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Lieu> postLieu(@RequestBody LieuDto entity) {

		try {
			log.debug("Debut de la creation d'un lieu: {}", entity.toString());
			
			log.debug("Recherche du superviseur");
			Superviseur superviseur = new Superviseur();
			superviseur = superviseurService.findOne(entity.getSuperviseurid());

			if(superviseur == null) {
				log.error("Impossible de trouvé le superviseur");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			log.debug("Superviseur trouvé, id:{}", superviseur.getSuperviseurid());


			Lieu lieu = new Lieu();
			
			lieu.setSuperviseur(superviseur);

			lieu.setLieudescription(entity.getLieudescription());
			lieu.setLieunom(entity.getLieunom());
			lieu.setLieupays(entity.getLieupays());
			lieu.setLieucommune(entity.getLieucommune());
			lieu.setLieuville(entity.getLieuville());
			lieu.setLieuenable(true);
			lieu.setLieustatut(env.getProperty("statut.actif"));
			lieu.setLieudatecreation(dateTools.getCurrentTimeStamp());
			
			lieu = lieuService.create(lieu);
			log.debug("Lieu crée avec succes");

			return new ResponseEntity<>(lieu, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un lieu,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un lieu
	 * @return Lieu
	 */
	@ApiOperation(value = "Mettre à jour un lieu", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lieu updated|OK"),
			@ApiResponse(code = 401, message = "Lieu non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Lieu> updateLieu(@RequestBody Lieu entity) {

		try {
			log.debug("Debut de la mise à jour d'un lieu: {}", entity.toString());
			Lieu lieu = lieuService.findOne(entity.getLieuid());
			if(lieu == null) {
				log.debug("Lieu non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Lieu local = lieuService.findByNom(entity.getLieunom());
			if(local != null && local.getLieunom() != lieu.getLieunom() ) {
				log.debug("Nom existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			lieu.setLieudescription(entity.getLieudescription());
			lieu.setLieupays(entity.getLieupays());
			lieu.setLieuville(entity.getLieuville());
			lieu.setLieucommune(entity.getLieucommune());

			lieu = lieuService.update(lieu);
			log.debug("Lieu mis à jour avec succes");

			return new ResponseEntity<>(lieu, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un lieu,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer un lieu
	 * @return Lieu
	 */
	@ApiOperation(value = "Supprimer un lieu", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lieu deleted|OK"),
			@ApiResponse(code = 204, message = "Lieu non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Lieu> deleteLieu(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour d'un lieu id: {}", id);
			Lieu lieu = lieuService.findOne(id);
			if(lieu == null) {
				log.debug("Lieu non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				
				lieuService.delete(lieu);
				log.debug("Lieu mis à jour avec succes");

				return new ResponseEntity<>(lieu, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un lieu,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


}
