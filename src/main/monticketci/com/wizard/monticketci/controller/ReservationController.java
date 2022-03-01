package com.wizard.monticketci.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.CheckTicketDto;
import com.wizard.monticketci.dto.ReservationDto;
import com.wizard.monticketci.entities.Caissier;
import com.wizard.monticketci.entities.Evenement;
import com.wizard.monticketci.entities.Lieu;
import com.wizard.monticketci.entities.Parameter;
import com.wizard.monticketci.entities.Reservation;
import com.wizard.monticketci.entities.Utilisateur;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.AdminService;
import com.wizard.monticketci.service.CaissierService;
import com.wizard.monticketci.service.EvenementService;
import com.wizard.monticketci.service.LieuService;
import com.wizard.monticketci.service.LocalisationService;
import com.wizard.monticketci.service.ParameterLogic;
import com.wizard.monticketci.service.ReservationService;
import com.wizard.monticketci.service.SuperviseurService;
import com.wizard.monticketci.service.UtilisateurService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 09 June,21
 */

@Api(value = "Gestion des reservations")
@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {
	
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
	ReservationService reservationService;
	
	@Autowired
	UtilisateurService utilisateurService;
	
	@Autowired
	EvenementService evenementService;
	
	@Autowired
	LocalisationService localisationService;
	
	@Autowired
	SuperviseurService superviseurService;
	
	@Autowired
	CaissierService caissierService;
	
	@Autowired
	LieuService lieuService;
	
	@Autowired
	ParameterLogic paramlogic;
	
	
	/**
	 * Fonction pour afficher les reservations
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher la liste des reservations", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<Reservation>> ObtainReservations() {

		try {
			log.debug("Debut de l'affichage des reservations");
			List<Reservation> reservation = reservationService.findAll();
			if(reservation.size() > 0) {
				
				log.debug("Reservation obtenu taille: {}", reservation.size());

				return new ResponseEntity<>(reservation, HttpStatus.OK);

			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}	

	/**
	 * Fonction pour afficher une reservation par utilisateur ID
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher une reservation par utilisateur ID", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/utilisateur/{utilisateurid}")
	public ResponseEntity<List<Reservation>> ObtainReservationByUtilisateurId(@PathVariable("utilisateurid") String utilisateurid) {

		try {
			log.debug("Debut de l'affichage des reservations, utilisateur ID:{}", utilisateurid);
			List<Reservation> reservation = reservationService.findByUtilisateurid(utilisateurid);
			if(reservation.size() > 0) {
				
				log.debug("Reservation obtenu taille: {}", reservation.size());

				return new ResponseEntity<>(reservation, HttpStatus.OK);

			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher une reservation par evenement ID
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher une reservation par evenement ID", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/byevent/{evenementid}")
	public ResponseEntity<List<Reservation>> ObtainReservationByEvenementId(@PathVariable("evenementid") String evenementid) {

		try {
			log.debug("Debut de l'affichage des reservations, evenement ID:{}", evenementid);
			List<Reservation> reservation = reservationService.findByEvenementid(evenementid);
			if(reservation.size() > 0) {
				
				log.debug("Reservation obtenu taille: {}", reservation.size());

				return new ResponseEntity<>(reservation, HttpStatus.OK);

			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	/**
	 * Fonction pour afficher une reservation par reference ticket (reservationcodereference) et marquer la commande comme scanner
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher une reservation par reference ticket (reservationcodereference) et marquer la commande comme scanner", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 401, message = "Compte scanner non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping("/checkticket")
	public ResponseEntity<Reservation> ObtainReservationByReservationcodereference(@RequestBody CheckTicketDto checkTicketDto) {

		try {
			log.debug("Recherche du superviseur");
			Caissier caissier = new Caissier();
			caissier = caissierService.findOne(checkTicketDto.getCaissierid());
			if(caissierService == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			log.debug("Caissier trouvé, email: {}", caissier.getCaissieremail());
			
			log.debug("Debut de l'affichage de la reservation, reservationcodereference:{}", checkTicketDto.getReservationcodereference());
			Reservation reservation = reservationService.findByReservationcodereference(checkTicketDto.getReservationcodereference());
			if(reservation != null) {
				
				log.debug("Verifier si le compte à le droit de valider le ticket");
				if(reservation.getResevenementid().equals(checkTicketDto.getEvenementid())) {
					log.debug("Compte autorisé à valider le ticket");

				}else {
					log.debug("Compte non autorisé à valider le ticket");
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

				}
				
				log.debug("Reservation obtenu, telephone: {}, prix: {}", reservation.getUtilisateur().getUtilisateurtelephone(), reservation.getReservationprixtotal());
				
				log.debug("Verifier si le ticket à deja été scanner, Scanner: {}", reservation.isResticketischecked());
				if(reservation.isResticketischecked()) {
					log.debug("Le ticket est deja scanner");
					return new ResponseEntity<>(reservation, HttpStatus.CREATED);

				}else {
					//Mise a jour de la lecture (Valider le ticket comme lu)
					reservation.setResticketischecked(true);
					reservation.setReservationcheckeddate(dateTools.getCurrentTimeStamp());
					reservation.setReservationscannerid(caissier.getCaissierid());
					reservation = reservationService.update(reservation);
					return new ResponseEntity<>(reservation, HttpStatus.OK);

				}
				
			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher une reservation par reference ticket (reservationcodereference)
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher une reservation par reference ticket (reservationcodereference)", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/notifticket/{reservationcodereference}")
	public ResponseEntity<Reservation> CheckReservationByReservationcodereference(@PathVariable("reservationcodereference") String reservationcodereference) {

		try {
			
			log.debug("Debut fonction verification de scan ticket");
			log.debug("Debut de l'affichage de la reservation, reservationcodereference:{}", reservationcodereference);
			Reservation reservation = reservationService.findByReservationcodereference(reservationcodereference);
			if(reservation != null) {
				
				log.debug("Reservation obtenu, telephone: {}, prix: {}, isResticketischecked: {}", reservation.getUtilisateur().getUtilisateurtelephone(), reservation.getReservationprixtotal(), reservation.isResticketischecked());
				return new ResponseEntity<>(reservation, HttpStatus.OK);

				
			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour afficher une reservation par utilisateur ID et type
	 * @return Reservation
	 */
	@ApiOperation(value = "Afficher une reservation par utilisateur ID et type", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 401, message = "Type non reconnu"),
			@ApiResponse(code = 406, message = "User not found"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/byuserandtype/{utilisateurid}/{type}")
	public ResponseEntity<List<Reservation>> ObtainReservationByUtilisateurIdEtDesttel(@PathVariable("utilisateurid") String utilisateurid, @PathVariable("type") String type) {

		try {
			log.debug("Debut de l'affichage des reservations, utilisateur ID:{}, type:{}", utilisateurid, type);
			List<Reservation> reservation = new ArrayList<>();
			log.debug("recherche du client");
			Utilisateur user = new Utilisateur();
			user = utilisateurService.findOne(utilisateurid);
			if(user == null) {
				log.debug("Utilisateur non trouvé, ID:{}", utilisateurid);
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}

			switch (type) {
			case "personnel":
				reservation = reservationService.findByUtilisateuridAndDesttelisNull(utilisateurid);
				break;
			case "offert":
				reservation = reservationService.findByUtilisateuridAndDesttelisNotNull(utilisateurid);
				break;
			case "recu":
				reservation = reservationService.findByResdestinatairetel(user.getUtilisateurtelephone());
				break;

			default:
				log.debug("Type inconnu");
				return new ResponseEntity<>(reservation, HttpStatus.UNAUTHORIZED);
			}
			
			if(reservation.size() > 0) {
				
				log.debug("Reservation obtenu taille: {}", reservation.size());

				return new ResponseEntity<>(reservation, HttpStatus.OK);

			}else {
				
				log.debug("Reservation non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				
			}
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Fonction pour compter le nombre de reservation par utilisateur ID et type
	 * @return Reservation
	 */
	@ApiOperation(value = "Fonction pour compter le nombre de reservation par utilisateur ID et type", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Reservation trouvé|OK"),
			@ApiResponse(code = 204, message = "Reservation non trouvé"),
			@ApiResponse(code = 401, message = "Type non reconnu"),
			@ApiResponse(code = 406, message = "User not found"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping("/countbyuserandtype/{utilisateurid}/{type}")
	public ResponseEntity<Long> CountReservationByUtilisateurIdEtDesttel(@PathVariable("utilisateurid") String utilisateurid, @PathVariable("type") String type) {

		try {
			log.debug("Debut du compte des reservations, utilisateur ID:{}, type:{}", utilisateurid, type);
			// List<Reservation> reservation = new ArrayList<>();
			long nombreReservation;
			log.debug("recherche du client");
			Utilisateur user = new Utilisateur();
			user = utilisateurService.findOne(utilisateurid);
			if(user == null) {
				log.debug("Utilisateur non trouvé, ID:{}", utilisateurid);
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}

			switch (type) {
			case "personnel":
				nombreReservation = reservationService.countByUtilisateuridAndDesttelisNullAndStatutSucces(utilisateurid);
				//nombreReservation = reservationService.countByUtilisateuridAndDesttelisNull(utilisateurid);

				break;
				
			case "offert":
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			
			case "recu":
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				
			default:
				log.debug("Type inconnu");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			log.debug("Nombre de reservation: {}", nombreReservation);
			return new ResponseEntity<>(nombreReservation, HttpStatus.OK);
			

		} catch (Exception e) {

			log.error("erreur lors de l'obtention de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	/**
	 * Fonction pour enregistrer une reservation
	 * @return Reservation
	 */
	@ApiOperation(value = "Enregistrer une reservation", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Reservation crée|OK"),
			@ApiResponse(code = 401, message = "Utilisateur / reservation / localisation non trouvé"),
			@ApiResponse(code = 406, message = "Quantité indisponible"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<Reservation> postReservations(@RequestBody ReservationDto entity) {

		try {
			log.debug("Debut de la creation d'une reservation: {}", entity.toString());
			
			log.debug("Recherche de l'utilisateur par id: {}", entity.getUtilisateurid());
			Utilisateur user = utilisateurService.findOne(entity.getUtilisateurid());
			if(user == null) {
				log.debug("Utilisateur non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			log.debug("Recherche de l'evenement par id: {}", entity.getResevenementid());
			Evenement event = evenementService.findOne(entity.getResevenementid());
			if(event == null) {
				log.debug("Evenement non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}else {
				log.debug("Verification des quantités");
				if((event.getEvenementqteprise() + 1) > event.getEvenementqtemticket()) {
					log.debug("Quantité incorrect");
					return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

				}
			}
			
			log.debug("Recherche de le lieu par id: {}", entity.getReslieuid());
			Lieu lieu = lieuService.findOne(entity.getReslieuid());
			if(lieu == null) {
				log.debug("Lieu non trouvé dans la BD");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}
			
			int prixPanier = entity.getReservationprix();
			log.debug("Montant ticket: {}", prixPanier);

			List<Parameter> parameters = paramlogic.findByType("FRAIS".toUpperCase());
			
			float montantFrais = 0;
			float fraisObtenu = 0;
			for (Parameter parameter : parameters) {
				fraisObtenu = (prixPanier * parameter.getValue()) / 100;
				log.debug("Montant fraisObtenu iterate: {}", fraisObtenu);
				montantFrais += Math.round(fraisObtenu * 100.0) / 100.0;
				log.debug("Montant frais iterate: {}", montantFrais);
				//montantFrais += Precision.round(((prixPanier * parameter.getValue()) / 100), 2);
			}
			log.debug("Montant frais: {}", montantFrais);

			//Prix total
			float prixTotal = prixPanier + montantFrais;
			log.debug("Montant total (frais + prix ticket): {}", prixTotal);

			Reservation reservation = new Reservation();
				
			reservation.setUtilisateur(user);
			reservation.setReservationcodereference(dateTools.generateTransactionIdonTime());
			reservation.setResevenementid(entity.getResevenementid());
			reservation.setResevenementnom(event.getEvenementnom());
			reservation.setReservationpaiementmode(env.getProperty("paiement.type.mobilemoney"));
			reservation.setReservationpaiementstatut(env.getProperty("transaction.attente"));
			reservation.setReservationtypetransaction(env.getProperty("typetransaction.debit"));
			// Renseigner devise en fonction du pays du client
			//reservation.setReservationdevise(env.getProperty("devise.cfa"));
			reservation.setReservationdevise(user.getLocalisation().getDevise().getDevisecode());
			reservation.setReservationprix(entity.getReservationprix());
			reservation.setReservationremise(0);
			reservation.setReservationfrais(montantFrais);
			reservation.setReservationprixtotal(prixTotal);
			reservation.setReslieuid(lieu.getLieuid());
			reservation.setReslieunom(lieu.getLieunom());
			reservation.setResdestinatairenom(entity.getResdestinatairenom());
			reservation.setResdestinatairetel(entity.getResdestinatairetel());
			reservation.setReservationdatecreation(dateTools.getCurrentTimeStamp());
			reservation.setResticketischecked(false);

			// Infos à obtenir avec Mobile money
			/*reservation.setReservationwallet(reservationwallet);
			reservation.setReseervationcommentaire(reseervationcommentaire);*/
			
			
			reservation = reservationService.create(reservation);
			log.debug("Reservation crée avec succes");

			return new ResponseEntity<>(reservation, HttpStatus.CREATED);
			

		} catch (Exception e) {

			log.error("erreur lors de la creation de la reservation,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	

}
