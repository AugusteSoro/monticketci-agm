package com.wizard.monticketci.dao;


import com.wizard.monticketci.entities.Superviseur;

import lombok.Data;

@Data
public class SuperviseurLogedDto {
	
	private Superviseur superviseur;
	private String token;

}
