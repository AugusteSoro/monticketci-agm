package com.wizard.monticketci.entities;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;


/**
 * The persistent class for the caissier database table.
 * 
 */
@Data
@Entity
@NamedQuery(name="Caissier.findAll", query="SELECT c FROM Caissier c")
public class Caissier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String caissierid;

	private String caissiercivilite;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date caissierdatecreation;

	private String caissieremail;

	private Boolean caissierenable;

	private Boolean caissierisdeleted;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date caissierlastcon;

	private String caissiernom;

	private String caissierpassword;

	private String caissierprenom;

	private String caissiertelephone;
	
	private String caissiercode;

	private String caissiertoken;
	
	private String caissierstatut;
	
	//bi-directional many-to-one association to Superviseur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="superviseurid")
	private Superviseur superviseur;

	public Caissier() {
	}

	

}