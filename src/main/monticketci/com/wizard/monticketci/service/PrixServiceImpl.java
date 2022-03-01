package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.PrixDao;
import com.wizard.monticketci.entities.Prix;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class PrixServiceImpl extends AbstractService<Prix> implements PrixService {

	@Autowired
	PrixDao dao;
	
	@Override
	protected PagingAndSortingRepository<Prix, String> getDao() {
		return dao;
	}
	
	@Override
	public List<Prix> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "prixdatecreation"));
	}

	@Override
	public List<Prix> findByValeur(int value) {
		return dao.findByPrixvaleur(value);
	}

	@Override
	public List<Prix> findPrixBySuperviseurId(String id) {
		return dao.findBySuperviseurSuperviseurid(id);
	}

	@Override
	public Prix findByValeurAndSuperviseur(int value, String superviseurid) {
		return dao.findByPrixvaleurAndSuperviseurSuperviseurid(value, superviseurid);
	}

}
