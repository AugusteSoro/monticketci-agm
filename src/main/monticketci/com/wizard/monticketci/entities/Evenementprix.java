package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the evenementprix database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Evenementprix.findAll", query="SELECT e FROM Evenementprix e")
public class Evenementprix implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String evenementprixid;

	private String evenementprixcode;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evenementprixdatecreation;

	private String evenementprixlibelle;

	//bi-directional many-to-one association to Evenement
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="evenementid")
	private Evenement evenement;

	//bi-directional many-to-one association to Prix
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prixid")
	private Prix prix;

	public Evenementprix() {
	}

	public String getEvenementprixcode() {
		return this.evenementprixcode;
	}

	public void setEvenementprixcode(String evenementprixcode) {
		this.evenementprixcode = evenementprixcode;
	}

	public Date getEvenementprixdatecreation() {
		return this.evenementprixdatecreation;
	}

	public void setEvenementprixdatecreation(Date evenementprixdatecreation) {
		this.evenementprixdatecreation = evenementprixdatecreation;
	}

	public String getEvenementprixid() {
		return this.evenementprixid;
	}

	public void setEvenementprixid(String evenementprixid) {
		this.evenementprixid = evenementprixid;
	}

	public String getEvenementprixlibelle() {
		return this.evenementprixlibelle;
	}

	public void setEvenementprixlibelle(String evenementprixlibelle) {
		this.evenementprixlibelle = evenementprixlibelle;
	}

	public Evenement getEvenement() {
		return this.evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public Prix getPrix() {
		return this.prix;
	}

	public void setPrix(Prix prix) {
		this.prix = prix;
	}

}