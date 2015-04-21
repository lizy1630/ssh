<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function del(id){
		$.get("/test_ssh/user/delUser?id=" + id,function(data){
			if("success" == data.result){
				alert("delete success!");
				window.location.reload();
			}else{
				alert("delete failed!");
			}
		});
	}
</script>
</head>
<body>
	<h6><a href="/test_ssh/programSkills/gotoIndex">Go to Index</a></h6>
	
<table border="1">
		<tbody>
			<tr>
				<th>Table Name</th>
				<th>Level Name</th>
				<th>Edit</th>
			</tr>
			<c:if test="${!empty surveyList }">
				<c:forEach items="${surveyList }" var="user">
					<tr>
						<td>${user.tableName}</td>
						<td>${user.levelName}</td>
						<td>
							<a href="/test_ssh/surveyLevels/getSurveyLevel?levelId=${user.levelID}">edit</a>
						</td>
					</tr>				
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	
</body>
</html>