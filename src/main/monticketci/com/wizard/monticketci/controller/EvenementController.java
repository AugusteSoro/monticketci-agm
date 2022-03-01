package com.wizard.monticketci.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wizard.monticketci.dto.ActiveEvenementDto;
import com.wizard.monticketci.dto.EvenementDto;
import com.wizard.monticketci.entities.Evenement;
import com.wizard.monticketci.entities.Evenementlieu;
import com.wizard.monticketci.entities.Evenementprix;
import com.wizard.monticketci.entities.Lieu;
import com.wizard.monticketci.entities.Localisation;
import com.wizard.monticketci.entities.Prix;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.entities.Typeevent;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.EvenementService;
import com.wizard.monticketci.service.EvenementlieuService;
import com.wizard.monticketci.service.EvenementprixService;
import com.wizard.monticketci.service.FileService;
import com.wizard.monticketci.service.LieuService;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.service.PrixService;
import com.wizard.monticketci.service.SuperviseurService;
import com.wizard.monticketci.service.TypeeventService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 03 June,21
 */

@Api(value = "Gestion des evenements")
@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EvenementController {
	
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
	EvenementService evenementService;
	
	@Autowired
	TypeeventService typeeventService;
	
	@Autowired
	SuperviseurService superviseurService;
	
	@Autowired
	LocalisationService localisationService;
	
	@Autowired
	LieuService lieuService;
	
	@Autowired
	PrixService prixService;
	
	@Autowired
	EvenementlieuService evenementlieuService;
	
	@Autowired
	EvenementprixService evenementprixService;

	@Autowired
	FileService fileService;
	
	@Value("${event.upload.baseurl}")
	private String baseUrl;
	
	@Value("${event.upload.path}")
	private String path;
	
	/**
	 * Fonction pour afficher les evenements
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher les evenements", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Evenement>> ObtainEvenement() {

		try {
			log.debug("Debut de l'affichage des evenements");
			List<Evenement> evenement = evenementService.findAll();
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}	
	
	
	/**
	 * Fonction pour afficher les evenements limités
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher les evenements limités", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("limit3")
	public ResponseEntity<List<Evenement>> ObtainEvenementLimit() {

		try {
			log.debug("Debut de l'affichage des 3 derniers evenements");
			List<Evenement> evenement = evenementService.findTop3Event();
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention des evenements,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par son ID
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un evenement par son ID", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Evenement> ObtainEvenementById(@PathVariable("id") String id) {

		try {
			log.debug("Debut de l'affichage de l'evenement, id: {}", id);
			Evenement evenement = evenementService.findOne(id);
			if(evenement != null) {
				
				log.debug("Evenement obtenu: {}", evenement);

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par le statut
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un evenement par le statut", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bystatut/{statut}")
	public ResponseEntity<List<Evenement>> ObtainEvenementByStatut(@PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des evenements, statut: {}", statut);
			List<Evenement> evenement = evenementService.findAllByStatut(statut);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour afficher un evenement par superviseurid
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un evenement par superviseurid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}")
	public ResponseEntity<List<Evenement>> ObtainEventsBySuperviseurId(@PathVariable("id") String id) {

		try {
			log.debug("Debut de l'affichage des evenements, id:{}", id);
			List<Evenement> evenement = evenementService.findEventsBySuperviseurId(id);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par superviseurid et par statut
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un evenement par superviseurid et par statut", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}/{statut}")
	public ResponseEntity<List<Evenement>> ObtainEventsBySuperviseurIdAndStatut(@PathVariable("id") String id, @PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des evenements, id:{}, statut:{}", id, statut);
			List<Evenement> evenement = evenementService.findEventsBySuperviseurIdAndStatut(id, statut);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	
	/**
	 * Fonction pour afficher le prix d'un evenement par un evenementid 
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher le prix d'un evenement par un evenementid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/findprix/{id}")
	public ResponseEntity<List<Evenementprix>> ObtainPrixByEvenement(@PathVariable String id) {

		try {
			log.debug("Debut de l'affichage des prix par evenement");
			List<Evenementprix> evenementprix = evenementprixService.findByEvenementId(id);
			if(evenementprix.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenementprix.size());

				return new ResponseEntity<>(evenementprix, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher le prix d'un evenement par un evenementid 
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher le lieu d'un evenement par un evenementid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/findlieu/{id}")
	public ResponseEntity<List<Evenementlieu>> ObtainLieuxByEvenement(@PathVariable String id) {

		try {
			log.debug("Debut de l'affichage des lieux par evenement");
			List<Evenementlieu> evenementlieu = evenementlieuService.findByEvenementId(id);
			if(evenementlieu.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenementlieu.size());

				return new ResponseEntity<>(evenementlieu, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par un typeevent 
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un afficher un evenement par un typeevent", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/findbytypeevent/{id}")
	public ResponseEntity<List<Evenement>> ObtainByTypeevent(@PathVariable String id) {

		try {
			log.debug("Debut de l'affichage des evenements par typeevent, id: {}", id);
			List<Evenement> evenement = evenementService.findByTypeeventId(id);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par un typeevent et son statut
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un afficher un evenement par un typeevent et son statut", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/typeeventandstatut/{id}/{statut}")
	public ResponseEntity<List<Evenement>> ObtainByTypeeventAndStatut(@PathVariable("id") String id, @PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des evenements par typeevent, id: {}, statut:{}", id, statut);
			List<Evenement> evenement = evenementService.findByTypeeventIdAndStatut(id, statut);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un evenement par un typeevent, son statut et sa localisation
	 * @return Evenement
	 */
	@ApiOperation(value = "Afficher un afficher un evenement par un typeevent, son statut et sa localisation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement trouvé|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/typeeventstatutandlocalisation/{id}/{localisation}/{statut}")
	public ResponseEntity<List<Evenement>> ObtainByTypeeventAndLocalisationAndStatut(@PathVariable("id") String id, @PathVariable("localisation") String localisation, @PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des evenements par typeevent, id: {}, localisation:{}, statut:{}", id, localisation, statut);
			List<Evenement> evenement = evenementService.findByTypeeventIdAndLocalisationAndStatut(id, localisation, statut);
			if(evenement.size() > 0) {
				
				log.debug("Evenement obtenu taille: {}", evenement.size());

				return new ResponseEntity<>(evenement, HttpStatus.OK);

			}else {
				
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer un evenement
	 * @return Evenement
	 */
	@ApiOperation(value = "Enregistrer un evenement", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Evenement crée|OK"),
			@ApiResponse(code = 401, message = "Typeevent / superviseur / Localisation non trouvé"),
			@ApiResponse(code = 406, message = "Quantité incorrect"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Evenement> postEvenement(@RequestBody EvenementDto entity) {

		try {
			log.debug("Debut de la creation d'un evenement: {}", entity.toString());
			
			log.debug("Verification des quantités");
			if(entity.getEvenementqteglobal() < entity.getEvenementqtemticket()) {
				log.debug("Quantité incorrect");
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

			}
			
			log.debug("Recherche du typeevent par id: {}", entity.getTypeeventid());
			Typeevent typeevent = typeeventService.findOne(entity.getTypeeventid());
			if(typeevent == null) {
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			log.debug("Recherche du superviseur par id: {}", entity.getSuperviseurid());
			Superviseur superviseur = superviseurService.findOne(entity.getSuperviseurid());
			if(superviseur == null) {
				log.debug("Superviseur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			log.debug("Recherche de la localisation par id: {}", entity.getLocalisationid());
			Localisation localisation = localisationService.findOne(entity.getLocalisationid());
			if(localisation == null) {
				log.debug("Localisation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Evenement eventInDb = evenementService.findByNom(entity.getEvenementnom());
			if(eventInDb == null) {

				Evenement evenement = new Evenement();
				
				evenement.setSuperviseur(superviseur);
				evenement.setLocalisation(localisation);
				evenement.setTypeevent(typeevent);
				evenement.setEvenementdescription(entity.getEvenementdescription());
				evenement.setEvenementnom(entity.getEvenementnom());
				evenement.setEvenementenable(true);
				evenement.setEvenementqteglobal(entity.getEvenementqteglobal());
				evenement.setEvenementqtemticket(entity.getEvenementqtemticket());
				evenement.setEvenementqteprise(0);
				if(entity.isEvenementisactive()) {
					evenement.setEvenementstatut(env.getProperty("statut.actif"));

				}else {
					evenement.setEvenementstatut(env.getProperty("statut.attente"));

				}
				evenement.setEvenementdatecreation(dateTools.getCurrentTimeStamp());
				evenement.setEvenementdateevenement(entity.getEvenementdateevenement());
				
				evenement = evenementService.create(evenement);
				log.debug("Evenement crée avec succes");
				
				log.debug("Ajouter la liste des lieux");
				for (String id : entity.getLieu()) {
					log.debug("ID: {}", id);
					Lieu lieu = lieuService.findOne(id);
					Evenementlieu evenementlieu = new Evenementlieu();
					evenementlieu.setEvenement(evenement);
					/*evenementlieu.setEvenementlieucode(evenementlieucode);
					evenementlieu.setEvenementlieulibelle(evenementlieulibelle);*/
					evenementlieu.setEvenementlieudatecreation(dateTools.getCurrentTimeStamp());
					evenementlieu.setLieu(lieu);
					evenementlieuService.create(evenementlieu);
					
				}
				log.debug("Enregistrement lieu OK");
				
				log.debug("Ajouter la liste des prix");
				for (String id : entity.getPrix()) {
					Prix prix = prixService.findOne(id);
					Evenementprix evenementprix = new Evenementprix();
					evenementprix.setEvenement(evenement);
					/*evenementprix.setEvenementprixcode(evenementprixcode);
					evenementprix.setEvenementprixlibelle(evenementprixlibelle);*/
					evenementprix.setEvenementprixdatecreation(dateTools.getCurrentTimeStamp());
					evenementprix.setPrix(prix);
					evenementprixService.create(evenementprix);
				}
				log.debug("Enregistrement prix OK");
	
				return new ResponseEntity<>(evenement, HttpStatus.CREATED);
				
			}else {
				log.debug("Evenement existe deja");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un evenement
	 * @return Evenement
	 */
	@ApiOperation(value = "Mettre à jour un evenement", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Evenement updated|OK"),
			@ApiResponse(code = 401, message = "Evenement non trouvé|OK"),
			@ApiResponse(code = 406, message = "Quantité incorrect"),
			@ApiResponse(code = 409, message = "Evenement existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Evenement> updateEvenement(@RequestBody EvenementDto entity) {

		try {
			log.debug("Debut de la modification d'un evenement: {}", entity.toString());
			
			log.debug("Verification des quantités");
			if(entity.getEvenementqteglobal() < entity.getEvenementqtemticket()) {
				log.debug("Quantité incorrect");
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

			}

			Evenement eventInDb = evenementService.findOne(entity.getEvenementid());
			if(eventInDb != null) {
				
				if(entity.getEvenementqtemticket() < eventInDb.getEvenementqteprise()) {
					log.debug("Quantité incorrect");
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

				}
					
				eventInDb.setEvenementdescription(entity.getEvenementdescription());
				eventInDb.setEvenementnom(entity.getEvenementnom());
				eventInDb.setEvenementqteglobal(entity.getEvenementqteglobal());
				eventInDb.setEvenementqtemticket(entity.getEvenementqtemticket());
				
				eventInDb = evenementService.update(eventInDb);
				log.debug("Evenement mis à jour");
				
				//Supprimer les lieux et prix existants
				// Rechercher la liste des evenementlieu associés
				List<Evenementlieu> evenementlieuDelete = evenementlieuService.findByEvenementId(eventInDb.getEvenementid());
				log.debug("Taille des elements evenementlieu à supprimer: {}", evenementlieuDelete.size());
				for (Evenementlieu iterateEvenementlieu : evenementlieuDelete) {
					evenementlieuService.delete(iterateEvenementlieu);

				}
				log.debug("Suppression des enfants(evenementlieu) dans la BD");
				
				// Rechercher la liste des evenementprix associés
				List<Evenementprix> evenementprixDelete = evenementprixService.findByEvenementId(eventInDb.getEvenementid());
				log.debug("Taille des elements evenementprix à supprimer: {}", evenementprixDelete.size());
				for (Evenementprix iterateEvenementprix : evenementprixDelete) {
					evenementprixService.delete(iterateEvenementprix);

				}
				log.debug("Suppression des enfants(evenementprix) dans la BD");
				
				// Ajouter les lieux et prix
				log.debug("Ajouter la liste des lieux");
				for (String id : entity.getLieu()) {
					log.debug("ID: {}", id);
					Lieu lieu = lieuService.findOne(id);
					Evenementlieu evenementlieu = new Evenementlieu();
					evenementlieu.setEvenement(eventInDb);
					/*evenementlieu.setEvenementlieucode(evenementlieucode);
					evenementlieu.setEvenementlieulibelle(evenementlieulibelle);*/
					evenementlieu.setEvenementlieudatecreation(dateTools.getCurrentTimeStamp());
					evenementlieu.setLieu(lieu);
					evenementlieuService.create(evenementlieu);
					
				}
				log.debug("Enregistrement lieu OK");
				
				log.debug("Ajouter la liste des prix");
				for (String id : entity.getPrix()) {
					Prix prix = prixService.findOne(id);
					Evenementprix evenementprix = new Evenementprix();
					evenementprix.setEvenement(eventInDb);
					/*evenementprix.setEvenementprixcode(evenementprixcode);
					evenementprix.setEvenementprixlibelle(evenementprixlibelle);*/
					evenementprix.setEvenementprixdatecreation(dateTools.getCurrentTimeStamp());
					evenementprix.setPrix(prix);
					evenementprixService.create(evenementprix);
				}
				log.debug("Enregistrement prix OK");
	
				return new ResponseEntity<>(eventInDb, HttpStatus.CREATED);
				
			}else {
				log.debug("Evenement n'existe pas en base");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}
	
	
	/**
	 * Fonction pour activer un evenement
	 * @return Evenement
	 */
	@ApiOperation(value = "Activer un evenement", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Evenement updated|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping("/active")
	public ResponseEntity<Evenement> activeEvenement(@RequestBody ActiveEvenementDto entity) {

		try {
			log.debug("Debut de la modification d'un evenement: {}", entity.toString());
			
			
			Evenement eventInDb = evenementService.findOne(entity.getEvenementid());
			if(eventInDb != null) {
				
				log.debug("Superviseur activé/desactivé");
				switch (entity.getStatutactive().toLowerCase()) {
				case "attente":
					log.debug("Statut attente");
					eventInDb.setEvenementstatut(env.getProperty("statut.attente"));
					break;
				case "actif":
					log.debug("Statut actif");
					eventInDb.setEvenementstatut(env.getProperty("statut.actif"));
					break;
					
				case "annuler":
					log.debug("Statut annuler");
					eventInDb.setEvenementstatut(env.getProperty("statut.annulation"));
					break;

				default:
					log.debug("Statut defaut");
					eventInDb.setEvenementstatut(env.getProperty("statut.attente"));
					break;
				}
				
				eventInDb = evenementService.update(eventInDb);
				log.debug("Evenement(statut) mis à jour");
	
				return new ResponseEntity<>(eventInDb, HttpStatus.CREATED);
				
			}else {
				log.debug("Evenement n'existe pas en base");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}
	
	
	/**
	 * Fonction pour supprimer un evenement
	 * @return Evenement
	 */
	@ApiOperation(value = "Supprimer un evenement", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evenement deleted|OK"),
			@ApiResponse(code = 204, message = "Evenement non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Evenement> deleteEvenements(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour d'un evenement id: {}", id);
			Evenement evenement = evenementService.findOne(id);
			if(evenement == null) {
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				// Rechercher la liste des evenementlieu associés
				List<Evenementlieu> evenementlieu = evenementlieuService.findByEvenementId(id);
				log.debug("Taille des elements evenementlieu à supprimer: {}", evenementlieu.size());
				for (Evenementlieu iterateEvenementlieu : evenementlieu) {
					evenementlieuService.delete(iterateEvenementlieu);

				}
				log.debug("Suppression des enfants(evenementlieu) dans la BD");
				
				// Rechercher la liste des evenementprix associés
				List<Evenementprix> evenementprix = evenementprixService.findByEvenementId(id);
				log.debug("Taille des elements evenementprix à supprimer: {}", evenementprix.size());
				for (Evenementprix iterateEvenementprix : evenementprix) {
					evenementprixService.delete(iterateEvenementprix);

				}
				log.debug("Suppression des enfants(evenementprix) dans la BD");

				evenementService.delete(evenement);
				log.debug("Evenement supprimé avec succes");

				return new ResponseEntity<>(evenement, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un evenement,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction to upload image event
	 * @param evenementid
	 * @param file
	 * @return
	 */
	@PostMapping("/uploadpicture")
	public ResponseEntity<Evenement> uploadFile(@RequestParam("evenementid") String evenementid,
			@RequestParam MultipartFile file) {
		try {

			log.debug("setting image to evenement,{}", evenementid);

			// checking if requirement parameters are set
			if (evenementService.findOne(evenementid) != null) {

				// all data are OK for saving
				// setting other optional data
				

				Evenement entity = evenementService.findOne(evenementid);

				log.trace(" ------- Loading entity ---------------------------------     " + entity.getEvenementid());

				if (entity.getEvenementlogo() != null) {
					log.trace("ckecking and deleting image file {},{} if exist", path, entity.getEvenementlogo());
					File f = new File(path + entity.getEvenementlogo());
					boolean result = Files.deleteIfExists(f.toPath());

					log.trace("ckecking and deleting image file {} if exist {} ", entity.getEvenementlogo(), result);

				}

				log.trace(" +++++++++++++++ File to load ++++++++++++++++++++++++++++");

				fileService.uploadFile(file, "event");

				entity.setEvenementpathimage(baseUrl + evenementid);
				entity.setEvenementlogo(file.getOriginalFilename());

				entity = evenementService.update(entity);
				//fileService.uploadFile(file);

				log.debug("success added image to Evenement:{}", entity.getEvenementnom());
				return new ResponseEntity<>(entity, HttpStatus.CREATED);
			} else {

				log.error("object with not found ,{}", evenementid);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			log.debug("error while adding image to Evenement,{}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	/**
	 * Fonction to download image evenement
	 * @param evenementid
	 * @param request
	 * @return
	 */
	@GetMapping("/downloadpicture/{evenementid}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String evenementid, HttpServletRequest request) {
		try {
			// Load file as Resource
			Evenement entity = evenementService.findOne(evenementid);

			if (entity != null) {

				File f = new File(entity.getEvenementlogo().trim());

				FileInputStream aa = new FileInputStream(path + entity.getEvenementlogo().trim());
				log.debug("loading file to path,{}{} ", path, entity.getEvenementlogo());

				final InputStream targetStream = new DataInputStream(aa);
				InputStreamResource resource = new InputStreamResource(targetStream);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.getName() + "\"");

				return new ResponseEntity<>(resource, headers, HttpStatus.OK);

			} else {

				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			// Try to determine file's content type

		} catch (Exception e) {
			log.error(" error while downloading file, {}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}





}
