package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Evenementprix;

@Service
public interface EvenementprixService extends IOperations<Evenementprix> {
	
	List<Evenementprix> findAll();
	
	List<Evenementprix> findByEvenementId(String id);
	
}
