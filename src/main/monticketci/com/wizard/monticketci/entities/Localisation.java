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
 * The persistent class for the localisation database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Localisation.findAll", query="SELECT l FROM Localisation l")
public class Localisation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String localisationid;

	private int localisationcode;
	
	private int localisationnbredigittel;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date localisationdatecreation;

	private Boolean localisationenable;

	private String localisationicon;

	private String localisationnom;

	private String localisationstatut;

	//bi-directional many-to-one association to Administrateur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="administrateurid")
	private Administrateur administrateur;
	
	//bi-directional many-to-one association to Devise
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="deviseid")
	private Devise devise;

	//bi-directional many-to-one association to Utilisateur
	@OneToMany(mappedBy="localisation")
	@JsonIgnore
	private List<Utilisateur> utilisateurs;

	public Localisation() {
	}


}