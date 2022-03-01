package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the evenementlieu database table.
 * 
 */
@Data
@Entity
@NamedQuery(name="Evenementlieu.findAll", query="SELECT e FROM Evenementlieu e")
public class Evenementlieu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String evenementlieuid;

	private String evenementlieucode;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evenementlieudatecreation;

	private String evenementlieulibelle;

	//bi-directional many-to-one association to Evenement
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="evenementid")
	private Evenement evenement;

	//bi-directional many-to-one association to Lieu
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lieuid")
	private Lieu lieu;

	public Evenementlieu() {
	}

	public String getEvenementlieucode() {
		return this.evenementlieucode;
	}

	public void setEvenementlieucode(String evenementlieucode) {
		this.evenementlieucode = evenementlieucode;
	}

	public String getEvenementlieuid() {
		return this.evenementlieuid;
	}

	public void setEvenementlieuid(String evenementlieuid) {
		this.evenementlieuid = evenementlieuid;
	}

	public Date getEvenementlieudatecreation() {
		return this.evenementlieudatecreation;
	}

	public void setEvenementlieudatecreation(Date evenementlieudatecreation) {
		this.evenementlieudatecreation = evenementlieudatecreation;
	}

	public String getEvenementlieulibelle() {
		return this.evenementlieulibelle;
	}

	public void setEvenementlieulibelle(String evenementlieulibelle) {
		this.evenementlieulibelle = evenementlieulibelle;
	}

	public Evenement getEvenement() {
		return this.evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public Lieu getLieu() {
		return this.lieu;
	}

	public void setLieu(Lieu lieu) {
		this.lieu = lieu;
	}

}