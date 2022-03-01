package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.EvenementDao;
import com.wizard.monticketci.entities.Evenement;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class EvenementServiceImpl extends AbstractService<Evenement> implements EvenementService {
	
	@Autowired
	EvenementDao dao;

	@Override
	protected PagingAndSortingRepository<Evenement, String> getDao() {
		return dao;
	}
	
	@Override
	public List<Evenement> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "evenementdatecreation"));
	}

	@Override
	public Evenement findByNom(String nom) {
		return dao.findByEvenementnom(nom);
	}

	@Override
	public long count() {
		return dao.count();
	}
	
	@Override
	public long countBySuperviseur(String superviseurid) {
		return dao.countBySuperviseurSuperviseurid(superviseurid);
	}


	@Override
	public List<Evenement> findByTypeeventId(String id) {
		return dao.findByTypeeventTypeeventid(id);
	}
	
	@Override
	public List<Evenement> findByTypeeventIdAndStatut(String id, String statut) {
		return dao.findByTypeeventTypeeventidAndEvenementstatutIgnoreCase(id, statut);
	}
	
	@Override
	public List<Evenement> findByTypeeventIdAndLocalisationAndStatut(String id, String localisation, String statut) {
		return dao.findByTypeeventTypeeventidAndLocalisationLocalisationidAndEvenementstatutIgnoreCase(id, localisation, statut);
	}

	@Override
	public List<Evenement> findEventsBySuperviseurId(String id) {
		return dao.findBySuperviseurSuperviseurid(id);
	}

	@Override
	public List<Evenement> findAllByStatut(String statut) {
		return dao.findByEvenementstatutIgnoreCase(statut);
	}

	@Override
	public List<Evenement> findEventsBySuperviseurIdAndStatut(String id, String statut) {
		return dao.findBySuperviseurSuperviseuridAndEvenementstatutIgnoreCase(id, statut);
	}

	@Override
	public List<Evenement> findTop3Event() {
		return dao.findTop3ByOrderByEvenementdatecreationDesc();
	}


}
