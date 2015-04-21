package com.tgb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="id_table")

public class TableID {

	
	@Column(name="MAX_ID", length=255)
	private String maxId;
	
	@Id
	@Column(name="TABLE_NAME", length=255)
	private String tbaleName;

	public String getMaxId() {
		return maxId;
	}

	public void setMaxId(String maxId) {
		this.maxId = maxId;
	}

	public String getTbaleName() {
		return tbaleName;
	}

	public void setTbaleName(String tbaleName) {
		this.tbaleName = tbaleName;
	}

	
	
}
