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
 * The persistent class for the prix database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Prix.findAll", query="SELECT p FROM Prix p")
public class Prix implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String prixid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date prixdatecreation;

	private Boolean prixenable;

	private String prixstatut;
	
	private String prixlibelle;
	
	private String prixdescription;

	private Integer prixvaleur;
	
	//bi-directional many-to-one association to Superviseur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="superviseurid")
	private Superviseur superviseur;

	//bi-directional many-to-one association to Evenementprix
	@OneToMany(mappedBy="prix")
	@JsonIgnore
	private List<Evenementprix> evenementprixs;

	public Prix() {
	}

	public String getPrixid() {
		return this.prixid;
	}

	public void setPrixid(String prixid) {
		this.prixid = prixid;
	}

	public Date getPrixdatecreation() {
		return this.prixdatecreation;
	}

	public void setPrixdatecreation(Date prixdatecreation) {
		this.prixdatecreation = prixdatecreation;
	}

	public Boolean getPrixenable() {
		return this.prixenable;
	}

	public void setPrixenable(Boolean prixenable) {
		this.prixenable = prixenable;
	}

	public String getPrixstatut() {
		return this.prixstatut;
	}

	public void setPrixstatut(String prixstatut) {
		this.prixstatut = prixstatut;
	}

	public Integer getPrixvaleur() {
		return this.prixvaleur;
	}

	public void setPrixvaleur(Integer prixvaleur) {
		this.prixvaleur = prixvaleur;
	}

	public List<Evenementprix> getEvenementprixs() {
		return this.evenementprixs;
	}

	public void setEvenementprixs(List<Evenementprix> evenementprixs) {
		this.evenementprixs = evenementprixs;
	}

	public Evenementprix addEvenementprix(Evenementprix evenementprix) {
		getEvenementprixs().add(evenementprix);
		evenementprix.setPrix(this);

		return evenementprix;
	}

	public Evenementprix removeEvenementprix(Evenementprix evenementprix) {
		getEvenementprixs().remove(evenementprix);
		evenementprix.setPrix(null);

		return evenementprix;
	}

}