package com.tgb.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.tgb.entity.TableID;
import com.tgb.entity.User_T;

public class TableIdDaoImpl implements TableIdDao {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public TableID getTable(String tableName) {
		
		String hql = "from TableID u where u.tbaleName=?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, tableName);
		
		return (TableID)query.uniqueResult();
	}

	@Override
	public boolean updateTableId(TableID tableID) {
		
		String hql = "update TableID u set u.maxId=? where u.tbaleName = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, tableID.getMaxId());
		query.setString(1, tableID.getTbaleName());

		return (query.executeUpdate() > 0);
	}
	
	

}
