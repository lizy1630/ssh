<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="../js/jquery-1.7.1.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Edit user</h1>
	<form action="/test_ssh/user/updateUser" name="userForm" method="post">
		<input type="hidden" name="id" value="${user.id }">
		Name：<input type="text" name="userName" value="${user.userName }">
		Age：<input type="text" name="age" value="${user.age }">
		<input type="submit" value="edit" >
	</form>
</body>
</html>