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
				<th>Student Name</th>
				<th>Program Language Name</th>
				<th>Level Name</th>
				<th>Edit</th>
			</tr>
			<c:if test="${!empty userList }">
				<c:forEach items="${userList }" var="user">
					<tr>
<%-- 						<td>${user.stuNum}</td> --%>
						<td>${user.studentName}</td>
<%-- 						<td>${user.plCode}</td> --%>
						<td>${user.plName}</td>
<%-- 						<td>${user.levelId}</td> --%>
						<td>${user.levelName}</td>
						<td>
							<a href="/test_ssh/programSkills/getProgramSkills?stuNum=${user.stuNum }&plCode=${user.plCode}&levelId=${user.levelId}&stuName=${user.studentName}&plName=${user.plName}&levelName=${user.levelName}">edit</a>
<%-- 							<a href="javascript:del('${user.stuNum }')">delete</a> --%>
						</td>
					</tr>				
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	
</body>
</html>