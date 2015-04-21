package com.tgb.manager;

import java.util.List;

import com.tgb.dao.SurveyLevelsDao;
import com.tgb.entity.survey.SurveyLevels;

public class SurveyLevelsManagerImpl implements SurveyLevelsManager {

	private SurveyLevelsDao surveyLevelsDao;
	
	

	public void setSurveyLevelsDao(SurveyLevelsDao surveyLevelsDao) {
		this.surveyLevelsDao = surveyLevelsDao;
	}

	@Override
	public SurveyLevels getSurveyLevels(String levelId) {
		return surveyLevelsDao.getSurveyLevels(levelId);
	}

	@Override
	public List<SurveyLevels> getAllSurveyLevels() {

		return surveyLevelsDao.getAllSurveyLevels();
	}

	@Override
	public void addSurveyLevels(SurveyLevels surveyLevels) {
		
		surveyLevelsDao.addSurveyLevels(surveyLevels);
		
	}

	@Override
	public boolean delSurveyLevels(String levelId) {
		
		return surveyLevelsDao.delSurveyLevels(levelId);
	}

	@Override
	public boolean updateSurveyLevels(SurveyLevels surveyLevels) {
		
		return surveyLevelsDao.updateSurveyLevels(surveyLevels);
	}



}
