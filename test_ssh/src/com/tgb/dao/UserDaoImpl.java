package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.intern.User;

public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public User getUser(String id) {
		
		String hql = "from User u where u.id=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (User)query.uniqueResult();
	}

	@Override
	public List<User> getAllUser() {
		
		String hql = "from User";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}

	@Override
	public void addUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public boolean delUser(String id) {
		
		String hql = "delete User u where u.user_id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public boolean updateUser(User user) {
		
		String hql = "update User u set u.fname = ?,u.lname=?, u.user_position=?, u.school=?, u.tel=?, "
				+ "extension=?, mobile = ?, email =?, notes=?  where u.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, user.getFname());
		query.setString(1, user.getLname());
		query.setString(2, user.getUser_position());
		query.setString(3, user.getSchool());
		query.setString(4, user.getTel());
		query.setString(5, user.getExtension());
		query.setString(6, user.getMobile());
		query.setString(7, user.getEmail());
		query.setString(8, user.getNotes());
		query.setString(9, user.getUser_id());
		
		return (query.executeUpdate() > 0);
	}

}
