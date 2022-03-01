package com.wizard.monticketci.service;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.monticketci.dao.PisteauditDao;
import com.wizard.monticketci.entities.Pisteaudit;
import com.wizard.monticketci.log.Log;
import com.wizard.monticketci.service.common.AbstractService;


@Service
@Transactional
public class PisteauditServiceImpl extends AbstractService<Pisteaudit> implements PisteauditService {

	@Autowired
	PisteauditDao dao;
	
	@Log
	private Logger log;
	
	 public PisteauditServiceImpl() {
	        super();
	    }

	@Override
	protected PagingAndSortingRepository<Pisteaudit, String> getDao() {
		return dao;
	}

	@Override
	public Page<Pisteaudit> findAllPageable(Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	public List<Pisteaudit> findAllPisteAudit() {
		Pageable pageble = PageRequest.of(0, 100);

		// TODO Auto-generated method stub
		return dao.findAllPisteAudit(pageble);
		
	}
	public Page<Pisteaudit> findByTypePageable(String type, Pageable pageable) {
		return dao.findByTypePageable(type, pageable);
	}

}
