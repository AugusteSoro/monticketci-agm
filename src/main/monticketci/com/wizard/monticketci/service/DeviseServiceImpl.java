package com.wizard.monticketci.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.DeviseDao;
import com.wizard.monticketci.entities.Devise;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class DeviseServiceImpl extends AbstractService<Devise> implements DeviseService{

	@Autowired
	DeviseDao dao;
	
	@Override
	protected PagingAndSortingRepository<Devise, String> getDao() {
		return dao;
	}

}
