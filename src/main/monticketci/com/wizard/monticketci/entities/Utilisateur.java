package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the utilisateur database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Utilisateur.findAll", query="SELECT u FROM Utilisateur u")
public class Utilisateur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String utilisateurid;

	private String utilisateurcivilite;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date utilisateurdatecreation;

	private String utilisateuremail;

	private Boolean utilisateurenable;

	private Boolean utilisateurisdeleted;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date utilisateurlastcon;

	private String utilisateurnom;

	private String utilisateurpassword;

	private String utilisateurprenom;

	private String utilisateurtelephone;

	private String utilisateurtoken;
	
	private String utilisateurcode;

	private String utilisateursexe;
	
	private String utilisateurstatut;
	
	//bi-directional many-to-one association to Localisation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="localisationid")
	private Localisation localisation;

	//bi-directional many-to-one association to Reservation
	@OneToMany(mappedBy="utilisateur")
	@JsonIgnore
	private List<Reservation> reservations;

	public Utilisateur() {
	}

	public String getUtilisateurid() {
		return this.utilisateurid;
	}

	public void setUtilisateurid(String utilisateurid) {
		this.utilisateurid = utilisateurid;
	}

	public String getUtilisateurcivilite() {
		return this.utilisateurcivilite;
	}

	public void setUtilisateurcivilite(String utilisateurcivilite) {
		this.utilisateurcivilite = utilisateurcivilite;
	}

	public Date getUtilisateurdatecreation() {
		return this.utilisateurdatecreation;
	}

	public void setUtilisateurdatecreation(Date utilisateurdatecreation) {
		this.utilisateurdatecreation = utilisateurdatecreation;
	}

	public String getUtilisateuremail() {
		return this.utilisateuremail;
	}

	public void setUtilisateuremail(String utilisateuremail) {
		this.utilisateuremail = utilisateuremail;
	}

	public Boolean getUtilisateurenable() {
		return this.utilisateurenable;
	}

	public void setUtilisateurenable(Boolean utilisateurenable) {
		this.utilisateurenable = utilisateurenable;
	}

	public Boolean getUtilisateurisdeleted() {
		return this.utilisateurisdeleted;
	}

	public void setUtilisateurisdeleted(Boolean utilisateurisdeleted) {
		this.utilisateurisdeleted = utilisateurisdeleted;
	}

	public Date getUtilisateurlastcon() {
		return this.utilisateurlastcon;
	}

	public void setUtilisateurlastcon(Date utilisateurlastcon) {
		this.utilisateurlastcon = utilisateurlastcon;
	}

	public String getUtilisateurnom() {
		return this.utilisateurnom;
	}

	public void setUtilisateurnom(String utilisateurnom) {
		this.utilisateurnom = utilisateurnom;
	}

	public String getUtilisateurpassword() {
		return this.utilisateurpassword;
	}

	public void setUtilisateurpassword(String utilisateurpassword) {
		this.utilisateurpassword = utilisateurpassword;
	}

	public String getUtilisateurprenom() {
		return this.utilisateurprenom;
	}

	public void setUtilisateurprenom(String utilisateurprenom) {
		this.utilisateurprenom = utilisateurprenom;
	}

	public String getUtilisateurtelephone() {
		return this.utilisateurtelephone;
	}

	public void setUtilisateurtelephone(String utilisateurtelephone) {
		this.utilisateurtelephone = utilisateurtelephone;
	}

	public String getUtilisateurtoken() {
		return this.utilisateurtoken;
	}

	public void setUtilisateurtoken(String utilisateurtoken) {
		this.utilisateurtoken = utilisateurtoken;
	}

	public List<Reservation> getReservations() {
		return this.reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Reservation addReservation(Reservation reservation) {
		getReservations().add(reservation);
		reservation.setUtilisateur(this);

		return reservation;
	}

	public Reservation removeReservation(Reservation reservation) {
		getReservations().remove(reservation);
		reservation.setUtilisateur(null);

		return reservation;
	}

	public Localisation getLocalisation() {
		return this.localisation;
	}

	public void setLocalisation(Localisation localisation) {
		this.localisation = localisation;
	}

}