package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wizard.monticketci.entities.Superviseur;


public interface SuperviseurDao extends JpaRepository<Superviseur, String> {
	
	@Query("SELECT s from Superviseur s WHERE s.superviseuremail =:login AND s.superviseurenable = TRUE")
	Superviseur getAccountEnableByLogin(String login);
	
	Superviseur findBySuperviseuremail(String email);
	
	long count();

}
