package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.EvenementlieuDao;
import com.wizard.monticketci.entities.Evenementlieu;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class EvenementlieuServiceImpl extends AbstractService<Evenementlieu> implements EvenementlieuService {
	
	@Autowired
	EvenementlieuDao dao;

	@Override
	protected PagingAndSortingRepository<Evenementlieu, String> getDao() {
		return dao;
	}

	@Override
	public List<Evenementlieu> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "evenementlieudatecreation"));
	}

	@Override
	public List<Evenementlieu> findByEvenementId(String id) {
		return dao.findByEvenementEvenementid(id);
	}
	
}
