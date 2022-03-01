package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.entities.Parameter;


@Service
public interface ParameterLogic extends CrudEntityLogic<Parameter>{

	  Parameter findByCode(String parametercode);
	  List<Parameter> findByType(String parametertype);
	  List<Parameter> findAllParameter();

}
