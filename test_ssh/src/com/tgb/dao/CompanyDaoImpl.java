package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.intern.Company;

public class CompanyDaoImpl implements CompanyDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Company getCompany(String id) {
		
		String hql = "from Company u where u.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (Company)query.uniqueResult();
	}

	@Override
	public List<Company> getAllCompany() {
		
		String hql = "from Company";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}

	@Override
	public void addCompany(Company company) {
		sessionFactory.getCurrentSession().save(company);
	}

	@Override
	public boolean delCompany(String id) {
		
		String hql = "delete Company u where u.company_code = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateCompany(Company company) {
		
		String hql = "update Company u set u.name = ?,u.address=?, u.city=?, u.post_code=?, u.tel=?, "
				+ " email =?, fax=?  where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, company.getName());
		query.setString(1, company.getAddress());
		query.setString(2, company.getCity());
		query.setString(3, company.getPost_code());
		query.setString(4, company.getTel());
		query.setString(5, company.getEmail());
		query.setString(6, company.getFax());
		query.setString(7, company.getCom_code());
		
		return (query.executeUpdate() > 0);
	}

}
