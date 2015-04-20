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
	<h1>&nbsp;&nbsp;&nbsp;&nbsp;Edit company</h1>
	<div class="view">
		<form class="form-horizontal" action="/test_ssh/company/updateCompany" name="userForm" method="post">
			<input type="hidden" name="com_code" value="${company.com_code }">
			<div class="control-group">
				<label class="control-label"  
					 >* Company Name:</label>
				<div class="controls">
					<input name="name" value="${company.name }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Address:</label>
				<div class="controls">
					<input name="address" value="${company.address }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*City: </label>
				<div class="controls">
					<input name="city" value="${company.city }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Post Code:  </label>
				<div class="controls">
					<input name="post_code" value="${company.post_code }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Phone: </label>
				<div class="controls">
					<input name="tel" value="${company.tel }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Email:   </label>
				<div class="controls">
					<input name="email" value="${company.email }">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >Fax:  </label>
				<div class="controls">
					<input name="fax" value="${company.fax }">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn"  onclick="addCompany()"  >Submit

					</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>