package com.wizard.monticketci.dto;


import com.wizard.monticketci.entities.Utilisateur;

import lombok.Data;

@Data
public class UtilisateurLogedDto {
	
	private Utilisateur utilisateur;
	private String token;

}
