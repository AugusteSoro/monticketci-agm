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
 * The persistent class for the evenement database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Evenement.findAll", query="SELECT e FROM Evenement e")
public class Evenement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String evenementid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evenementdatecreation;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date evenementdateevenement;

	private String evenementdescription;

	private Boolean evenementenable;

	private String evenementnom;

	private String evenementstatut;
	
	private String evenementpathimage;
	
	private String evenementlogo;	
	
	private int evenementqteglobal;
	
	private int evenementqtemticket;

	private int evenementqteprise;

	//bi-directional many-to-one association to Typeevent
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="typeeventid")
	private Typeevent typeevent;
	
	//bi-directional many-to-one association to Superviseur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="superviseurid")
	private Superviseur superviseur;
	
	//bi-directional many-to-one association to Localisation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="localisationid")
	private Localisation localisation;

	//bi-directional many-to-one association to Evenementlieu
	@JsonIgnore
	@OneToMany(mappedBy="evenement")
	private List<Evenementlieu> evenementlieus;

	//bi-directional many-to-one association to Evenementprix
	@OneToMany(mappedBy="evenement")
	@JsonIgnore
	private List<Evenementprix> evenementprixs;

	public Evenement() {
	}

	

}