package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Utilisateur;

@Service
public interface UtilisateurService extends IOperations<Utilisateur> {
	
	public Utilisateur getAccountEnableByLogin(String login);
	
	public Utilisateur findByUtilisateuremail(String email);
	
	public Utilisateur findByUtilisateurcode(String code);
	
	public Utilisateur findByUtilisateurtelephone(String tel);
	
	long count();
		
	List<Utilisateur> findAll();

}
