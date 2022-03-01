package com.wizard.monticketci.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Parameter;


public interface ParameterDao  extends CrudRepository<Parameter, String>{
	
	  @Query("SELECT p FROM Parameter p WHERE  p.parametercode=:parametercode AND p.parameterenable=true")
	  Parameter findByCode(String parametercode);
	  
	  @Query("SELECT p FROM Parameter p WHERE  p.parametertype=:parametertype AND p.parameterenable=true")
	  List<Parameter> findByType(@Param("parametertype") String parametertype);
	  	
	  @Query("SELECT p FROM Parameter p WHERE p.parameterenable=TRUE ORDER BY p.parameterdate  DESC")
	  List<Parameter> findAllParameter();
	  
	  @Query("SELECT p FROM Parameter p WHERE  p.parameterid=:parameterid AND p.parameterenable=true")
	  Parameter findoneById(int parameterid);

}
