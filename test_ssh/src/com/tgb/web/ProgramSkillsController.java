package com.tgb.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tgb.entity.Student;
import com.tgb.entity.survey.ProgramLanguages;
import com.tgb.entity.survey.ProgramSkills;
import com.tgb.entity.survey.ProgramSkillsPK;
import com.tgb.entity.survey.SurveyLevels;
import com.tgb.extras.ProgSkillsVO;
import com.tgb.manager.ProgramLanguagesManager;
import com.tgb.manager.ProgramSkillsManager;
import com.tgb.manager.StudentManager;
import com.tgb.manager.SurveyLevelsManager;
import com.tgb.manager.UserManager;

@Controller
@RequestMapping("/programSkills")
public class ProgramSkillsController {

	@Resource(name="programSkillsManager")
	private ProgramSkillsManager programSkillsManager;
	
	@Resource(name="studentManager")
	private StudentManager studentManager;
	
	@Resource(name="programLanguagesManager")
	private ProgramLanguagesManager programLanguagesManager;
	
	@Resource(name="surveyLevelsManager")
	private SurveyLevelsManager surveyLevelsManager;
	
//	@Resource(name="userManager")
//	private UserManager userManager;

	@RequestMapping("/getAllProgramSkills")
	public String getAllUser(HttpServletRequest request){
		
		List<ProgramSkills> programSkills = programSkillsManager.getAllProgramSkills();
		ArrayList<ProgSkillsVO> progSkillsList = new ArrayList<ProgSkillsVO>();
		
		for(ProgramSkills program :programSkills){
			
			ProgSkillsVO oneProg = new ProgSkillsVO();
			
			oneProg.setStuNum(program.getProgSkillsPk().getStudent().getStu_num());
			oneProg.setPlCode(program.getProgSkillsPk().getProgramLanguages().getPlCode());
			oneProg.setLevelId(program.getSurveyLevels().getLevelID());
			
			oneProg.setStudentName(program.getProgSkillsPk().getStudent().getFname());
			oneProg.setPlName(program.getProgSkillsPk().getProgramLanguages().getPlName());
			oneProg.setLevelName(program.getSurveyLevels().getLevelName());
			
			progSkillsList.add(oneProg);
		}
		request.setAttribute("userList", progSkillsList);
		
		return "/getAllProgramSkills";
	}
	
	@RequestMapping("/getProgramSkills")
	public String getUser(String stuNum,String plCode,String levelId,HttpServletRequest request){
		
		Student student = studentManager.getStudent(stuNum);
		ProgramLanguages progLanguages = programLanguagesManager.getProgramLanguages(plCode);
		
		ProgramSkillsPK progSkillsPk= new ProgramSkillsPK();
		
		progSkillsPk.setStudent(student);
		progSkillsPk.setProgramLanguages(progLanguages);
		
		ProgramSkills programSkills = programSkillsManager.getProgramSkills(progSkillsPk);
		
		ProgSkillsVO oneProgram = new ProgSkillsVO();
		
		oneProgram.setStuNum(programSkills.getProgSkillsPk().getStudent().getStu_num());
		oneProgram.setPlCode(programSkills.getProgSkillsPk().getProgramLanguages().getPlCode());
		oneProgram.setLevelId(programSkills.getSurveyLevels().getLevelID());
		
		oneProgram.setStudentName(programSkills.getProgSkillsPk().getStudent().getFname());
		oneProgram.setPlName(programSkills.getProgSkillsPk().getProgramLanguages().getPlName());
		oneProgram.setLevelName(programSkills.getSurveyLevels().getLevelName());
		
		
		List<Student> allStudents = studentManager.getAllStudent();
		List<ProgramLanguages> programLanguages = programLanguagesManager.getAllProgramLanguages();
		List<SurveyLevels> surveyLevels = surveyLevelsManager.getAllSurveyLevels();
		
		Map<String,String> students = new LinkedHashMap<String,String>();
		Map<String,String> plNames = new LinkedHashMap<String,String>();
		Map<String,String> levelNames = new LinkedHashMap<String,String>();
		
		
		for(Student s: allStudents){ 
			students.put(s.getStu_num(), s.getFname());
		}
		
		for(ProgramLanguages p: programLanguages){ 
			plNames.put(p.getPlCode(),p.getPlName());
		}
		
		for(SurveyLevels s:surveyLevels){ 
			levelNames.put(s.getLevelID(),s.getLevelName());
		}
		
		request.setAttribute("studentList", students);
		request.setAttribute("programList", plNames);
		request.setAttribute("levelList", levelNames);
		
		request.setAttribute("user", oneProgram);
	
		return "/editProgramSkills";
	}
	
