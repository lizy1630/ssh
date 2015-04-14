package com.tgb.entity.intern;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="com_Contact_Person")
public class Com_Contact_Person {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name = "system-uuid",strategy="uuid")
	@Column(length=11)
	private int cp_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_code", nullable = false)
	private Company company;
	
	@Column(length=50)
	private String cp_fname;
	
	@Column(length=50)
	private String cp_lname;
	
	@Column(length=200)
	private String cp_positon;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "com_contact_person")
	private Set<Contact_History> contact_histories;

	public int getCp_id() {
		return cp_id;
	}

	public void setCp_id(int cp_id) {
		this.cp_id = cp_id;
	}

	public String getCp_fname() {
		return cp_fname;
	}

	public void setCp_fname(String cp_fname) {
		this.cp_fname = cp_fname;
	}

	public String getCp_lname() {
		return cp_lname;
	}

	public void setCp_lname(String cp_lname) {
		this.cp_lname = cp_lname;
	}

	public String getCp_positon() {
		return cp_positon;
	}

	public void setCp_positon(String cp_positon) {
		this.cp_positon = cp_positon;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Contact_History> getContact_histories() {
		return contact_histories;
	}

	public void setContact_histories(Set<Contact_History> contact_histories) {
		this.contact_histories = contact_histories;
	}


}
