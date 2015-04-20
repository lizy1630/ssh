package com.tgb.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tgb.entity.intern.Company;
import com.tgb.manager.CompanyManager;

@Controller
@RequestMapping("/company")
public class CompanyController {

	@Resource(name="companyManager")
	private CompanyManager companyManager;

	@RequestMapping("/getAllCompany")
	public String  getAllCompany(HttpServletRequest request){
		
		request.setAttribute("companyList", companyManager.getAllCompany());
		return "/WEB-INF/jsp/company/companyList";
		
		
	}
	
	
	@RequestMapping("/getCompany")
	public String getCompany(String id,HttpServletRequest request){
		
		request.setAttribute("company", companyManager.getCompany(id));
	
		return "/WEB-INF/jsp/company/editCompany";
	}
	
	@RequestMapping("/toAddCompany")
	public String toAddCompany(){
		return "/WEB-INF/jsp/company/addCompany";
	}
	
	@RequestMapping("/addCompany")
	public String addCompany(Company company,HttpServletRequest request){
		
		companyManager.addCompany(company);
		
		return "redirect:/company/getAllCompany";
	}
	
	@RequestMapping("/delCompany")
	public void delCompany(String id,HttpServletResponse response){
		
		String result = "{\"result\":\"error\"}";
		
		if(companyManager.delCompany(id)){
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
	
	@RequestMapping("/updateCompany")
	public String updateCompany(Company company,HttpServletRequest request){
		
		if(companyManager.updateCompany(company)){
			company = companyManager.getCompany(company.getCom_code());
			request.setAttribute("company", company);
			return "redirect:/company/getAllCompany";
		}else{
			return "/WEB-INF/error";
		}
	}
}