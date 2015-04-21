<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addSurveyLevel(){
		var form = document.forms[0];

		form.action = "/test_ssh/surveyLevels/addSurveyLevel";
		form.method="post";
		form.submit();
	}
</script>
</head>
<body>
	<h1>Add Survey Level</h1>
	
	<form action="" name="surveyLevelForm">
	<table>
		<tbody>
			<tr>
				<td>Table Name： </td>
				<td>
				<input type="text" name="tableName">
				</td>
			</tr>
			<tr>
				<td>Level Name： </td>
<!-- 				<td><input type="text" name="level_id"> </td> -->
					<td>
					<input type="text" name="levelName">
					</td>
			</tr>
			<tr>
				<td><input type="button" value="Add" onclick="addSurveyLevel()"> </td>
			</tr>
		</tbody>
	</table>
		
		
		
		
	</form>
</body>
</html>