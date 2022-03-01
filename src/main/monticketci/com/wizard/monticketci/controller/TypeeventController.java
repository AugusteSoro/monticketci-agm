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

import com.wizard.monticketci.dto.ActiveDto;
import com.wizard.monticketci.dto.TypeeventDto;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.entities.Typeevent;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.FileService;
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

@Api(value = "Gestion des types evenements")
@RestController
@RequestMapping("/typeevent")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TypeeventController {
	
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
	TypeeventService typeeventService;
	
	@Autowired
	FileService fileService;
	
	@Value("${typeevent.upload.baseurl}")
	private String baseUrl;
	
	@Value("${typeevent.upload.path}")
	private String path;
	
	
	/**
	 * Fonction pour afficher un typeevent
	 * @return Typeevent
	 */
	@ApiOperation(value = "Afficher un typeevent", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Typeevent trouvé|OK"),
			@ApiResponse(code = 204, message = "Typeevent non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Typeevent>> ObtainTypeevents() {

		try {
			log.debug("Debut de l'affichage des typeevents");
			List<Typeevent> typeevent = typeeventService.findAll();
			if(typeevent.size() > 0) {
				
				log.debug("Typeevent obtenu taille: {}", typeevent.size());

				return new ResponseEntity<>(typeevent, HttpStatus.OK);

			}else {
				
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}	
	
	
	/**
	 * Fonction pour afficher un typeevent par statut
	 * @return Typeevent
	 */
	@ApiOperation(value = "Afficher un typeevent par statut", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Typeevent trouvé|OK"),
			@ApiResponse(code = 204, message = "Typeevent non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bystatut/{statut}")
	public ResponseEntity<List<Typeevent>> ObtainTypeeventsByStatut(@PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des typeevents, statut: {}", statut);
			List<Typeevent> typeevent = typeeventService.findAllByStatut(statut);
			if(typeevent.size() > 0) {
				
				log.debug("Typeevent obtenu taille: {}", typeevent.size());

				return new ResponseEntity<>(typeevent, HttpStatus.OK);

			}else {
				
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}	

	/**
	 * Fonction pour afficher un typeevent par superviseurid
	 * @return Typeevent
	 */
	@ApiOperation(value = "Afficher un typeevent par superviseurid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Typeevent trouvé|OK"),
			@ApiResponse(code = 204, message = "Typeevent non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}")
	public ResponseEntity<List<Typeevent>> ObtainTypeeventsBySuperviseurId(@PathVariable("id") String id) {

		try {
			log.debug("Debut de l'affichage des typeevents, id:{}", id);
			List<Typeevent> typeevent = typeeventService.findTypeeventsBySuperviseurId(id);
			if(typeevent.size() > 0) {
				
				log.debug("Typeevent obtenu taille: {}", typeevent.size());

				return new ResponseEntity<>(typeevent, HttpStatus.OK);

			}else {
				
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher un typeevent par superviseurid et par statut
	 * @return Typeevent
	 */
	@ApiOperation(value = "Afficher un typeevent par superviseurid et par statut", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Typeevent trouvé|OK"),
			@ApiResponse(code = 204, message = "Typeevent non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/bysuperviseurid/{id}/{statut}")
	public ResponseEntity<List<Typeevent>> ObtainTypeeventsBySuperviseurIdAndStatut(@PathVariable("id") String id, @PathVariable("statut") String statut) {

		try {
			log.debug("Debut de l'affichage des typeevents, id:{}, statut:{}", id, statut);
			List<Typeevent> typeevent = typeeventService.findTypeeventsBySuperviseurIdAndStatut(id, statut);
			if(typeevent.size() > 0) {
				
				log.debug("Typeevent obtenu taille: {}", typeevent.size());

				return new ResponseEntity<>(typeevent, HttpStatus.OK);

			}else {
				
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer un typeevent
	 * @return Typeevent
	 */
	@ApiOperation(value = "Enregistrer un typeevent", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Typeevent crée|OK"),
			@ApiResponse(code = 401, message = "Admin non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Typeevent> postTypeevents(@RequestBody TypeeventDto entity) {

		try {
			log.debug("Debut de la creation d'un typeevent: {}", entity.toString());
			
			log.debug("Recherche de l'admin par id: {}", entity.getSuperviseurid());
			Superviseur superviseur = superviseurService.findOne(entity.getSuperviseurid());
			if(superviseur == null) {
				log.debug("Superviseur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}

			Typeevent typeevent = new Typeevent();
				
			typeevent.setSuperviseur(superviseur);
			typeevent.setTypeeventdescritpion(entity.getTypeeventdescritpion());			
			typeevent.setTypeeventicon(entity.getTypeeventicon());
			typeevent.setTypeeventenable(true);
			typeevent.setTypeeventnom(entity.getTypeeventnom());
			if(entity.isTypeeventisactive()) {
				typeevent.setTypeeventstatut(env.getProperty("statut.actif"));

			}else {
				typeevent.setTypeeventstatut(env.getProperty("statut.attente"));

			}
			typeevent.setTypeeventdatecreation(dateTools.getCurrentTimeStamp());
			
			typeevent = typeeventService.create(typeevent);
			log.debug("Typeevent crée avec succes");

			return new ResponseEntity<>(typeevent, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour activer/desactiver une categorie(typeevent)
	 * @return Typeevent
	 */
	@ApiOperation(value = "activer/desactiver une categorie(typeevent)", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Typeevent mis à jour|OK"),
			@ApiResponse(code = 204, message = "Typeevent introuvable"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping("/active")
	public ResponseEntity<Typeevent> activeTypeevents(@RequestBody ActiveDto entity) {

		try {
			log.debug("Debut de l'activation/desactivation obj: {}", entity.toString());
			Typeevent typeevent = new Typeevent();
			typeevent = typeeventService.findOne(entity.getId());
			if(typeevent != null) {
				
				log.debug("Typeevent activé/desactivé");
				switch (entity.getStatutactive().toLowerCase()) {
				case "attente":
					log.debug("Statut attente");
					typeevent.setTypeeventstatut(env.getProperty("statut.attente"));
					break;
				case "actif":
					log.debug("Statut actif");
					typeevent.setTypeeventstatut(env.getProperty("statut.actif"));
					break;
					
				case "annuler":
					log.debug("Statut annuler");
					typeevent.setTypeeventstatut(env.getProperty("statut.annulation"));
					break;

				default:
					log.debug("Statut defaut");
					typeevent.setTypeeventstatut(env.getProperty("statut.attente"));
					break;
				}
				
				typeevent = typeeventService.update(typeevent);
				log.debug("Evenement(statut) mis à jour");

				return new ResponseEntity<>(typeevent, HttpStatus.CREATED);
			}else {
				log.debug("Typeevent non trouvé");
				return new ResponseEntity<>(typeevent, HttpStatus.NO_CONTENT);

			}

		} catch (Exception e) {

			log.error("erreur lors de la creation de typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * Fonction pour mettre à jour un typeevent
	 * @return Typeevent
	 */
	@ApiOperation(value = "Mettre à jour un typeevent", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Typeevent updated|OK"),
			@ApiResponse(code = 401, message = "Typeevent non trouvé|OK"),
			@ApiResponse(code = 409, message = "Email existe deja|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PutMapping
	public ResponseEntity<Typeevent> updateTypeevents(@RequestBody Typeevent entity) {

		try {
			log.debug("Debut de la mise à jour d'un typeevent: {}", entity.toString());
			Typeevent typeevent = typeeventService.findOne(entity.getTypeeventid());
			if(typeevent == null) {
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			Typeevent local = typeeventService.findByName(entity.getTypeeventnom());
			if(local != null && local.getTypeeventnom() != typeevent.getTypeeventnom() ) {
				log.debug("Nom existe deja dans la BD");
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
				
			//TODO: Check if email already exist
			typeevent.setTypeeventnom(entity.getTypeeventnom());
			typeevent.setTypeeventdescritpion(entity.getTypeeventdescritpion());			
			typeevent.setTypeeventicon(entity.getTypeeventicon());
			
			typeevent = typeeventService.update(typeevent);
			log.debug("Typeevent mis à jour avec succes");

			return new ResponseEntity<>(typeevent, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour supprimer un typeevent
	 * @return Typeevent
	 */
	@ApiOperation(value = "Supprimer un typeevent", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Typeevent deleted|OK"),
			@ApiResponse(code = 204, message = "Typeevent non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Typeevent> deleteTypeevents(@PathVariable String id) {

		try {
			log.debug("Debut de la mise à jour d'un typeevent id: {}", id);
			Typeevent typeevent = typeeventService.findOne(id);
			if(typeevent == null) {
				log.debug("Typeevent non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}else {
				
				typeeventService.delete(typeevent);
				log.debug("Typeevent mis à jour avec succes");

				return new ResponseEntity<>(typeevent, HttpStatus.OK);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de la creation d'un typeevent,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction to upload image typeevent
	 * @param typeeventid
	 * @param file
	 * @return
	 */
	@PostMapping("/uploadpicture")
	public ResponseEntity<Typeevent> uploadFile(@RequestParam("typeeventid") String typeeventid,
			@RequestParam MultipartFile file) {
		try {

			log.debug("setting image to typeevent,{}", typeeventid);

			// checking if requirement parameters are set
			if (typeeventService.findOne(typeeventid) != null) {

				// all data are OK for saving
				// setting other optional data
				

				Typeevent entity = typeeventService.findOne(typeeventid);

				log.trace(" ------- Loading entity ---------------------------------     " + entity.getTypeeventid());

				if (entity.getTypeeventlogo() != null) {
					log.trace("ckecking and deleting image file {},{} if exist", path, entity.getTypeeventlogo());
					File f = new File(path + entity.getTypeeventlogo());
					boolean result = Files.deleteIfExists(f.toPath());

					log.trace("ckecking and deleting image file {} if exist {} ", entity.getTypeeventlogo(), result);

				}

				log.trace(" +++++++++++++++ File to load ++++++++++++++++++++++++++++");

				fileService.uploadFile(file, "typeevent");

				entity.setTypeeventpathimage(baseUrl + typeeventid);
				entity.setTypeeventlogo(file.getOriginalFilename());

				entity = typeeventService.update(entity);
				//fileService.uploadFile(file);

				log.debug("success added image to Evenement:{}", entity.getTypeeventnom());
				return new ResponseEntity<>(entity, HttpStatus.CREATED);
			} else {

				log.error("object with not found ,{}", typeeventid);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			log.debug("error while adding image to Evenement,{}", e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
	
	/**
	 * Fonction to download image typeevent
	 * @param typeeventid
	 * @param request
	 * @return
	 */
	@GetMapping("/downloadpicture/{typeeventid}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String typeeventid, HttpServletRequest request) {
		try {
			// Load file as Resource
			Typeevent entity = typeeventService.findOne(typeeventid);

			if (entity != null) {

				File f = new File(entity.getTypeeventlogo().trim());

				FileInputStream aa = new FileInputStream(path + entity.getTypeeventlogo().trim());
				log.debug("loading file to path,{}{} ", path, entity.getTypeeventlogo());

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
