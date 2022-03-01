package com.wizard.monticketci.dao;

import java.util.List;

import lombok.Data;

@Data
public class SendMail {
	
	private String message;
	private List<String> adresseMail;
	private String objet;

}
