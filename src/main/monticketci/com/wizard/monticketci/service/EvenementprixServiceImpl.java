package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.EvenementprixDao;
import com.wizard.monticketci.entities.Evenementprix;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class EvenementprixServiceImpl extends AbstractService<Evenementprix> implements EvenementprixService {
	
	@Autowired
	EvenementprixDao dao;

	@Override
	protected PagingAndSortingRepository<Evenementprix, String> getDao() {
		return dao;
	}
	
	@Override
	public List<Evenementprix> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "evenementprixdatecreation"));
	}

	@Override
	public List<Evenementprix> findByEvenementId(String id) {
		return dao.findByEvenementEvenementid(id);
	}

}
