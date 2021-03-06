package com.wizard.monticketci.service.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.wizard.monticketci.dao.common.IOperations;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    // read - one

    @Override
    @Transactional(readOnly = true)
    public T findOne(final String id) {
        return getDao().findById(id).orElse(null);
    }

    // read - all

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
    	return Lists.newArrayList(getDao().findAll());
    }

    // write

    @Override
    public T create(final T entity) {
        return getDao().save(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().save(entity);
    }
    
    @Override
    public void delete(T entity) {
        getDao().delete(entity);
    }
    
    @Override
    public void deleteById(String entityId) {
        T entity = findOne(entityId);
        delete(entity);
    }

    protected abstract PagingAndSortingRepository<T, String> getDao();
    


}
