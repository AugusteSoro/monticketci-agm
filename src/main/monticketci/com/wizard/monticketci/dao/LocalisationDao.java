package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.monticketci.entities.Localisation;

public interface LocalisationDao extends JpaRepository<Localisation, String> {
	
	Localisation findByLocalisationnom(String name);
	long count();

}
