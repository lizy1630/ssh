package com.tgb.manager;

import java.util.List;

import com.tgb.dao.CompanyDao;
import com.tgb.entity.intern.Company;

public class CompanyManagerImpl implements CompanyManager {

	private CompanyDao companyDao;
	
	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	@Override
	public Company getCompany(String id) {
		return companyDao.getCompany(id);
	}

	@Override
	public List<Company> getAllCompany() {
		return companyDao.getAllCompany();
	}

	@Override
	public void addCompany(Company company) {
		companyDao.addCompany(company);
	}

	@Override
	public boolean delCompany(String id) {
		
		return companyDao.delCompany(id);
	}

	@Override
	public boolean updateCompany(Company company) {
		return companyDao.updateCompany(company);
	}

}
