package com.wizard.monticketci.tools;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@SuppressWarnings("unchecked")
@Component
//public  class AbstractAssemblerLogic<E extends Serializable , D> implements GenericAssembler<E, D> {	
public  class AbstractAssemblerLogic<E extends Serializable , D> extends AbstratAssembler<E ,D> implements GenericAssembler<E, D> {


	//abstract
	
  //   protected Class<E> entityClazz;
	  
	//  protected Class<D> dtoClazz;
	
	//@Autowired
	ModelMapper mapper = new ModelMapper();
	
	@Override
	public E createFrom(D dto) {
		

		return  mapper.map(dto, super.entityClazz );
		//return null;
	}

	@Override
	public D createFrom(E entity) {
		return  mapper.map(entity, super.dtoClazz);
		//return null;
	}

	@Override
	public E updateEntity(E entity, D dto) {
		return null;
	}

   
}