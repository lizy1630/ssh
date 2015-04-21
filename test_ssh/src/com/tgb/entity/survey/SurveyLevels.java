package com.tgb.entity.survey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name="survey_levels")
public class SurveyLevels {

	@Id
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(name="LEVEL_ID", length=32)
	private String levelID;
	
	@Column(name="TABLE_NAME", length=50)
	private String tableName;
	
	@Column(name="LEVEL_NAME", length=50)
	private String levelName;

	
	/**************************************************GETTERS AND SETTERS**********************************************************************************/
	
	public String getLevelID() {
		return levelID;
	}

	public void setLevelID(String levelID) {
		this.levelID = levelID;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	
}
