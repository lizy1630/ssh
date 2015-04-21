package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.Student;

public class StudentDaoImpl implements StudentDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Student getStudent(String id) {
		
		String hql = "from Student u where u.stu_num=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (Student)query.uniqueResult();
	}

	@Override
	public List<Student> getAllStudent() {
		
		String hql = "from Student";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		
		return query.list();
	}

//	@Override
//	public void addUser(User_T user) {
//		sessionFactory.getCurrentSession().save(user);
//	}
//
//	@Override
//	public boolean delUser(String id) {
//		
//		String hql = "delete User_T u where u.id = ?";
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setString(0, id);
//		
//		return (query.executeUpdate() > 0);
//	}
//
//	@Override
//	public boolean updateUser(User_T user) {
//		
//		String hql = "update User_T u set u.userName = ?,u.age=? where u.id = ?";
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setString(0, user.getUserName());
//		query.setString(1, user.getAge());
//		query.setString(2, user.getId());
//		
//		return (query.executeUpdate() > 0);
//	}

}
