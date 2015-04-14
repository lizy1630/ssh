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
	@Column(length=11)
	private int job_code;
	
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

}
