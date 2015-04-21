package com.tgb.manager;

import java.util.List;

import com.tgb.dao.StudentDao;
import com.tgb.entity.Student;
import com.tgb.entity.User_T;

public class StudentManagerImpl implements StudentManager {

	private StudentDao studentDao;
	
	

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}



	@Override
	public Student getStudent(String stuNum) {
		return studentDao.getStudent(stuNum);
	}

	@Override
	public List<Student> getAllStudent() {
		return studentDao.getAllStudent();
	}
//
//	@Override
//	public void addUser(User_T user) {
//		userDao.addUser(user);
//	}
//
//	@Override
//	public boolean delUser(String id) {
//		
//		return userDao.delUser(id);
//	}
//
//	@Override
//	public boolean updateUser(User_T user) {
//		return userDao.updateUser(user);
//	}

}
