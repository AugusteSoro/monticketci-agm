package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Evenementlieu;

@Service
public interface EvenementlieuService extends IOperations<Evenementlieu> {
	
	List<Evenementlieu> findByEvenementId(String id);
	List<Evenementlieu> findAll();


}
