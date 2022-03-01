package com.wizard.monticketci.dao.common;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {

    T findOne(final String id);

    List<T> findAll();
    
    T create(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final String entityId);
        

}
