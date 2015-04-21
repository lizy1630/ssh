package com.tgb.manager;

import java.util.List;

import com.tgb.entity.survey.SurveyLevels;

public interface SurveyLevelsManager {

	public SurveyLevels getSurveyLevels(String levelId);
	
	public List<SurveyLevels> getAllSurveyLevels();
	
	public void addSurveyLevels(SurveyLevels surveyLevels);
	
	public boolean delSurveyLevels(String levelId);
	
	public boolean updateSurveyLevels(SurveyLevels surveyLevels);
}
