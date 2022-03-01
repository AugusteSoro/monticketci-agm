package com.wizard.monticketci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import com.wizard.monticketci.dao.UtilisateurDao;
import com.wizard.monticketci.entities.Utilisateur;
import com.wizard.monticketci.service.common.AbstractService;

@Service
@Transactional
public class UtilisateurServiceImpl extends AbstractService<Utilisateur> implements UtilisateurService {
	
	@Autowired
	UtilisateurDao dao;

	@Override
	protected PagingAndSortingRepository<Utilisateur, String> getDao() {
		return dao;
	}

	@Override
	public Utilisateur getAccountEnableByLogin(String login) {
		return dao.getAccountEnableByLogin(login);
	}

	@Override
	public Utilisateur findByUtilisateuremail(String email) {
		return dao.findByUtilisateuremail(email);
	}
	
	@Override
	public long count() {
		return dao.count();
	}

	@Override
	public Utilisateur findByUtilisateurcode(String code) {
		return dao.findByUtilisateurcode(code);
	}

	@Override
	public Utilisateur findByUtilisateurtelephone(String tel) {
		return dao.findByUtilisateurtelephone(tel);
	}
	
	@Override
	public List<Utilisateur> findAll() {
		return dao.findAll(Sort.by(Sort.Direction.DESC, "utilisateurdatecreation"));
	}

}
