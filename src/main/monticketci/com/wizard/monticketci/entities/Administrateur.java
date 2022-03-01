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
 * The persistent class for the administrateur database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Administrateur.findAll", query="SELECT a FROM Administrateur a")
public class Administrateur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String administrateurid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date administrateurdatecreation;

	private String administrateuremail;

	private Boolean administrateurenable;

	private Boolean administrateurisdeleted;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date administrateurlastcon;

	private String administrateurnom;

	private String administrateurpassword;

	private String administrateurprenom;

	private String administrateurtelephone;
	
	private String administrateurtype;
	
	private String administrateurstatut;

	private String administrateurtoken;

	//bi-directional many-to-one association to Localisation
	@OneToMany(mappedBy="administrateur")
	@JsonIgnore
	private List<Localisation> localisations;


	public Administrateur() {
	}


}