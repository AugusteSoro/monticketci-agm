package com.wizard.monticketci.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.config.MailConfig;
import com.wizard.monticketci.dto.SendMail;
import com.wizard.monticketci.entities.Caissier;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.EmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author augustesoro
 * @create 23 sept,21
 */

@Api(value = "Gestion des emails")
@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmailController {
	
	@Log
	private Logger log;
	
	@Autowired
	MailConfig mailconfig;
	
	@Autowired
	EmailService emailservice;
	
	
	/**
	 * Fonction pour envoyer un email
	 * @return 
	 */
	@ApiOperation(value = "Envoyer un email", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Email envoyé|OK"),
			@ApiResponse(code = 204, message = "Infos incorrect"),
			@ApiResponse(code = 401, message = "Envoi echoué"),
			@ApiResponse(code = 500, message = "Internal server error")
	})
	@PostMapping
	public ResponseEntity<List<Caissier>> postEmail(@RequestBody SendMail sendmail) {

		try {
			log.debug("Debut de l'envoie du mail, objet: {}", sendmail.toString());
			
			Boolean result = emailservice.sendEmail(sendmail);

			if(!result) {
				log.debug("Email non envoyé");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			}else {
				log.debug("Email envoyer à l'adress: {}", sendmail.getAdresseMail());
				return new ResponseEntity<>(HttpStatus.OK);

			}
			

		} catch (Exception e) {

			log.error("Exception obtenue,{}", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
