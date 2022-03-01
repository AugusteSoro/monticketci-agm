package com.wizard.monticketci.service;

import org.springframework.stereotype.Service;

@Service
public interface CrudEntityLogic <E>{
	
	E save(E entity);  // Save entity in db   --- create / update  POST
	
	E getById(int id);	// get an Entity by Id  ---- get  / get 
	
	E getByName(String n);	// get an Entity by Id  ---- get  / get 
	
	void remove (int id);//  Delete and Entity      - remove - Delete 

}