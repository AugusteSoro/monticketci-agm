package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Lieu;

public interface LieuDao extends JpaRepository<Lieu, String> {
	
	Lieu findByLieunom(@Param("nom") String nom);
	
	long count();
	
	List<Lieu> findBySuperviseurSuperviseurid(@Param("id") String id);


}
