package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the pisteaudit database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Pisteaudit.findAll", query="SELECT p FROM Pisteaudit p")
public class Pisteaudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String pisteauditid;

	private String pisteauditcommentaire;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pisteauditdate;

	private String pisteauditlibelle;

	private String pisteauditstatut;

	private String pisteaudittype;

	private String userpisteaudit;

	public Pisteaudit() {
	}

	public String getPisteauditid() {
		return this.pisteauditid;
	}

	public void setPisteauditid(String pisteauditid) {
		this.pisteauditid = pisteauditid;
	}

	public String getPisteauditcommentaire() {
		return this.pisteauditcommentaire;
	}

	public void setPisteauditcommentaire(String pisteauditcommentaire) {
		this.pisteauditcommentaire = pisteauditcommentaire;
	}

	public Date getPisteauditdate() {
		return this.pisteauditdate;
	}

	public void setPisteauditdate(Date pisteauditdate) {
		this.pisteauditdate = pisteauditdate;
	}

	public String getPisteauditlibelle() {
		return this.pisteauditlibelle;
	}

	public void setPisteauditlibelle(String pisteauditlibelle) {
		this.pisteauditlibelle = pisteauditlibelle;
	}

	public String getPisteauditstatut() {
		return this.pisteauditstatut;
	}

	public void setPisteauditstatut(String pisteauditstatut) {
		this.pisteauditstatut = pisteauditstatut;
	}

	public String getPisteaudittype() {
		return this.pisteaudittype;
	}

	public void setPisteaudittype(String pisteaudittype) {
		this.pisteaudittype = pisteaudittype;
	}

	public String getUserpisteaudit() {
		return this.userpisteaudit;
	}

	public void setUserpisteaudit(String userpisteaudit) {
		this.userpisteaudit = userpisteaudit;
	}

}