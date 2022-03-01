package com.wizard.monticketci.dto;

import java.util.List;

import lombok.Data;

@Data
public class SuperviseurDto {
	
	private String superviseurid;

	private String superviseurresidence;

	private String superviseuremail;

	private String superviseurnom;

	private String superviseurpassword;

	private String superviseurprenom;

	private String superviseurtelephone;
		
	private String superviseurtype;
	
	private String superviseurraisonsocial;
	
	private String administrateurid;
	
	private boolean superviseurisactive;
	
	/**/
	private String superviseursigle;
	
	private String superviseurtitre;
	
	private String superviseuractivite;
	
	private String superviseurformejuridique;
	
	private String superviseurpays;
	
	private String superviseurville;
		
	private String superviseuremailrepresentant;
	
	private String superviseurtelephonerepresentant;
	
	private String superviseurfonction;
	
	private boolean superviseuractivitehorsci;
	
	private List<String> superviseurpaysactivite;
	
	private boolean superviseurmonticketcihorsci;
	
}
