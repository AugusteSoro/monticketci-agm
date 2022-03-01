package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Prix;

@Service
public interface PrixService extends IOperations<Prix> {
	
	List<Prix> findAll();
	
	List<Prix> findByValeur(int value);
	
	Prix findByValeurAndSuperviseur(int value, String superviseurid);
	
	List<Prix> findPrixBySuperviseurId(String id);

}
