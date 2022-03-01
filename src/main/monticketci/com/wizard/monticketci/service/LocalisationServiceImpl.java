package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.LocalisationDao;
import com.wizard.monticketci.entities.Localisation;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class LocalisationServiceImpl extends AbstractService<Localisation> implements LocalisationService {
	
	@Autowired
	LocalisationDao dao;

	@Override
	protected PagingAndSortingRepository<Localisation, String> getDao() {
		return dao;
	}

	@Override
	public Localisation findByName(String name) {
		return dao.findByLocalisationnom(name);
	}
	
	@Override
	public List<Localisation> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "localisationdatecreation"));
	}
	
	@Override
	public long count() {
		return dao.count();
	}


}
