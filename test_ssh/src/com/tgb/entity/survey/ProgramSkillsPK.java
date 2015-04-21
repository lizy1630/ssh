package com.tgb.entity.survey;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tgb.entity.Student;





@Embeddable
public class ProgramSkillsPK implements Serializable{
	
	
//		@Column(name = "STU_NUM")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "STU_NUM")
	private Student student;
		
//	    @Column(name = "PL_CODE")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "PL_CODE")
	private ProgramLanguages programLanguages;

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public ProgramLanguages getProgramLanguages() {
		return programLanguages;
	}

	public void setProgramLanguages(ProgramLanguages programLanguages) {
		this.programLanguages = programLanguages;
	}
	
	
	
	
}
