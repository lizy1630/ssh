package com.tgb.dao;

import java.util.List;

import com.tgb.entity.Student;
import com.tgb.entity.User_T;
import com.tgb.entity.survey.ProgramLanguages;
import com.tgb.entity.survey.ProgramSkills;
import com.tgb.entity.survey.ProgramSkillsPK;
import com.tgb.entity.survey.SurveyLevels;

public interface ProgramSkillsDao {

	public ProgramSkills getProgramSkills(ProgramSkillsPK programSkillsPK);
	
	public List<ProgramSkills> getAllProgramSkills();
	
	public void addProgramSKills(ProgramSkills progSkills);
	
	public boolean delProgramSkills(String id);
	
	public boolean updateProgramSkills(Student student, ProgramLanguages progLanguages, SurveyLevels surveyLevels);
}
