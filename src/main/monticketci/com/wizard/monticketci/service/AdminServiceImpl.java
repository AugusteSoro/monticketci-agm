package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.AdminDao;
import com.wizard.monticketci.entities.Administrateur;
import com.wizard.monticketci.service.common.AbstractService;



@Service
@Transactional
public class AdminServiceImpl extends AbstractService<Administrateur> implements AdminService {
	
	@Autowired
	AdminDao dao;

	@Override
	protected PagingAndSortingRepository<Administrateur, String> getDao() {
		return dao;
	}
	
	@Override
	public Administrateur getAccountEnableByLogin(String login) {
		return dao.getAccountEnableByLogin(login);
	}

	@Override
	public Administrateur findByAdministrateuremail(String email) {
		return dao.findByAdministrateuremail(email);
	}

	@Override
	public long count() {
		return dao.count();
	}

	@Override
	public List<Administrateur> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "administrateurdatecreation"));
	}

}
