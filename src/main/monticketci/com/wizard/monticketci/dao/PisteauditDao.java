package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.wizard.monticketci.entities.Pisteaudit;



public interface PisteauditDao extends PagingAndSortingRepository<Pisteaudit, String> {
	
	@Query("SELECT p FROM Pisteaudit p WHERE LOWER(p.pisteaudittype) = LOWER(:type) ORDER BY p.pisteauditdate DESC")
	Page<Pisteaudit> findByTypePageable(String type, Pageable pageable);

	
	  @Query("SELECT p FROM Pisteaudit p ORDER BY p.pisteauditdate DESC ")
	  List<Pisteaudit> findAllPisteAudit(Pageable pageable);
	
}
