<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>&nbsp;&nbsp;&nbsp;&nbsp;Edit user</h1>
	<div class="view">
		<form class="form-horizontal" action="/test_ssh/user/updateUser" name="userForm" method="post">
		<input type="hidden" name="user_id" value="${user.user_id }">
			<div class="control-group">
				<label class="control-label"  >*First name：</label>
				<div class="controls">
					<input name="fname" type="text" value="${user.fname }">
				</div>
				
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >Last name：</label>
				<div class="controls">
					<input name="lname" value="${user.lname }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Position:</label>
				<div class="controls">
					<input name="user_position" value="${user.user_position }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*School: </label>
				<div class="controls">
					<input name="school" value="${user.school }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >Telephone:  </label>
				<div class="controls">
					<input name="tel" value="${user.tel }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Mobile: </label>
				<div class="controls">
					<input name="mobile" value="${user.mobile }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Email:   </label>
				<div class="controls">
					<input name="email" value="${user.email }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >notes:  </label>
				<div class="controls">
					<input name="notes" value="${user.notes }" >
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn"   >Submit

					</button>
				</div>
			</div>
		</form>
	</div>
	
		
<!-- 	<form action="/test_ssh/user/updateUser" name="userForm" method="post"> -->
	
<%-- 		*first name：<input type="text" name="fname" value="${user.fname }"> --%>
<%-- 		last name：<input type="text" name="lname" value="${user.lname }"> --%>
<%-- 		*position: <input type="text" name="user_position" value="${user.user_position }"> --%>
<%-- 		*school:  <input type="text" name="school" value="${user.school }"> --%>
<%-- 		telephone:  <input type="text" name="tel" value="${user.tel }"> --%>
<%-- 		extension:  <input type="text" name="extension" value="${user.extension }"> --%>
<%-- 		*mobile:  <input type="text" name="mobile" value="${user.mobile }"> --%>
<%-- 		*email:  <input type="text" name="email" value="${user.email }"> --%>
<%-- 		notes:  <input type="text" name="notes" value="${user.notes }"> --%>
<!-- 		<input type="submit" value="edit" > -->
<!-- 	</form> -->
</body>
</html>