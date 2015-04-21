<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function editProgramSkills(){
		var form = document.forms[0];
		
// 		var student = document.getElementById("studentList");
// 		var stuId = student.options[student.selectedIndex].value;
		
// 		var progLang = document.getElementById("programList");
// 		var pl_code = progLang.options[progLang.selectedIndex].value;
		
		var survey = document.getElementById("levelList");
		var level_id = survey.options[survey.selectedIndex].value;
		
		var stuId = document.getElementById("stuNum").value;
		var pl_code = document.getElementById("plCode").value;
// 		alert("stuId"+stuId);
// 		alert("plId"+plCode);
// 		alert("levelId"+levelId);
		
		form.action = "/test_ssh/programSkills/updateProgramSkills?stu_num="+stuId+"&pl_code="+pl_code+"&level_id="+level_id;
		form.method="post";
		form.submit();
	}
</script>
</head>
<body>
	<h1>Edit user</h1>
	<form action="" name="programSkillsForm">
		<input type="hidden" name="stuNum" id="stuNum" value="${user.stuNum }">
		<input type="hidden" name="plCode" id="plCode" value="${user.plCode }">
<%-- 		<input type="hidden" name="levelId" id="levelId" value="${user.levelId }"> --%>
		
<%-- 		<input type="hidden" name="stuName" id="stuName" value="${user.studentName }"> --%>
<%-- 		<input type="hidden" name="plName" id="plName" value="${user.plName }"> --%>
<%-- 		<input type="hidden" name="levelName" id="levelName" value="${user.levelName }"> --%>
		<table>
		<tbody>
			<tr>
				<td>Student Name： </td>
<!-- 				<td><input type="text" name="stu_num"></td> -->
				<td>
				<c:out value="${user.studentName }" ></c:out>
<%-- 				<form:select path="studentList" id="studentList" > --%>
<%-- 				 	<form:option label="${user.studentName }" value="${user.stuNum }"/> --%>
<%--     					<form:options items="${studentList}" /> --%>
<%-- 					</form:select> --%>
				</td>
			</tr>
			<tr>
				<td>Program Language Name： </td>
<!-- 				<td> <input type="text" name="pl_code"></td> -->
					<td>
<%-- 					<form:select path="programList" id="programList"> --%>
<%-- 					<form:option label="${user.plName }" value="${user.plCode }"/> --%>
<%-- 	    					<form:options items="${programList}" /> --%>
<%-- 						</form:select> --%>
				<c:out value="${user.plName }" ></c:out>
					</td>
			</tr>
			<tr>
				<td>Level Name： </td>
<!-- 				<td><input type="text" name="level_id"> </td> -->
					<td><form:select path="levelList" id="levelList">
					<form:option label="${user.levelName }" value="${user.levelId }"/>
	    					<form:options items="${levelList}" />
						</form:select>
					</td>
			</tr>
			<tr><td></td>
			<td>
				<input type="submit" value="edit" onclick="editProgramSkills()" >
			</td>
		</tr>
		</tbody>
		</table>
	</form>
</body>
</html>