package com.tgb.dao;

import java.util.List;

import com.tgb.entity.Student;
import com.tgb.entity.User_T;


public interface StudentDao {

	public Student getStudent(String stuNum);
	
	public List<Student> getAllStudent();
//	
//	public void addUser(User_T user);
//	
//	public boolean delUser(String id);
//	
//	public boolean updateUser(User_T user);
}
