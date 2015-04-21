package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.survey.ProgramLanguages;

public class ProgramLanguagesDaoImpl implements ProgramLanguagesDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public ProgramLanguages getProgramLanguages(String plCode) {
		
		String hql = "from ProgramLanguages u where u.plCode=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, plCode);
		
		return (ProgramLanguages)query.uniqueResult();
	}

	@Override
	public List<ProgramLanguages> getAllProgramLanguages() {
		
		String hql = "from ProgramLanguages";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
		
	}

	@Override
	public void addProgramLanguages(ProgramLanguages programLanguages) {
		
		sessionFactory.getCurrentSession().save(programLanguages);
		
	}

	@Override
	public boolean delProgramLanguages(String plCode) {

		String hql = "delete ProgramLanguages u where u.plCode = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, plCode);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateProgramLanguages(ProgramLanguages programLanguages) {
		

		String hql = "update ProgramLanguages u set u.plName = ?,u.plDescription=? where u.plCode = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, programLanguages.getPlName());
		query.setString(1, programLanguages.getPlDescription());
		query.setString(2, programLanguages.getPlCode());
		
		return (query.executeUpdate() > 0);
	}


}
