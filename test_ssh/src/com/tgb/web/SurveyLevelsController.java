package com.tgb.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tgb.entity.TableID;
import com.tgb.entity.survey.SurveyLevels;
import com.tgb.extras.GeneratingID;
import com.tgb.manager.SurveyLevelsManager;
import com.tgb.manager.TableIdManager;

@Controller
@RequestMapping("/surveyLevels")
public class SurveyLevelsController {

	@Resource(name="TableIdManager")
	private TableIdManager tableIdManager;
	
	
	@Resource(name="surveyLevelsManager")
	private SurveyLevelsManager surveyLevelsManager;
	

	@RequestMapping("/getAllSurveyLevels")
	public String getAllSurveyLevels(HttpServletRequest request){
		
		List<SurveyLevels> allSurveyLevels = new ArrayList<SurveyLevels>();
		
		allSurveyLevels = surveyLevelsManager.getAllSurveyLevels();
		
		request.setAttribute("surveyList", allSurveyLevels);
		
		
		return "/getAllSurveyLevels";
	}
	
	@RequestMapping("/getSurveyLevel")
	public String getSurveyLevel(String levelId,HttpServletRequest request){
		
		SurveyLevels surveyLevel = surveyLevelsManager.getSurveyLevels(levelId);
		
		request.setAttribute("surveyLevel", surveyLevel);
		
		return "/editSurveyLevel";
	}
	
	@RequestMapping("/toAddSurveyLevel")
	public String toAddSurveyLevel(HttpServletRequest request){
		
		
		return "/addSurveyLevel";
	}
	
	
	@RequestMapping("/addSurveyLevel")
	public String addSurveyLevel(String tableName,String levelName, HttpServletRequest request){
		
		TableID surveyTable = tableIdManager.getTable("survey_levels");
		String Id = surveyTable.getMaxId();
		
		GeneratingID genId = new GeneratingID();
		
		String surveyId = genId.generatingId(Id);
		
		surveyTable.setMaxId(surveyId);
		
		
		SurveyLevels newSurveyLevel = new SurveyLevels();
		
		newSurveyLevel.setLevelID(surveyId);
		newSurveyLevel.setTableName(tableName);
		newSurveyLevel.setLevelName(levelName);
		
		surveyLevelsManager.addSurveyLevels(newSurveyLevel);
		tableIdManager.updateTableId(surveyTable);
		
		
		return "redirect:/surveyLevels/getAllSurveyLevels";
	}
	

	@RequestMapping("/updateSurveyLevel")
	public String updateSurveyLevel(SurveyLevels surveyLevels,HttpServletRequest request){
		
		if(surveyLevelsManager.updateSurveyLevels(surveyLevels)){
			return "redirect:/surveyLevels/getAllSurveyLevels";
		}else{
			return "/error";
		}
	}
	
	@RequestMapping("/gotoIndex")
	public String goToIndex(HttpServletRequest request){
		
		return "/WEB-INF/userList";
	}
}