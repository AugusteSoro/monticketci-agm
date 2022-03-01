package com.wizard.monticketci.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@NamedQuery(name="Parameter.findAll", query="SELECT p FROM Parameter p")
public class Parameter implements Serializable {
	
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
    private String parameterid;
    
	@Column(length=128)
	private String parameterlibele;
	
	@Column
	private float value;

	@Column
	private float value1;
	
    public String parametertype;
	
	@Column(length=128)
	private String parametercode;
	
	@Column(length=24)
	private String parameterstatut;
	
	private Boolean parameterenable;
	
	private String parameterdescription;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
   	@Temporal(TemporalType.TIMESTAMP)
    private Date parameterdate;
    
    
    

}
