package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Administrateur;


@Service
public interface AdminService extends IOperations<Administrateur> {
	
	public Administrateur getAccountEnableByLogin(String login);
	
	public Administrateur findByAdministrateuremail(String email);
	
	long count();
	
	List<Administrateur> findAll();

}
