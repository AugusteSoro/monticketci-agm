package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.ParameterDao;
import com.wizard.monticketci.entities.Parameter;



@Service
public class ParameterLogicImpl implements ParameterLogic {
	
	@Autowired
	ParameterDao paramdao ;

	@Override
	public Parameter save(Parameter entity) {
		return paramdao.save(entity);
	}

	@Override
	public Parameter getById(int id) {
		return  paramdao.findoneById(id);
	}

	@Override
	public Parameter getByName(String n) {
		return null;
	}

	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub
		Parameter param = this.getById(id);
		if(param!=null) {	
			paramdao.delete(param);
		}
		
	}

	@Override
	public Parameter findByCode(String parametercode) {
		return paramdao.findByCode(parametercode);
	}

	@Override
	public List<Parameter> findAllParameter() {
		return paramdao.findAllParameter();
	}

	@Override
	public List<Parameter> findByType(String parametertype) {
		return paramdao.findByType(parametertype);
	}

}
