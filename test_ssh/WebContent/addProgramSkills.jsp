<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addProgramSkills(){
		var form = document.forms[0];
		
		var student = document.getElementById("studentList");
		var stuId = student.options[student.selectedIndex].value;
		
		var progLang = document.getElementById("programList");
		var plCode = progLang.options[progLang.selectedIndex].value;
		
		var survey = document.getElementById("levelList");
		var levelId = survey.options[survey.selectedIndex].value;
		
// 		alert("stuId"+stuId);
// 		alert("plId"+plCode);
// 		alert("levelId"+levelId);
		
		form.action = "/test_ssh/programSkills/addProgramSkills?stu_num="+stuId+"&plCode="+plCode+"&levelId="+levelId;
		form.method="post";
		form.submit();
	}
</script>
</head>
<body>
	<h1>Add Program Skills</h1>
	
	<form action="" name="programSkillsForm">
	<table>
		<tbody>
			<tr>
				<td>Student Name： </td>
<!-- 				<td><input type="text" name="stu_num"></td> -->
				<td><form:select path="studentList" id="studentList" >
    					<form:options items="${studentList}" />
					</form:select>
				</td>
			</tr>
			<tr>
				<td>Program Language Name： </td>
<!-- 				<td> <input type="text" name="pl_code"></td> -->
					<td><form:select path="programList" id="programList">
	    					<form:options items="${programList}" />
						</form:select>
					</td>
			</tr>
			<tr>
				<td>Level Name： </td>
<!-- 				<td><input type="text" name="level_id"> </td> -->
					<td><form:select path="levelList" id="levelList">
	    					<form:options items="${levelList}" />
						</form:select>
					</td>
			</tr>
			<tr>
				<td><input type="button" value="Add" onclick="addProgramSkills()"> </td>
			</tr>
		</tbody>
	</table>
		
		
		
		
	</form>
</body>
</html>