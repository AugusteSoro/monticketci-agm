package com.wizard.monticketci.dto;

import lombok.Data;

@Data
public class ReservationDto {
	
	private String reservationid;

	private String reservationcommentaire;

	private String reservationcodereference;

	private String reservationpaiementmode;

	private String reservationpaiementstatut;

	private Integer reservationprix;

	private float reservationprixtotal;

	private Integer reservationremise = 0;
			
	private float reservationfrais = 0;

	private String reservationwallet;

	private String resevenementid;
	
	private String resevenementnom;
	
	private String reslieuid;
	
	private String reslieunom;
	
	private String reservationtypetransaction;
	
	private String reservationdevise;

	private String utilisateurid;
	
	private String resdestinatairenom;
	
	private String resdestinatairetel;


}
