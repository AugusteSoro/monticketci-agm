/**
 * 
 */
package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Caissier;


/**
 * @author Auguste SORO
 *
 */
public interface CaissierDao extends JpaRepository<Caissier, String> {

	
	@Query("SELECT c from Caissier c WHERE c.caissieremail =:login AND c.caissierenable = TRUE")
	Caissier getAccountEnableByLogin(String login);
	
	Caissier findByCaissieremail(String email);
	
	List<Caissier> findBySuperviseurSuperviseurid(String superviseurid);
	
	long count();
	
	long countBySuperviseurSuperviseurid(@Param("superviseurid") String superviseurid);
	
}
