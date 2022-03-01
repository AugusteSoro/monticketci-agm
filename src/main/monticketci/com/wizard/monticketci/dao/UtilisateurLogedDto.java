package com.wizard.monticketci.dao;


import com.wizard.monticketci.entities.Utilisateur;

import lombok.Data;

@Data
public class UtilisateurLogedDto {
	
	private Utilisateur utilisateur;
	private String token;

}
