package com.wizard.monticketci.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wizard.monticketci.dto.ParameterDto;
import com.wizard.monticketci.entities.Parameter;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.ParameterLogic;
import com.wizard.monticketci.service.PisteauditService;
import com.wizard.monticketci.tools.DateTools;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/parameter/utils")
@CrossOrigin(origins = "*", maxAge=3600)
public class ParamController {
	
	
	
	@Autowired
	ParameterLogic paramlogic ;
	

	@Autowired
	PisteauditService pisteauditServce ;
	
	@Autowired
	Environment env ;	
	

	@Autowired
	DateTools dateTools ;
	

	@Log
	private Logger log;
	
	
	@ApiOperation(value = "Ajout parameter ")
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	public ResponseEntity<?> Ajouter(@RequestBody ParameterDto request) {
		try {
			
			log.info("Parametre entrant, libellé:{}", request.parameterlibele);
			
			Parameter parameter = paramlogic.findByCode(request.parametercode);
			
			if(parameter == null) {
				
				Parameter param = new Parameter();
				param.setParametercode(request.parametercode);
				param.setParametertype(request.parametertype.toUpperCase());
				param.setParameterlibele(request.parameterlibele);
				param.setParameterdate(dateTools.getCurrentTimeStamp());
				param.setParameterenable(true);
				param.setValue(request.value);
				param.setValue1(request.value1);
				param.setParameterdescription(request.parameterdescription);
				param.setParameterstatut(env.getProperty("statut.actif"));
				param = paramlogic.save(param);
				
			    log.debug("Parametre enregistré avec succès");

				return new ResponseEntity<>(param, HttpStatus.OK);				
			}else {

				log.debug("Code existant en base, code:{}", request.parametercode);
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			
				
			}
			
				
			
		}catch (Exception e) {	
			log.debug("Une exception");
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	

	@ApiOperation(value = "MAJ parameter ")
	@RequestMapping(value = { "/update" }, method = RequestMethod.POST)
	public ResponseEntity<?> modifier(@RequestBody ParameterDto request) {
		try {
			
			log.info(" parametre to update : "+request.toString()+"----"+request.parametercode);
			
			Parameter parameter = paramlogic.findByCode(request.parametercode);
			
			if(parameter!=null) {
				
				//parameter.setParameterdate(dateTools.getCurrentTimeStamp());
				//parameter.setParameterlibele(request.parameterlibele);
				parameter.setValue(request.value);
				//parameter.setValue1(request.value1);
				parameter.setParameterdescription(request.parameterdescription);
				parameter = paramlogic.save(parameter);
				
				log.debug("Parametre mise à jour");
				return new ResponseEntity<>(parameter, HttpStatus. CREATED);				
			}else {

				log.debug("Parametre introuvable");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
				
			}
			
				
			
		}catch (Exception e) {	
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@ApiOperation(value = "GET parameter ")
	@RequestMapping(value = { "/getby/{type}/{data}" }, method = RequestMethod.GET)
	public ResponseEntity<List<Parameter>> getParam(@PathVariable("type") String type, @PathVariable("data") String data) {
		try {
			
			log.info("Debut de la recherche de parametre, type de recherche: {}, critere de recherche: {} ", type, data);
			
			List<Parameter> parameters = new ArrayList<Parameter>();
			Parameter parameter = new Parameter();
			
			switch (type) {
			case "type":
				parameters = paramlogic.findByType(data.toUpperCase());

				break;
			case "code":
				parameter = paramlogic.findByCode(data);
				parameters.add(parameter);
				break;
			default:
				log.debug("Type de recherche inexistant");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);				
			}
			
			log.debug("Parametres trouvé, Taille: {}", parameters.size());
			return new ResponseEntity<>(parameters, HttpStatus.OK);				

						
		}catch (Exception e) {	
			log.error(" : "+e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	
	
	@ApiOperation(value = "liste des parametres ")
	@RequestMapping(value = { "/getall" }, method = RequestMethod.GET)
	public ResponseEntity<?> findAllParameter() {
		try {
			List<Parameter> params = paramlogic.findAllParameter();
			return new ResponseEntity<>(params, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception
			log.error(" error : " + e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
