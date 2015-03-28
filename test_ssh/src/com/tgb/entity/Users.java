package com.tgb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="USERS")
public class Users {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(length=11)
	private String USER_ID;
	
	@Column(length=50)
	private String FNAME;
	
	@Column(length=50)
	private String LNAME;
	
	@Column(length=50)
	private String USER_POSITION;
	
	@Column(length=50)
	private String SCHOOL;
	
	@Column(length=50)
	private String TEL;
	
	@Column(length=50)
	private String EXTENSION;
	
	@Column(length=50)
	private String MOBILE;
	
	@Column(length=25)
	private String EMAIL;

	@Column(length=300)
	private String NOTES;

	
}
