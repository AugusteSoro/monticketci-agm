/**
 * 
 */
package com.wizard.monticketci.tools;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ars
 * @create Nov 30, 2018
 */
public interface GenericAssembler<E extends Serializable, D> {

	E createFrom(D dto);

	D createFrom(E entity);

	E updateEntity(E entity, D dto);

	default List<D> createFromEntities(final List<E> entities) {
		return entities.stream().map(this::createFrom).collect(Collectors.toList());
	}

	default List<E> createFromDtos(final List<D> dtos) {
		return dtos.stream().map(this::createFrom).collect(Collectors.toList());
	}
}
