package com.wizard.monticketci.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.wizard.monticketci.entities.Reservation;


public interface ReservationDao extends JpaRepository<Reservation, String> {
	
	long count();
	
	Reservation findByReservationcodereference(String codereference);
	
	List<Reservation> findByUtilisateurUtilisateuridOrderByReservationdatecreationDesc(String utilisateurid);
	
	List<Reservation> findByUtilisateurUtilisateuridAndResdestinatairetelIsNullOrderByReservationdatecreationDesc(String utilisateurid);
	
	long countByUtilisateurUtilisateuridAndResdestinatairetelIsNullOrderByReservationdatecreationDesc(String utilisateurid);
	
	long countByUtilisateurUtilisateuridAndResdestinatairetelIsNullAndReservationpaiementstatutOrderByReservationdatecreationDesc(String utilisateurid, String statut);
	
	List<Reservation> findByUtilisateurUtilisateuridAndResdestinatairetelIsNotNullOrderByReservationdatecreationDesc(String utilisateurid);
	
	List<Reservation> findByResdestinatairetel(String utilisateurid);
	
	List<Reservation> findByResevenementid(@Param("resevenementid") String resevenementid);
	
	List<Reservation> findByResevenementidAndReservationpaiementstatutIgnoreCase(@Param("resevenementid") String resevenementid, @Param("statut") String statut);
	
	List<Reservation> findByResevenementidAndReservationpaiementstatutIgnoreCaseAndResticketischeckedTrue(@Param("resevenementid") String resevenementid, @Param("statut") String statut);
	
	List<Reservation> findByResevenementidAndReservationpaiementstatutIgnoreCaseAndResticketischeckedFalse(@Param("resevenementid") String resevenementid, @Param("statut") String statut);

}
