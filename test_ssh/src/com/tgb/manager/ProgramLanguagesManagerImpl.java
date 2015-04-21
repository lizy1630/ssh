package com.tgb.manager;

import java.util.List;

import com.tgb.dao.ProgramLanguagesDao;
import com.tgb.dao.UserDao;
import com.tgb.entity.survey.ProgramLanguages;

public class ProgramLanguagesManagerImpl implements ProgramLanguagesManager {

	private ProgramLanguagesDao programLanguagesDao;
	
	public void setProgramLanguagesDao(ProgramLanguagesDao programLanguagesDao) {
		this.programLanguagesDao = programLanguagesDao;
	}

	@Override
	public ProgramLanguages getProgramLanguages(String plCode) {
		return programLanguagesDao.getProgramLanguages(plCode);
	}

	@Override
	public List<ProgramLanguages> getAllProgramLanguages() {

		return programLanguagesDao.getAllProgramLanguages();
	}

	@Override
	public void addProgramLanguages(ProgramLanguages programLanguages) {
		
		programLanguagesDao.addProgramLanguages(programLanguages);
		
	}

	@Override
	public boolean delProgramLanguages(String plCode) {
		
		return programLanguagesDao.delProgramLanguages(plCode);
	}

	@Override
	public boolean updateProgramLanguages(ProgramLanguages programLanguages) {
		
		return programLanguagesDao.updateProgramLanguages(programLanguages);
	}


}
