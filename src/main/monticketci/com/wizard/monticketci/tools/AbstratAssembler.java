package com.wizard.monticketci.tools;


import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;


@Component
public abstract class AbstratAssembler  <E extends Serializable, D> implements GenericAssembler<E , D>{

	  protected Class<E> entityClazz;
	  
	  protected Class<D> dtoClazz;


	    protected final void setDtoClazz(final Class<E> clazzToSet) {
	    	entityClazz = Preconditions.checkNotNull(clazzToSet);
	    }

	    protected final void setEntityClazz(final Class<D> clazzToSet) {
	    	dtoClazz = Preconditions.checkNotNull(clazzToSet);
	    }
	
}
/*
@Component
public abstract class AbstratAssembler  <E extends Serializable, D> implements GenericAssembler<E , D>{

	  protected Class<E> entityClazz;
	  
	  protected Class<D> dtoClazz;


	    protected final void setDtoClazz(final Class<E> clazzToSet) {
	    	entityClazz = Preconditions.checkNotNull(clazzToSet);
	    }

	    protected final void setEntityClazz(final Class<D> clazzToSet) {
	    	dtoClazz = Preconditions.checkNotNull(clazzToSet);
	    }
	
}*/