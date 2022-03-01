package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Evenement;

public interface EvenementDao extends JpaRepository<Evenement, String>{
	
	Evenement findByEvenementnom(@Param("nom") String nom);
	
	List<Evenement> findByTypeeventTypeeventid(@Param("id") String id);
		
	List<Evenement> findByTypeeventTypeeventidAndEvenementstatutIgnoreCase(@Param("id") String id, @Param("statut") String statut);
	
	List<Evenement> findByTypeeventTypeeventidAndLocalisationLocalisationidAndEvenementstatutIgnoreCase(@Param("id") String id, @Param("localisation") String localisation, @Param("statut") String statut);

	List<Evenement> findBySuperviseurSuperviseurid(@Param("id") String id);
	
	List<Evenement> findByEvenementstatutIgnoreCase(@Param("statut") String statut);
	
	List<Evenement> findBySuperviseurSuperviseuridAndEvenementstatutIgnoreCase(@Param("id") String id, @Param("statut") String statut);
	
	//@Query("SELECT e FROM Evenement e ORDER BY e.evenementdatecreation DESC limit :limit")
	List<Evenement> findTop3ByOrderByEvenementdatecreationDesc();

	long count();
	
	long countBySuperviseurSuperviseurid(@Param("superviseurid") String superviseurid);


}
