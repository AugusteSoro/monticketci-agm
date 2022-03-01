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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.CaissierDto;
import com.wizard.monticketci.dto.CaissierLogedDto;
import com.wizard.monticketci.dto.SignForm;
import com.wizard.monticketci.dto.UpdatePasswordDto;
import com.wizard.monticketci.entities.Caissier;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.security.SecurityConstants;
import com.wizard.monticketci.service.CaissierService;
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

@Api(value = "Gestion des caissiers")
@RestController
@RequestMapping("/caissier")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CaissierController {
	
	@Log
	private Logger log;
	
	@Autowired
	CaissierService caissierService;
	
	@Autowired
	SuperviseurService superviseurService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	DateTools dateTools;
	
	@Autowired
	Environment env;

	
	/**
	 * Fonction pour connexion caissier
	 * @param caissier
	 */
	@ApiOperation(value = "Fonction pour connecter un caissier")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "connexion reussi|OK"),
			@ApiResponse(code = 204, message = "caissier non trouvé"),
			@ApiResponse(code = 401, message = "Mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	public ResponseEntity<CaissierLogedDto> CaissierLogin(@RequestBody SignForm request) {
		try {
			
			log.debug("Debut de la connexion, login: {}", request.login);
			Caissier caissier = caissierService.getAccountEnableByLogin(request.login) ;

			CaissierLogedDto caissierLogedDto = new CaissierLogedDto();
			
			if(caissier != null) {
				
				// Verifier si le mot de passe correspond
				if(passwordEncoder.matches(request.password, caissier.getCaissierpassword())) {
				
				
				//Authentification
				String jwt = Jwts.builder().setSubject(caissier.getCaissieremail())
						.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
						.claim("roles", new ArrayList<>()).compact();

				String token = SecurityConstants.TOKEN_PREFIXE + jwt;

				caissierLogedDto.setToken(token);
				caissierLogedDto.setCaissier(caissier);

				log.debug("success caissier found, email:{}, token:{}", caissierLogedDto.getCaissier().getCaissieremail(), caissierLogedDto.getToken());
				
				return new ResponseEntity<>(caissierLogedDto, HttpStatus.OK);
				}else {
					log.debug("Mot de passe incorrect found");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

				}
			}else {
				log.debug("caissier not found");
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	
	/**
	 * Fonction pour enregistrer un caissier
	 * @return Caissier
	 */
	@ApiOperation(value = "Enregistrer un caissier", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Caissier crée|OK"),
			@ApiResponse(code = 401, message = "Superviseur non trouvé"),
			@ApiResponse(code = 409, message = "caissier existe deja"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Caissier> postCaissiers(@RequestBody CaissierDto entity) {

		try {
			log.debug("Debut de la creation du caissier: {}", entity.toString());
			Caissier caissier = new Caissier();
			Superviseur superviseur = new Superviseur();
			superviseur = superviseurService.findOne(entity.getSuperviseurid());
			
			if(superviseur == null) {
				log.error("Impossible de trouvé le superviseur");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			Caissier caissierInDb = caissierService.findByCaissieremail(entity.getCaissieremail());
			if(caissierInDb != null) {
				log.error("Adresse email existe deja en base");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			caissier.setCaissierdatecreation(dateTools.getCurrentTimeStamp());
			caissier.setCaissieremail(entity.getCaissieremail());
			caissier.setCaissierenable(true);
			caissier.setCaissierisdeleted(false);
			caissier.setCaissiernom(entity.getCaissiernom());
			caissier.setCaissierpassword(passwordEncoder.encode(entity.getCaissierpassword()));
			caissier.setCaissierprenom(entity.getCaissierprenom());
			caissier.setCaissierstatut(env.getProperty("statut.actif"));
			caissier.setCaissiertelephone(entity.getCaissiertelephone());
			caissier.setCaissiercode(env.getProperty("prefix.caissier") + dateTools.generateTransactionIdonTime());
			caissier.setSuperviseur(superviseur);
			
			caissier = caissierService.create(caissier);
			log.debug("Caissier crée avec succes");

			return new ResponseEntity<>(caissier, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'caissier,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un caissier
	 * @return Caissier
	 */
	@ApiOperation(value = "Mettre à jour un caissier", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Caissier updated|OK"),
			@ApiResponse(code = 401, message = "Caissier non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Caissier> updateCaissiers(@RequestBody Caissier entity) {

		try {
			log.debug("Debut de la mise à jour de l'caissier: {}", entity.toString());
			Caissier caissier = caissierService.findOne(entity.getCaissierid());
			if(caissier == null) {
				log.debug("Caissier non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Caissier user = caissierService.findByCaissieremail(entity.getCaissieremail());
			if(user != null && user.getCaissieremail() != caissier.getCaissieremail() ) {
				log.debug("Email existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			//caissier.setCaissieremail(entity.getCaissieremail());
			caissier.setCaissiernom(entity.getCaissiernom());
			caissier.setCaissierprenom(entity.getCaissierprenom());
			caissier.setCaissiertelephone(entity.getCaissiertelephone());
			
			caissier = caissierService.update(caissier);
			log.debug("Caissier mis à jour avec succes");

			return new ResponseEntity<>(caissier, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de l'caissier,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour mettre à jour mot de passe caissier
	 * @param UpdatePasswordDto
	 */
	@ApiOperation(value = "Mettre à jour mot de passe caissier")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Mot de passe reset|OK"),
			@ApiResponse(code = 204, message = "Admin non trouvé"),
			@ApiResponse(code = 401, message = "Ancien mot de passe incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(value = { "/updatepassword" }, method = RequestMethod.PUT)
	public ResponseEntity<Caissier> CaissierUpdatePassword(@RequestBody UpdatePasswordDto request) {
		try {
			
			log.debug("Debut de la reinitialisation de mot de passe, Objet: {}", request);

			Caissier caissier = caissierService.findByCaissieremail(request.getEmail());

			if (caissier != null && passwordEncoder.matches(request.getAncienPassword(), caissier.getCaissierpassword())) { // Si ancien mot de passe correspond

				caissier.setCaissierpassword(passwordEncoder.encode(request.getNewPassword()));
				caissier = caissierService.update(caissier);
				
				log.debug("Mot de passe mis à jour");
				return new ResponseEntity<>(caissier, HttpStatus.CREATED);


			} else {
				log.debug("Caissier inexistant, {} ", request);

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
			
		}catch (Exception e) {
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * Fonction pour lister les caissiers par superviseur
	 * @return Caissier
	 */
	@ApiOperation(value = "Lister les caissiers par superviseur", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "caissier trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun caissier trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("bysuperviseur/{superviseurid}")
	public ResponseEntity<List<Caissier>> getCaissiersBySuperviseur(@PathVariable("superviseurid") String superviseurid) {

		try {
			log.debug("Afficher les caissiers en fonction du superviseur, id: ", superviseurid);
			List<Caissier> lsCaissier = caissierService.findBySuperviseurid(superviseurid);

			if(lsCaissier.isEmpty()) {
				log.debug("Caissier non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Caissier trouvé, nombre: {}", lsCaissier.size());
				return new ResponseEntity<>(lsCaissier, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des caissiers,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour lister les caissiers
	 * @return Caissier
	 */
	@ApiOperation(value = "Lister les caissiers", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "caissier trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun caissier trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Caissier>> getCaissiers() {

		try {
			log.debug("Afficher les caissiers");
			List<Caissier> lsCaissier = caissierService.findAll();

			if(lsCaissier.isEmpty()) {
				log.debug("Caissier non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Caissier trouvé, nombre: {}", lsCaissier.size());
				return new ResponseEntity<>(lsCaissier, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des caissiers,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer un caissier
	 * @return Caissier
	 */
	@ApiOperation(value = "Supprimer un caissier", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "caissier supprimer|OK"),
			@ApiResponse(code = 204, message = "Aucun caissier trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Caissier> deleteCaissier(@PathVariable("id") String id) {

		try {
			log.debug("Supprimer un caissier, id: {}", id);
			
			Caissier caissier = caissierService.findOne(id);

			if(caissier == null) {
				log.debug("Caissier non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				log.debug("Suppression caissier en cours");
				caissierService.delete(caissier);
				return new ResponseEntity<>(caissier, HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des caissiers,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
