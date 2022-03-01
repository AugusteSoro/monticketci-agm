package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Typeevent;

public interface TypeeventDao extends JpaRepository<Typeevent, String> {
	
	Typeevent findByTypeeventnom(String name);
	
	List<Typeevent> findBySuperviseurSuperviseurid(@Param("id") String id);
	
	List<Typeevent> findBySuperviseurSuperviseuridAndTypeeventstatutIgnoreCase(@Param("id") String id, @Param("statut") String statut);
	
	List<Typeevent> findByTypeeventstatutIgnoreCase(@Param("statut") String statut);

}
