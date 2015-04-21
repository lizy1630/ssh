package com.tgb.entity.survey;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;



@Entity
@Table(name="program_languages")

public class ProgramLanguages {
	
	
	@Id
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name="PL_CODE", length=32)
	private String plCode;
	
	@Column(name="PL_NAME", length=20)
	private String plName;
	
	@Column(name="PL_DESCRIPTION", length=200)
	private String plDescription;

	/**************************************************GETTERS AND SETTERS**********************************************************************************/
	
	public String getPlCode() {
		return plCode;
	}

	public void setPlCode(String plCode) {
		this.plCode = plCode;
	}

	public String getPlName() {
		return plName;
	}

	public void setPlName(String plName) {
		this.plName = plName;
	}

	public String getPlDescription() {
		return plDescription;
	}

	public void setPlDescription(String plDescription) {
		this.plDescription = plDescription;
	}
		

	
}
