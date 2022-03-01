package com.wizard.monticketci.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.common.IOperations;
import com.wizard.monticketci.entities.Reservation;

@Service
public interface ReservationService extends IOperations<Reservation> {
	
	List<Reservation> findAll();
	Reservation findByReservationcodereference(String codereference);
	List<Reservation> findByUtilisateurid(String utilisateurid);
	List<Reservation> findByUtilisateuridAndDesttelisNull(String utilisateurid);
	long countByUtilisateuridAndDesttelisNull(String utilisateurid);
	long countByUtilisateuridAndDesttelisNullAndStatutSucces(String utilisateurid);
	List<Reservation> findByUtilisateuridAndDesttelisNotNull(String utilisateurid);
	List<Reservation> findByResdestinatairetel(String utilisateurtel);
	List<Reservation> findByEvenementid(String evenementid);
	List<Reservation> findByEvenementidAndStatut(String evenementid, String statut);
	List<Reservation> findByEvenementidAndStatutAndChecked(String evenementid, String statut, boolean ischecked);
	long count();

}
