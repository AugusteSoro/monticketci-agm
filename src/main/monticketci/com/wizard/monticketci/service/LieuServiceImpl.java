package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.LieuDao;
import com.wizard.monticketci.entities.Lieu;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class LieuServiceImpl extends AbstractService<Lieu> implements LieuService {
	
	@Autowired
	LieuDao dao;

	@Override
	protected PagingAndSortingRepository<Lieu, String> getDao() {
		return dao;
	}
	
	@Override
	public List<Lieu> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "lieudatecreation"));
	}

	@Override
	public Lieu findByNom(String nom) {
		return dao.findByLieunom(nom);
	}

	@Override
	public List<Lieu> findPrixBySuperviseurId(String id) {
		return dao.findBySuperviseurSuperviseurid(id);
	}

}
