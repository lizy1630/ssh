package com.tgb.entity.intern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="job")
public class Job {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(length=32)
	private String job_code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_id", nullable = false)
	private Company company;
	
	@Column(length=300)
	private String description;
	
	@Column(length=300)
	private String responsibilities;
	
	@Column(length=300)
	private String requirements;
	
	@Column(length=11)
	private int position_num;
	
	@Column(length=50)
	private String job_postion;

	public String getJob_code() {
		return job_code;
	}

	public void setJob_code(String job_code) {
		this.job_code = job_code;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(String responsibilities) {
		this.responsibilities = responsibilities;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public int getPosition_num() {
		return position_num;
	}

	public void setPosition_num(int position_num) {
		this.position_num = position_num;
	}

	public String getJob_postion() {
		return job_postion;
	}

	public void setJob_postion(String job_postion) {
		this.job_postion = job_postion;
	}

}
