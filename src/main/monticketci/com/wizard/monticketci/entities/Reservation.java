package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the reservation database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Reservation.findAll", query="SELECT r FROM Reservation r")
public class Reservation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String reservationid;

	private String reseervationcommentaire;

	private String reservationcodereference;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reservationdatecreation;

	private String reservationpaiementmode;

	private String reservationpaiementstatut;

	private Integer reservationprix;

	private Float reservationprixtotal;

	private Integer reservationremise = 0;
	
	//private float reservationfraiscinetpay = 0;
	
	//private float reservationfraisservice = 0;
	
	private Float reservationfrais;

	private String reservationwallet;

	private String resevenementid;
	
	private String resevenementnom;
	
	private String reslieuid;
	
	private String reslieunom;
	
	private String reservationtypetransaction;
	
	private String reservationdevise;
	
	private String resdestinatairenom;
	
	private String resdestinatairetel;
	
	private boolean resticketischecked;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reservationcheckeddate;
	
	private String reservationscannerid;

	//bi-directional many-to-one association to Utilisateur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="utilisateurid")
	private Utilisateur utilisateur;

	public Reservation() {
	}

}