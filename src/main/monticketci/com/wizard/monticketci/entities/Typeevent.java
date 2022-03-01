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
 * The persistent class for the typeevent database table.
 * 
 */
@Entity
@Data
@NamedQuery(name="Typeevent.findAll", query="SELECT t FROM Typeevent t")
public class Typeevent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(unique=true, nullable=false, length=128)
	private String typeeventid;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date typeeventdatecreation;

	private String typeeventdescritpion;

	private Boolean typeeventenable;

	private String typeeventicon;

	private String typeeventnom;

	private String typeeventstatut;
	
	private String typeeventpathimage;
	
	private String typeeventlogo;
	
	//bi-directional many-to-one association to Superviseur
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="superviseurid")
	private Superviseur superviseur;

	//bi-directional many-to-one association to Evenement
	@OneToMany(mappedBy="typeevent")
	@JsonIgnore
	private List<Evenement> evenements;

	public Typeevent() {
	}

}