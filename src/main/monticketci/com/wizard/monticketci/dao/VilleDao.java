package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.monticketci.entities.Ville;

public interface VilleDao extends JpaRepository<Ville, String>{
	
	Ville findByVillenom(String nom);
	
	long count();

}
