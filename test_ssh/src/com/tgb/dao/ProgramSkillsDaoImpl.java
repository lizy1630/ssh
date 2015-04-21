package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.Student;
import com.tgb.entity.User_T;
import com.tgb.entity.survey.ProgramLanguages;
import com.tgb.entity.survey.ProgramSkills;
import com.tgb.entity.survey.ProgramSkillsPK;
import com.tgb.entity.survey.SurveyLevels;
import com.tgb.extras.ProgSkillsVO;

public class ProgramSkillsDaoImpl implements ProgramSkillsDao{

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	

	@Override
	public void addProgramSKills(ProgramSkills progSkills) {
		sessionFactory.getCurrentSession().save(progSkills);
	}


	@Override
	public ProgramSkills getProgramSkills(ProgramSkillsPK programSkillsPK) {
		
		String hql = "from ProgramSkills u where u.progSkillsPk.student=? and u.progSkillsPk.programLanguages=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setEntity(0, programSkillsPK.getStudent());
		query.setEntity(1, programSkillsPK.getProgramLanguages());
		
		return (ProgramSkills)query.uniqueResult();
	}


	@Override
	public List<ProgramSkills> getAllProgramSkills() {

		String hql = "from ProgramSkills";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}


	@Override
	public boolean delProgramSkills(String id) {
		
		String hql = "delete ProgramSkills u where u.progSkillsPk.student.stu_num= ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}


	@Override
	public boolean updateProgramSkills(Student student, ProgramLanguages progLanguages, SurveyLevels surveyLevels) {

		String hql = "update ProgramSkills u set u.surveyLevels=? where  u.progSkillsPk.student=? and u.progSkillsPk.programLanguages=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setEntity(0,surveyLevels);
		query.setEntity(1, student);
		query.setEntity(2, progLanguages);
		
		return (query.executeUpdate() > 0);
	}



}
