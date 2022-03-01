package com.wizard.monticketci.service;

import java.util.List;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Lieu;

public interface LieuService extends IOperations<Lieu>{
	
	List<Lieu> findAll();
	Lieu findByNom(String nom);
	
	List<Lieu> findPrixBySuperviseurId(String id);

}
