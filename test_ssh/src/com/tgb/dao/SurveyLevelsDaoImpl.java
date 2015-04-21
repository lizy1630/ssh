package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.User_T;
import com.tgb.entity.survey.SurveyLevels;

public class SurveyLevelsDaoImpl implements SurveyLevelsDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public SurveyLevels getSurveyLevels(String id) {
		
		String hql = "from SurveyLevels u where u.levelID=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (SurveyLevels)query.uniqueResult();
	}

	@Override
	public List<SurveyLevels> getAllSurveyLevels() {
		
		String hql = "from SurveyLevels";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}

	@Override
	public void addSurveyLevels(SurveyLevels surveyLevels) {

		sessionFactory.getCurrentSession().save(surveyLevels);
		
	}

	@Override
	public boolean delSurveyLevels(String levelId) {
	
		String hql = "delete SurveyLevels u where u.levelID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, levelId);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateSurveyLevels(SurveyLevels surveyLevels) {
		
		String hql = "update SurveyLevels u set u.tableName = ?,u.levelName=? where u.levelID = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, surveyLevels.getTableName());
		query.setString(1, surveyLevels.getLevelName());
		query.setString(2, surveyLevels.getLevelID());
		
		return (query.executeUpdate() > 0);
	}



}
