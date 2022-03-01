package com.wizard.monticketci.dto;


import com.wizard.monticketci.entities.Superviseur;

import lombok.Data;

@Data
public class SuperviseurLogedDto {
	
	private Superviseur superviseur;
	private String token;

}
