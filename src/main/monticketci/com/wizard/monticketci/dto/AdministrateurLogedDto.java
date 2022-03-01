package com.wizard.monticketci.dto;

import com.wizard.monticketci.entities.Administrateur;

import lombok.Data;

@Data
public class AdministrateurLogedDto {
	
	private Administrateur administrateur;
	private String token;

}
