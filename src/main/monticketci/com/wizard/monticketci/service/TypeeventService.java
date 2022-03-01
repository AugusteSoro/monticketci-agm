package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Typeevent;

@Service
public interface TypeeventService extends IOperations<Typeevent> {
	
	Typeevent findByName(String name);
	
	List<Typeevent> findTypeeventsBySuperviseurId(String id);
	List<Typeevent> findTypeeventsBySuperviseurIdAndStatut(String id, String statut);
	List<Typeevent> findAllByStatut(String statut);

}
