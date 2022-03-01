package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.TypeeventDao;
import com.wizard.monticketci.entities.Typeevent;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class TypeeventServiceImpl extends AbstractService<Typeevent> implements TypeeventService {
	
	@Autowired
	TypeeventDao dao;

	@Override
	protected PagingAndSortingRepository<Typeevent, String> getDao() {
		return dao;
	}

	@Override
	public Typeevent findByName(String name) {
		return dao.findByTypeeventnom(name);
	}
	
	@Override
	public List<Typeevent> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "typeeventdatecreation"));
	}

	@Override
	public List<Typeevent> findTypeeventsBySuperviseurId(String id) {
		return dao.findBySuperviseurSuperviseurid(id);
	}

	@Override
	public List<Typeevent> findTypeeventsBySuperviseurIdAndStatut(String id, String statut) {
		return dao.findBySuperviseurSuperviseuridAndTypeeventstatutIgnoreCase(id, statut);
	}

	@Override
	public List<Typeevent> findAllByStatut(String statut) {
		return dao.findByTypeeventstatutIgnoreCase(statut);
	}

}
