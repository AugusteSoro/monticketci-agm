package com.wizard.monticketci.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.SuperviseurDao;
import com.wizard.monticketci.entities.Superviseur;
import com.wizard.monticketci.service.common.AbstractService;



@Service
@Transactional
public class SuperviseurServiceImpl extends AbstractService<Superviseur> implements SuperviseurService{

	@Autowired
	SuperviseurDao dao;
	
	@Override
	protected PagingAndSortingRepository<Superviseur, String> getDao() {
		return dao;
	}

	@Override
	public Superviseur getAccountEnableByLogin(String login) {
		return dao.getAccountEnableByLogin(login);
	}

	@Override
	public Superviseur findBySuperviseuremail(String email) {
		return dao.findBySuperviseuremail(email);
	}

	@Override
	public long count() {
		return dao.count();
	}

}
