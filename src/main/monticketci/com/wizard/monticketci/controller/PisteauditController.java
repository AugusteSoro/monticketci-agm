package com.wizard.monticketci.controller;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.entities.Pisteaudit;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.PisteauditService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author SORO
 * @create 23 fev,21
 */

@RestController
@RequestMapping("/parameter/pisteaudit")
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value="Controller de la piste audit")
public class PisteauditController {
	
	@Log
	private Logger log;

	@Autowired
	PisteauditService pisteauditService;

	@Autowired
	DateTools dateTools;
	
	@ApiOperation(value = "Voir la liste des pistes audit de la base de donnée", response = List.class)
	@GetMapping
	public ResponseEntity<List<Pisteaudit>> findall() {
		
		try {
			log.debug("getting all audit from database");

			if (!pisteauditService.findAll().isEmpty()) {	
			
				List<Pisteaudit> ls = pisteauditService.findAll();

				if (!ls.isEmpty()) {
					log.debug("audit found size,{}" + ls.size());
	
					return new ResponseEntity<>(ls, HttpStatus.OK);
	
				} else {
	
					log.debug("audit not found, database is empty");
	
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
				}
			
			} else {
				log.debug("audit not found, datbase is empty");
				
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					
			}

		} catch (Exception e) {

			e.printStackTrace();
			log.error("error while getting all audit,{}" + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@ApiOperation(value = "Voir la liste paginée des pistes audit trier par type", response = List.class)
	@GetMapping(value = { "/bytype/{type}" })
	public ResponseEntity<Page<Pisteaudit>> findbytypepage(@PathVariable("type") String type, Pageable pageable) {
		
		try {
			log.debug("getting audit by type from database, type:{}", type);
			
			Page<Pisteaudit> ls = pisteauditService.findByTypePageable(type, pageable);
			
			log.debug("Retour API:{}", ls);


			if (!ls.isEmpty()) {
				// log.debug("audit found size,{}" + ls.size());

				return new ResponseEntity<>(ls, HttpStatus.OK);

			} else {

				log.debug("audit not found, database is empty");

				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}
		

		} catch (Exception e) {

			e.printStackTrace();
			log.error("error while getting all audit,{}" + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	@ApiOperation(value = "Voir la liste paginée des pistes audit de la base de donnée", response = List.class)
	@GetMapping(value = { "/pageable" })
	public ResponseEntity<Page<Pisteaudit>> findallpage(Pageable pageable) {
		
		try {
			log.debug("getting all audit from database");

			if (!pisteauditService.findAll().isEmpty()) {	
			
				Page<Pisteaudit> ls = pisteauditService.findAllPageable(pageable);

				if (!ls.isEmpty()) {
					// log.debug("audit found size,{}" + ls.size());
	
					return new ResponseEntity<>(ls, HttpStatus.OK);
	
				} else {
	
					log.debug("audit not found, database is empty");
	
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
				}
			
			} else {
				log.debug("audit not found, datbase is empty");
				
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					
			}

		} catch (Exception e) {

			e.printStackTrace();
			log.error("error while getting all audit,{}" + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	
	
	@ApiOperation(value = "ajouter une nouvelle Heure de commande ")
	@RequestMapping(value = { "/byorder" }, method = RequestMethod.GET)
	public ResponseEntity<List<Pisteaudit>> findallbyorder() {
		
		try {
			log.debug("getting all audit from database");

			if (!pisteauditService.findAll().isEmpty()) {	
			
				List<Pisteaudit> ls = pisteauditService.findAllPisteAudit();

				if (!ls.isEmpty()) {
					log.debug("audit found size,{}" + ls.size());
	
					return new ResponseEntity<>(ls, HttpStatus.OK);
	
				} else {
	
					log.debug("audit not found, database is empty");
	
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
				}
			
			} else {
				log.debug("audit not found, datbase is empty");
				
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					
			}

		} catch (Exception e) {

			e.printStackTrace();
			log.error("error while getting all audit,{}" + e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
