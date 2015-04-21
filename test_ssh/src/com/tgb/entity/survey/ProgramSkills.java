package com.tgb.entity.survey;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tgb.entity.Student;



@Entity
@Table(name="program_skills")

public class ProgramSkills {
	
	@EmbeddedId ProgramSkillsPK progSkillsPk;
	
//	@MapsId("stuId")
//	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	@JoinColumn(name = "STU_NUM")
//	private Student student;
	
//	@MapsId("plId")
//	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	@JoinColumn(name = "PL_CODE")
//	private ProgramLanguages programLanguages;
	
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "LEVEL_ID")
	private SurveyLevels surveyLevels;

	

	/**************************************************GETTERS AND SETTERS**********************************************************************************/
	
//	public Student getStudent() {
//		return progSkillsPk.getStudent();
//	}
//	
//	public void setStudent(Student student) {
//		this.progSkillsPk.setStudent(student);
//	}
//	
//	public ProgramLanguages getProgramLanguages() {
//		return progSkillsPk.getProgramLanguages();
//	}
//	
//	public void setProgramLanguages(ProgramLanguages programLanguages) {
//		this.progSkillsPk.setProgramLanguages(programLanguages);
//	}
	
	
	
	public SurveyLevels getSurveyLevels() {
		return surveyLevels;
	}
	
	public void setSurveyLevels(SurveyLevels surveyLevels) {
		this.surveyLevels = surveyLevels;
	}

	public ProgramSkillsPK getProgSkillsPk() {
		return progSkillsPk;
	}

	public void setProgSkillsPk(ProgramSkillsPK progSkillsPk) {
		this.progSkillsPk = progSkillsPk;
	}
	
}


