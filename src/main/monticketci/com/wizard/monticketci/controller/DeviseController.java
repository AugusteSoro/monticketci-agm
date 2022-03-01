package com.wizard.monticketci.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.DeviseDto;
import com.wizard.monticketci.entities.Devise;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.DeviseService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 01 March,22
 */

@Api(value = "Gestion des devises")
@RestController
@RequestMapping("/devise")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DeviseController {
	
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
	DeviseService deviseService;
	
	
	/**
	 * Fonction pour afficher des devises
	 * @return Devise
	 */
	@ApiOperation(value = "Afficher des devises", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Devise trouvé|OK"),
			@ApiResponse(code = 204, message = "Devise non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Devise>> ObtainDevises() {

		try {
			log.debug("Debut de l'affichage des localisations");
			List<Devise> devise = deviseService.findAll();
			if(devise.size() > 0) {
				
				log.debug("Devise obtenu taille: {}", devise.size());

				return new ResponseEntity<>(devise, HttpStatus.OK);

			}else {
				
				log.debug("Devise non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la devise,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour enregistrer une Devise
	 * @return Devise
	 */
	@ApiOperation(value = "Enregistrer une Devise", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Devise crée|OK"),
			@ApiResponse(code = 401, message = "Admin / Devise non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Devise> postDevises(@RequestBody DeviseDto entity) {

		try {
			log.debug("Debut de la creation d'une Devise: {}", entity.toString());

			Devise Devise = new Devise();
				
			Devise.setDevisenom(entity.getDevisenom());
			Devise.setDevisecode(entity.getDevisecode());
			Devise.setDeviseicon(entity.getDeviseicon());
			Devise.setDevisesymbole(entity.getDevisesymbole());
			Devise.setDeviseenable(true);
			Devise.setDevisestatut(env.getProperty("statut.actif"));
			Devise.setDevisedatecreation(dateTools.getCurrentTimeStamp());
			
			Devise = deviseService.create(Devise);
			log.debug("Devise crée avec succes");

			return new ResponseEntity<>(Devise, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la Devise,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
