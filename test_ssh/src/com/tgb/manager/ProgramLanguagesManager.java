package com.tgb.manager;

import java.util.List;

import com.tgb.entity.survey.ProgramLanguages;

public interface ProgramLanguagesManager {

	public ProgramLanguages getProgramLanguages(String plCode);
	
	public List<ProgramLanguages> getAllProgramLanguages();
	
	public void addProgramLanguages(ProgramLanguages programLanguages);
	
	public boolean delProgramLanguages(String plCode);
	
	public boolean updateProgramLanguages(ProgramLanguages programLanguages);
}
