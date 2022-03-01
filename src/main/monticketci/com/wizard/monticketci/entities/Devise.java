package com.wizard.monticketci.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * The persistent class for the Devise database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Devise.findAll", query="SELECT d FROM Devise d")
public class Devise implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String deviseid;

	private String devisecode;
	
	private String devisenom;
	
	private String devisesymbole;
	
	private String deviseicon;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date devisedatecreation;

	private Boolean deviseenable;
	
	private String devisestatut;

	//bi-directional many-to-one association to Utilisateur
	@OneToMany(mappedBy="devise")
	@JsonIgnore
	private List<Localisation> localisations;
	
	public Devise() {
	}

}
