package com.wizard.monticketci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wizard.monticketci.entities.Devise;

public interface DeviseDao extends JpaRepository<Devise, String>{

}
