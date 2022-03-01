package com.wizard.monticketci.service;

import java.util.List;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Evenement;

public interface EvenementService extends IOperations<Evenement> {
	
	List<Evenement> findAll();
	Evenement findByNom(String nom);
	List<Evenement> findByTypeeventId(String id);
	List<Evenement> findByTypeeventIdAndStatut(String id, String statut);
	List<Evenement> findByTypeeventIdAndLocalisationAndStatut(String id, String localisation, String statut);
	List<Evenement> findEventsBySuperviseurId(String id);
	List<Evenement> findEventsBySuperviseurIdAndStatut(String id, String statut);
	List<Evenement> findAllByStatut(String statut);
	List<Evenement> findTop3Event();
	long count();
	long countBySuperviseur(String superviseurid);
	

}