	@RequestMapping("/toAddProgramSkills")
	public String toAddProgramSkills(HttpServletRequest request){
		
		List<Student> allStudents = studentManager.getAllStudent();
		List<ProgramLanguages> programLanguages = programLanguagesManager.getAllProgramLanguages();
		List<SurveyLevels> surveyLevels = surveyLevelsManager.getAllSurveyLevels();
		
		Map<String,String> students = new LinkedHashMap<String,String>();
		Map<String,String> plNames = new LinkedHashMap<String,String>();
		Map<String,String> levelNames = new LinkedHashMap<String,String>();
		
		
		for(Student s: allStudents){ 
			students.put(s.getStu_num(), s.getFname());
		}
		
		for(ProgramLanguages p: programLanguages){ 
			plNames.put(p.getPlCode(),p.getPlName());
		}
		
		for(SurveyLevels s:surveyLevels){ 
			levelNames.put(s.getLevelID(),s.getLevelName());
		}
		
		request.setAttribute("studentList", students);
		request.setAttribute("programList", plNames);
		request.setAttribute("levelList", levelNames);
		
		return "/addProgramSkills";
	}
	
	//test update functinality
	@RequestMapping("/addProgramSkills")
	public String addProgramSKills(String stu_num, String plCode,String levelId, HttpServletRequest request){
		
		try{
		ProgramSkills progSkills = new ProgramSkills();
		ProgramSkillsPK programSkillsPK = new ProgramSkillsPK();
		
		
		
		Student student = studentManager.getStudent(stu_num);
		ProgramLanguages progLang = programLanguagesManager.getProgramLanguages(plCode);
		SurveyLevels surveyLevels = surveyLevelsManager.getSurveyLevels(levelId);
		
		programSkillsPK.setStudent(student);
		programSkillsPK.setProgramLanguages(progLang);
		progSkills.setProgSkillsPk(programSkillsPK);
		progSkills.setSurveyLevels(surveyLevels);
		
		programSkillsManager.addProgramSkills(progSkills);
		}
		catch(Exception e){ 
			return "/error";
		}
		
		return "redirect:/programSkills/getAllProgramSkills";
	}
	
//	@RequestMapping("/delUser")
//	public void delUser(String id,HttpServletResponse response){
//		
//		String result = "{\"result\":\"error\"}";
//		
//		if(userManager.delUser(id)){
//			result = "{\"result\":\"success\"}";
//		}
//		
//		response.setContentType("application/json");
//		
//		try {
//			PrintWriter out = response.getWriter();
//			out.write(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	@RequestMapping("/updateProgramSkills")
	public String updateUser(String stu_num, String pl_code,String level_id,HttpServletRequest request){
		
		Student student = studentManager.getStudent(stu_num);
		ProgramLanguages progLanguages = programLanguagesManager.getProgramLanguages(pl_code);
		SurveyLevels surveyLevels = surveyLevelsManager.getSurveyLevels(level_id);
		
		if(programSkillsManager.updateProgramSkills(student, progLanguages, surveyLevels)){
			return "redirect:/programSkills/getAllProgramSkills";
		}else{
			return "/error";
		}
	}
	
	@RequestMapping("/gotoIndex")
	public String goToIndex(HttpServletRequest request){
		
//		request.setAttribute("userList", userManager.getAllUser());
		return "/WEB-INF/userList";
	}
}