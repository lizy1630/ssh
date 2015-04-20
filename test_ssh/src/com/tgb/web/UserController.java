package com.tgb.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tgb.entity.intern.User;
import com.tgb.manager.UserManager;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name="userManager")
	private UserManager userManager;

	@RequestMapping("/getAllUser")
	public String  getAllUser(HttpServletRequest request){
		
		request.setAttribute("userList", userManager.getAllUser());
		return "/WEB-INF/jsp/user/userList";
		
		
	}
	
	
	@RequestMapping("/getUserByRole")
	public String getUserByRole(HttpServletRequest request){
		
		request.setAttribute("userList", userManager.getAllUser());
		
		return "/WEB-INF/jsp/user/userList";
	}
	
	@RequestMapping("/getUser")
	public String getUser(String id,HttpServletRequest request){
		
		request.setAttribute("user", userManager.getUser(id));
	
		return "/WEB-INF/jsp/user/editUser";
	}
	
	@RequestMapping("/toAddUser")
	public String toAddUser(){
		return "/WEB-INF/jsp/user/addUser";
	}
	
	@RequestMapping("/addUser")
	public String addUser(User user,HttpServletRequest request){
		
		userManager.addUser(user);
		
		return "redirect:/user/getAllUser";
	}
	
	@RequestMapping("/delUser")
	public void delUser(String id,HttpServletResponse response){
		
		String result = "{\"result\":\"error\"}";
		
		if(userManager.delUser(id)){
			result = "{\"result\":\"success\"}";
		}
		
		response.setContentType("application/json");
		
		try {
			PrintWriter out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/updateUser")
	public String updateUser(User user,HttpServletRequest request){
		
		if(userManager.updateUser(user)){
			user = userManager.getUser(user.getUser_id());
			request.setAttribute("user", user);
			return "redirect:/user/getAllUser";
		}else{
			return "/WEB-INF/error";
		}
	}
}