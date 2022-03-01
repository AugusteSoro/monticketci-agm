package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wizard.monticketci.entities.Administrateur;


public interface AdminDao extends JpaRepository<Administrateur, String> {
	
	@Query("SELECT a from Administrateur a WHERE a.administrateuremail =:login AND a.administrateurenable = TRUE")
	Administrateur getAccountEnableByLogin(String login);
	
	Administrateur findByAdministrateuremail(String email);
	
	long count();

}
