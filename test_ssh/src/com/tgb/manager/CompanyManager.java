package com.tgb.manager;

import java.util.List;

import com.tgb.entity.intern.Company;

public interface CompanyManager {

	public Company getCompany(String id);
	
	public List<Company> getAllCompany();
	
	public void addCompany(Company company);
	
	public boolean delCompany(String id);
	
	public boolean updateCompany(Company company);
}
