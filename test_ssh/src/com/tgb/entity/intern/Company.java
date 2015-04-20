package com.tgb.entity.intern;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="company")
public class Company {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(length=32)
	private String com_code;
	
	@Column(length=50)
	private String name;
	
	@Column(length=100)
	private String address;
	
	@Column(length=25)
	private String city;
	
	@Column(length=25)
	private String post_code;
	
	@Column(length=20)
	private String tel;
	
	@Column(length=50)
	private String email;
	
	@Column(length=50)
	private String fax;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Com_Contact_Person> com_contact_persons;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
	private Set<Job> jobs;
	
	public String getCom_code() {
		return com_code;
	}

	public void setCom_code(String com_code) {
		this.com_code = com_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	public Set<Com_Contact_Person> getCom_contact_persons() {
		return com_contact_persons;
	}

	public void setCom_contact_persons(Set<Com_Contact_Person> com_contact_persons) {
		this.com_contact_persons = com_contact_persons;
	}


	
	
	
	
}
