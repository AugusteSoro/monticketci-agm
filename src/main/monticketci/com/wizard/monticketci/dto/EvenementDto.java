package com.wizard.monticketci.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class EvenementDto {
	
	private String evenementid;

	private String evenementdescription;

	private Boolean evenementenable;

	private int evenementqteglobal;
	
	private int evenementqtemticket;

	private String evenementnom;
	
	private boolean evenementisactive;

	private String evenementstatut;

	private String typeeventid;
	
	private String superviseurid;
	
	private String localisationid;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evenementdateevenement;
	
	private List<String> lieu;
	
	private List<String> prix;


}
