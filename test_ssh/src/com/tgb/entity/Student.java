package com.tgb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="student")
public class Student {

	
	@Column(length=11)
	private int id;
	
	@Column(length=20)
	private String fname;
	
	@Column(length=20)
	private String mname;

	@Column(length=20)
	private String lname;

	@Column(length=20)
	private String email;

	@Column(length=11)
	private int tel;
	
	@Column(length=10)
	private String reg_semester;
	
	@Column(length=10)
	private String reg_year;
	
	@Column(length=1)
	private boolean is_native;
	
	@Column(length=11)
	private int history_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTel() {
		return tel;
	}

	public void setTel(int tel) {
		this.tel = tel;
	}

	public String getReg_semester() {
		return reg_semester;
	}

	public void setReg_semester(String reg_semester) {
		this.reg_semester = reg_semester;
	}

	public String getReg_year() {
		return reg_year;
	}

	public void setReg_year(String reg_year) {
		this.reg_year = reg_year;
	}

	public boolean isIs_native() {
		return is_native;
	}

	public void setIs_native(boolean is_native) {
		this.is_native = is_native;
	}

	public int getHistory_id() {
		return history_id;
	}

	public void setHistory_id(int history_id) {
		this.history_id = history_id;
	}
	

	
}
