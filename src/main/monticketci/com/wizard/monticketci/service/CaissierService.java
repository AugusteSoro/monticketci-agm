package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Caissier;

@Service
public interface CaissierService extends IOperations<Caissier>{
	
	public Caissier getAccountEnableByLogin(String login);
	
	public Caissier findByCaissieremail(String email);
	
	public List<Caissier> findBySuperviseurid(String superviseurid);
	
	long count();
	
	long countBySuperviseur(String superviseurid);

}
