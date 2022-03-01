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
 * The persistent class for the lieu database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Lieu.findAll", query="SELECT l FROM Lieu l")
public class Lieu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String lieuid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lieudatecreation;

	private String lieudescription;

	private Boolean lieuenable;

	private String lieunom;

	private String lieupays;

	private String lieustatut;

	private String lieuville;
	
	private String lieucommune;
	
	//bi-directional many-to-one association to Superviseur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="superviseurid")
	private Superviseur superviseur;

	//bi-directional many-to-one association to Evenementlieu
	@OneToMany(mappedBy="lieu")
	@JsonIgnore
	private List<Evenementlieu> evenementlieus;

	public Lieu() {
	}

}