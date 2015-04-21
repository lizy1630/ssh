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
</head>
<body>
	<h1>Edit Survey Level</h1>surveyLevel
	<form action="/test_ssh/surveyLevels/updateSurveyLevel" name="surveyLevelForm" method="post">
		<input type="hidden" name="levelID" value="${surveyLevel.levelID }">

		<table>
		<tbody>
			<tr>
				<td>Table Name： </td>
				<td>
				<input type="text" name="tableName" value="${surveyLevel.tableName }">
				</td>
			</tr>
			
			<tr>
				<td>Level Name： </td>
					<td>
					<input type="text" name="levelName" value="${surveyLevel.levelName }">
					</td>
			</tr>
			<tr><td></td>
			<td>
				<input type="submit" value="edit" >
			</td>
		</tr>
		</tbody>
		</table>
	</form>
</body>
</html>