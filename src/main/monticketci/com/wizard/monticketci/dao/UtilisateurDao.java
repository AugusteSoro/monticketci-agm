package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wizard.monticketci.entities.Utilisateur;



public interface UtilisateurDao extends JpaRepository<Utilisateur, String> {
	
	@Query("SELECT u from Utilisateur u WHERE u.utilisateuremail =:login AND u.utilisateurenable = TRUE")
	Utilisateur getAccountEnableByLogin(String login);
	
	Utilisateur findByUtilisateuremail(String email);
	
	Utilisateur findByUtilisateurcode(String code);
	
	Utilisateur findByUtilisateurtelephone(String tel);
	
	long count();

}
