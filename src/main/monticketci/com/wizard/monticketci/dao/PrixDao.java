package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Prix;

public interface PrixDao extends JpaRepository<Prix, String> {
	
	List<Prix> findByPrixvaleur(@Param("value") int value);
	
	Prix findByPrixvaleurAndSuperviseurSuperviseurid(@Param("value") int value, @Param("superviseurid") String superviseurid);
	
	long count();
	
	List<Prix> findBySuperviseurSuperviseurid(@Param("id") String id);

}
