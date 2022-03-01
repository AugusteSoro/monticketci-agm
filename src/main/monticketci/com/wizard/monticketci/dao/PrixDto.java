package com.wizard.monticketci.dao;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PrixDto {
	
	private String prixid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date prixdatecreation;

	private Boolean prixenable;

	private String prixstatut;
	
	private String prixlibelle;
	
	private String prixdescription;

	private Integer prixvaleur;
	
	private String superviseurid;

}
