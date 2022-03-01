package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.monticketci.entities.Evenementlieu;

public interface EvenementlieuDao extends JpaRepository<Evenementlieu, String>{
	
	List<Evenementlieu> findByEvenementEvenementid(String id);


}
