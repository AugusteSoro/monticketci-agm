package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Ville;

@Service
public interface VilleService extends IOperations<Ville> {
	
	Ville findByName(String name);
	
	List<Ville> findAll();
	
	long count();


}
