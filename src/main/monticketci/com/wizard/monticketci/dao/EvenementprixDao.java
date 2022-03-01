package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.monticketci.entities.Evenementprix;

public interface EvenementprixDao extends JpaRepository<Evenementprix, String> {
	
	List<Evenementprix> findByEvenementEvenementid(String id);

}
