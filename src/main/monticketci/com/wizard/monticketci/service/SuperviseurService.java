package com.wizard.monticketci.service;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Superviseur;


@Service
public interface SuperviseurService extends IOperations<Superviseur>{
	
	public Superviseur getAccountEnableByLogin(String login);
	
	public Superviseur findBySuperviseuremail(String email);

	long count();

}
