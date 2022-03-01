package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.VilleDao;
import com.wizard.monticketci.entities.Ville;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class VilleServiceImpl extends AbstractService<Ville> implements VilleService {
	
	@Autowired
	VilleDao dao;

	@Override
	public long count() {
		return dao.count();
	}

	@Override
	protected PagingAndSortingRepository<Ville, String> getDao() {
		return dao;
	}

	@Override
	public Ville findByName(String name) {
		return dao.findByVillenom(name);
	}
	
	@Override
	public List<Ville> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "villedatecreation"));
	}

}
