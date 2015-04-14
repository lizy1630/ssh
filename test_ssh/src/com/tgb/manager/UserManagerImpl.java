package com.tgb.manager;

import java.util.List;

import com.tgb.dao.UserDao;
import com.tgb.entity.User_T;

public class UserManagerImpl implements UserManager {

	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User_T getUser(String id) {
		return userDao.getUser(id);
	}

	@Override
	public List<User_T> getAllUser() {
		return userDao.getAllUser();
	}

	@Override
	public void addUser(User_T user) {
		userDao.addUser(user);
	}

	@Override
	public boolean delUser(String id) {
		
		return userDao.delUser(id);
	}

	@Override
	public boolean updateUser(User_T user) {
		return userDao.updateUser(user);
	}

}
