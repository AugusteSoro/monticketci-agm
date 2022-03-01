package com.wizard.monticketci.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.entities.Evenement;
import com.wizard.monticketci.entities.PaymentNotify;
import com.wizard.monticketci.entities.Reservation;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.EvenementService;
import com.wizard.monticketci.service.ReservationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 25 June,21
 */

@Api(value = "IPN cinetpay")
@RestController
@RequestMapping("/ipn")
@CrossOrigin(origins = "*", maxAge = 3600)
public class IPNController {
	
	@Log
	private Logger log;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private EvenementService evenementService;
	
	@Autowired
	Environment env;
	
	/**
	 * Recevoir l'IPN de cinetpay (test)
	 * @return Reservation
	 */
	@ApiOperation(value = "Recevoir l'IPN de cinetpay (test)", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "IPN recu|OK"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<String> IPN() {

		try {
			log.debug("Debut de l'execution de l'IPN");
			
			
			log.debug("IPN executé avec succes");

			return new ResponseEntity<>(HttpStatus.OK);
			

		} catch (Exception e) {

			log.error("erreur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	/**
	 * Recevoir l'IPN de cinetpay
	 * @return Reservation
	 */
	@ApiOperation(value = "Recevoir l'IPN de cinetpay", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "IPN recu|OK"),
			@ApiResponse(code = 204, message = "Transaction non trouvé en base"),
			@ApiResponse(code = 401, message = "Commande deja traité"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@RequestMapping(path="payment", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	public ResponseEntity<String> IPNPost(PaymentNotify paymentNotify) {

		try {
			log.debug("Debut de la reception de l'IPN");
			log.debug("IPN reçu: {}", paymentNotify);

			//TODO verification du paiement et mise à jour
			
			/**
			 * 1- Verifier si le paiement existe en base
			 * 2- Verifier si le paiement est pas deja effectué
			 * 3- Mettre à jour le paiement (statut...)
			 */

			log.debug("Recherche de la reservation, reference:{}", paymentNotify.get_cpm_trans_id());
			Reservation reservation = reservationService.findByReservationcodereference(paymentNotify.get_cpm_trans_id());
			if(reservation != null) {
				log.debug("Reservation trouvé, reference:{}; montant:{}; statut:{}", paymentNotify.get_cpm_trans_id(), paymentNotify.get_cpm_amount(), paymentNotify.get_cpm_trans_status());

				if(!reservation.getReservationpaiementstatut().equals(env.getProperty("transaction.succes"))) {
					
					reservation.setReseervationcommentaire(paymentNotify.toString());
					reservation.setReservationwallet(paymentNotify.get_payment_method());
					reservation.setReservationdevise(paymentNotify.get_cpm_currency()); // Mettre à jour la devise avec celle venant du moyen de paiement
					if(paymentNotify.get_cpm_result().equals("00")) {
						reservation.setReservationpaiementstatut(env.getProperty("transaction.succes"));
						log.debug("Transaction reussi");
						log.debug("Mise a jour quantité, recherche de l'evenement");
						Evenement event = evenementService.findOne(reservation.getResevenementid());
						if(event != null) {
							log.debug("Evenement trouvé");
							event.setEvenementqteprise(event.getEvenementqteprise() + 1);
							event = evenementService.update(event);
							log.debug("Quantité actualisée, nouvelle qté commandé pour ce event: {}/{}", event.getEvenementqteprise(), event.getEvenementqtemticket());

						}else {
							log.debug("Evenement non trouvé, continue");

						}
						
					}else {
						reservation.setReservationpaiementstatut(env.getProperty("transaction.echec"));
						log.debug("Transaction echoué");
					}
					reservation = reservationService.create(reservation);
					log.debug("Statut mis à jour");
					
				}else {
					
					log.debug("Commande deja traité");
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				log.debug("IPN executé avec succes");

				
				return new ResponseEntity<>(HttpStatus.OK);

				
				
			}else {
				
				log.debug("Transaction non trouvé en base");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			

		} catch (Exception e) {

			log.error("erreur,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
