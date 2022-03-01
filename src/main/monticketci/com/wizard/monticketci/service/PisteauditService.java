package com.wizard.monticketci.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Pisteaudit;



@Service
public interface PisteauditService extends IOperations<Pisteaudit> {
	
	Page<Pisteaudit> findAllPageable(Pageable pageable);
	 List<Pisteaudit> findAllPisteAudit();
	
	Page<Pisteaudit> findByTypePageable(String type, Pageable pageable);


}
