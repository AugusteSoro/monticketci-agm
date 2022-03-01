package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.ReservationDao;
import com.wizard.monticketci.entities.Reservation;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class ReservationServiceImpl extends AbstractService<Reservation> implements ReservationService {
	
	@Autowired
	ReservationDao dao;

	@Override
	protected PagingAndSortingRepository<Reservation, String> getDao() {
		return dao;
	}
	
	@Override
	public List<Reservation> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "reservationdatecreation"));
	}
	
	@Override
	public long count() {
		return dao.count();
	}

	@Override
	public Reservation findByReservationcodereference(String codereference) {
		return dao.findByReservationcodereference(codereference);
	}

	@Override
	public List<Reservation> findByUtilisateurid(String utilisateurid) {
		return dao.findByUtilisateurUtilisateuridOrderByReservationdatecreationDesc(utilisateurid);
	}

	@Override
	public List<Reservation> findByUtilisateuridAndDesttelisNull(String utilisateurid) {
		return dao.findByUtilisateurUtilisateuridAndResdestinatairetelIsNullOrderByReservationdatecreationDesc(utilisateurid);
	}
	
	@Override
	public long countByUtilisateuridAndDesttelisNull(String utilisateurid) {
		return dao.countByUtilisateurUtilisateuridAndResdestinatairetelIsNullOrderByReservationdatecreationDesc(utilisateurid);
	}
	
	@Override
	public long countByUtilisateuridAndDesttelisNullAndStatutSucces(String utilisateurid) {
		return dao.countByUtilisateurUtilisateuridAndResdestinatairetelIsNullAndReservationpaiementstatutOrderByReservationdatecreationDesc(utilisateurid, "SUCCES");
	}


	@Override
	public List<Reservation> findByUtilisateuridAndDesttelisNotNull(String utilisateurid) {
		return dao.findByUtilisateurUtilisateuridAndResdestinatairetelIsNotNullOrderByReservationdatecreationDesc(utilisateurid);
	}

	@Override
	public List<Reservation> findByResdestinatairetel(String utilisateurtel) {
		return dao.findByResdestinatairetel(utilisateurtel);
	}

	@Override
	public List<Reservation> findByEvenementid(String findByEvenementid) {
		return dao.findByResevenementid(findByEvenementid);
	}

	@Override
	public List<Reservation> findByEvenementidAndStatut(String findByEvenementid, String statut) {
		return dao.findByResevenementidAndReservationpaiementstatutIgnoreCase(findByEvenementid, statut);
	}

	@Override
	public List<Reservation> findByEvenementidAndStatutAndChecked(String evenementid, String statut,
			boolean ischecked) {
		if(ischecked) {
			return dao.findByResevenementidAndReservationpaiementstatutIgnoreCaseAndResticketischeckedTrue(evenementid, statut);

		}else {
			return dao.findByResevenementidAndReservationpaiementstatutIgnoreCaseAndResticketischeckedFalse(evenementid, statut);

		}
	}






}
