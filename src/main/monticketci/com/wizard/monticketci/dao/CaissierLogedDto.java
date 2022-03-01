package com.wizard.monticketci.dao;

import com.wizard.monticketci.entities.Caissier;

import lombok.Data;

@Data
public class CaissierLogedDto {
	
	private Caissier caissier;
	private String token;

}
