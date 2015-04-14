package com.tgb.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.tgb.entity.intern.History;
import com.tgb.entity.intern.Project;

@Entity
@Table(name="student")
public class Student {

	@Id
	@Column(length=11)
	private int stu_num;
	
	@Column(length=20)
	private String fname;
	
	@Column(length=20)
	private String mname;

	@Column(length=20)
	private String lname;

	@Column(length=20)
	private String email;

	@Column(length=11)
	private String tel;
	
	@Column(length=10)
	private String reg_semester;
	
	@Column(length=10)
	private String reg_year;
	
	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean is_native;
	
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "student", cascade = CascadeType.ALL)
	private History history;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proj_id", nullable = true)
	private Project project;
	
	
	/**************************************************GETTERS AND SETTERS**********************************************************************************/
	
	public int getStu_num() {
		return stu_num;
	}

	public void setStu_num(int stu_num) {
		this.stu_num = stu_num;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
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

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	
}
