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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.StatsGlobalDto;
import com.wizard.monticketci.dto.StatsGlobalSupDto;
import com.wizard.monticketci.dto.StatsVenteSupDto;
import com.wizard.monticketci.entities.Evenement;
import com.wizard.monticketci.entities.Reservation;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.CaissierService;
import com.wizard.monticketci.service.EvenementService;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.service.ReservationService;
import com.wizard.monticketci.service.SuperviseurService;
import com.wizard.monticketci.service.UtilisateurService;
import com.wizard.monticketci.service.VilleService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 09 June,21
 */

@Api(value = "Gestion des statistiques")
@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatistiqueController {
	
	@Log
	private Logger log;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	UtilisateurService utilisateurService;
	
	@Autowired
	ReservationService reservationService;
	
	@Autowired
	EvenementService evenementService;
	
	@Autowired
	LocalisationService localisationService;
	
	@Autowired
	VilleService villeService;
	
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
	 * Fonction de stats globales admin (count)
	 * @return 
	 */
	@ApiOperation(value = "Afficher stats globales admin(count)", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Stats trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun stat trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/admin/global")
	public ResponseEntity<StatsGlobalDto> getStatsGlobalAdmin() {

		try {
			log.debug("Afficher les stats globales");
			
			StatsGlobalDto statsGlobalDto = new StatsGlobalDto();
			statsGlobalDto.setNombreUtilisateur(utilisateurService.count());
			statsGlobalDto.setNombreAdmin(adminService.count());
			statsGlobalDto.setNombreCaissier(caissierService.count());
			statsGlobalDto.setNombreSuperviseur(superviseurService.count());

			statsGlobalDto.setNombreEvenement(evenementService.count());
			statsGlobalDto.setNombreLocalisation(localisationService.count());
			statsGlobalDto.setNombreVille(villeService.count());
			statsGlobalDto.setNombreReservation(reservationService.count());

			
			log.debug("Stats trouvé: {}", statsGlobalDto);
			return new ResponseEntity<>(statsGlobalDto, HttpStatus.OK);
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des stats,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction de stats globales superviseur(count)
	 * @return 
	 */
	@ApiOperation(value = "Afficher stats globales superviseur(count)", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Stats trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun stat trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/superviseur/global/{superviseurid}")
	public ResponseEntity<StatsGlobalSupDto> getStatsGlobalSuperviseur(@PathVariable("superviseurid") String superviseurid) {

		try {
			log.debug("Afficher les stats globales");
			
			StatsGlobalSupDto statsGlobalSupDto = new StatsGlobalSupDto();
			statsGlobalSupDto.setNombreScanneur(caissierService.countBySuperviseur(superviseurid));
			statsGlobalSupDto.setNombreEvenement(evenementService.countBySuperviseur(superviseurid));
			
			log.debug("Stats trouvé: {}", statsGlobalSupDto);
			return new ResponseEntity<>(statsGlobalSupDto, HttpStatus.OK);
			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des stats,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher stats de participation par evenementid
	 * @return 
	 */
	@ApiOperation(value = "Afficher stats de participation par evenementid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Stats trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun stat trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/superviseur/statsglobalparticipation/{evenementid}")
	public ResponseEntity<StatsVenteSupDto> getStatsByEvent(@PathVariable("evenementid") String evenementid) {

		try {
			log.debug("Afficher les stats participation par evenement, evenementid: {}", evenementid);

			List<Reservation> reservation = reservationService.findByEvenementidAndStatut(evenementid, "SUCCES");
			List<Reservation> reservationChecked = reservationService.findByEvenementidAndStatutAndChecked(evenementid, "SUCCES", true);
			StatsVenteSupDto statsVenteSupDto = new StatsVenteSupDto();
			

			if(reservation != null) {
				statsVenteSupDto.setNombreScanner(reservationChecked.size());
				statsVenteSupDto.setNombreVentetotal(reservation.size());
				
				log.debug("Stats trouvé: {}", statsVenteSupDto);
				return new ResponseEntity<>(statsVenteSupDto, HttpStatus.OK);
			}else {
				statsVenteSupDto.setNombreScanner(0);
				statsVenteSupDto.setNombreVentetotal(0);
				log.debug("Aucune reservation trouvé");
				return new ResponseEntity<>(statsVenteSupDto, HttpStatus.NO_CONTENT);
				
			}

			

		} catch (Exception e) {

			log.error("erreur lors de l'affichage des stats,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Fonction pour afficher stats de vente par evenementid
	 * @return 
	 */
	@ApiOperation(value = "Afficher stats de vente par evenementid", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Stats trouvé|OK"),
			@ApiResponse(code = 204, message = "Aucun stat trouvé|OK"),
			@ApiResponse(code = 500, message = "Internal server error"),
	})
	@GetMapping("/superviseur/statsvente/{evenementid}")
	public ResponseEntity<Evenement> getStatsVenteByEvent(@PathVariable("evenementid") String evenementid) {

		try {
			log.debug("Afficher les stats de vente par evenement, evenementid: {}", evenementid);

			Evenement event = evenementService.findOne(evenementid);
			if(event != null) {
				
				log.debug("Event trouvé: {}", event);
				return new ResponseEntity<>(event, HttpStatus.OK);
			}else {
				log.debug("Aucun Evenement trouvé");
				return new ResponseEntity<>(event, HttpStatus.NO_CONTENT);
				
			}


		} catch (Exception e) {

			log.error("erreur lors de l'affichage des stats,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
