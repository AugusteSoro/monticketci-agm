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
 * The persistent class for the superviseur database table.
 * 
 */
@Data
@Entity
@NamedQuery(name="Superviseur.findAll", query="SELECT s FROM Superviseur s")
public class Superviseur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String superviseurid;

	private String superviseurresidence;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date superviseurdatecreation;

	private String superviseuremail;

	private Boolean superviseurenable;

	private Boolean superviseurisdeleted;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date superviseurlastcon;

	private String superviseurnom;

	private String superviseurpassword;

	private String superviseurprenom;

	private String superviseurtelephone;

	private String superviseurtoken;
	
	private String superviseurstatut;
	
	private String superviseurtype;
	
	private String superviseurraisonsocial;
	
	/**/
	
	private String superviseursigle;
	
	private String superviseurtitre;
	
	private String superviseuractivite;
	
	private String superviseurformejuridique;
	
	private String superviseurpays;
	
	private String superviseurville;
		
	private String superviseuremailrepresentant;
	
	private String superviseurtelephonerepresentant;
	
	private String superviseurfonction;
	
	private Boolean superviseuractivitehorsci;
	
	private String superviseurpaysactivite;
	
	private Boolean superviseurmonticketcihorsci;

	
	//bi-directional many-to-one association to Admin
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="administrateurid")
	private Administrateur administrateurid;
	
	//bi-directional many-to-one association to Typeevent
	@OneToMany(mappedBy="superviseur")
	@JsonIgnore
	private List<Typeevent> typeevents;

	public Superviseur() {
	}


}