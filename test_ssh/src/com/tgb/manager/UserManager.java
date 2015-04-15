package com.tgb.manager;

import java.util.List;

import com.tgb.entity.User_T;

public interface UserManager {

	public User_T getUser(String id);
	
	public List<User_T> getAllUser();
	
	public void addUser(User_T user);
	
	public boolean delUser(String id);
	
	public boolean updateUser(User_T user);
}
