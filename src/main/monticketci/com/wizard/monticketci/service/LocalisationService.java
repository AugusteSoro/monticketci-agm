package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Localisation;

@Service
public interface LocalisationService extends IOperations<Localisation> {
	
	
	Localisation findByName(String name);
	List<Localisation> findAll();
	long count();



}
