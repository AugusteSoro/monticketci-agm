package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.CaissierDao;
import com.wizard.monticketci.entities.Caissier;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class CaissierServiceImpl extends AbstractService<Caissier> implements CaissierService {
	
	@Autowired
	CaissierDao dao;

	@Override
	protected PagingAndSortingRepository<Caissier, String> getDao() {
		return dao;
	}

	@Override
	public Caissier getAccountEnableByLogin(String login) {
		return dao.getAccountEnableByLogin(login);
	}

	@Override
	public Caissier findByCaissieremail(String email) {
		return dao.findByCaissieremail(email);
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
	public List<Caissier> findBySuperviseurid(String superviseurid) {
		return dao.findBySuperviseurSuperviseurid(superviseurid);
	}


}
