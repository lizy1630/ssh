package com.tgb.manager;

import java.util.List;

import com.tgb.dao.ProgramSkillsDao;
import com.tgb.dao.UserDao;
import com.tgb.entity.Student;
import com.tgb.entity.User_T;
import com.tgb.entity.survey.ProgramLanguages;
import com.tgb.entity.survey.ProgramSkills;
import com.tgb.entity.survey.ProgramSkillsPK;
import com.tgb.entity.survey.SurveyLevels;

public class ProgramSkillsManagerImpl implements ProgramSkillsManager {

	private ProgramSkillsDao programSkillsDao;
	


	public void setProgramSkillsDao(ProgramSkillsDao programSkillsDao) {
		this.programSkillsDao = programSkillsDao;
	}



	@Override
	public void addProgramSkills(ProgramSkills programSkills) {
		programSkillsDao.addProgramSKills(programSkills);
		
	}



	@Override
	public ProgramSkills getProgramSkills(ProgramSkillsPK programSkillsPK) {

		return programSkillsDao.getProgramSkills(programSkillsPK);
	}



	@Override
	public List<ProgramSkills> getAllProgramSkills() {
		
		return programSkillsDao.getAllProgramSkills();
	}



	@Override
	public boolean delProgramSkills(String id) {
		
		return programSkillsDao.delProgramSkills(id);
	}



	@Override
	public boolean updateProgramSkills(Student student, ProgramLanguages progLanguages, SurveyLevels surveyLevels) {

		return programSkillsDao.updateProgramSkills(student, progLanguages, surveyLevels);
	}

	
}
