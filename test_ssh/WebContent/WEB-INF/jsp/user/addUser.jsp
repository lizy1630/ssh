<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addUser(){
		var form = document.forms[0];
		form.action = "/test_ssh/user/addUser";
		form.method="post";
		form.submit();
	}
</script>
</head>
<body>
	<h1>&nbsp;&nbsp;&nbsp;&nbsp;Add user</h1>
	<div class="view">
		<form class="form-horizontal">
			<div class="control-group">
				<label class="control-label"  >*First name：</label>
				<div class="controls">
					<input name="fname" type="text">
				</div>
				
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >Last name：</label>
				<div class="controls">
					<input name="lname">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Position:</label>
				<div class="controls">
					<input name="user_position">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*School: </label>
				<div class="controls">
					<input name="school">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >Telephone:  </label>
				<div class="controls">
					<input name="tel">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Mobile: </label>
				<div class="controls">
					<input name="mobile">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Email:   </label>
				<div class="controls">
					<input name="email">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >notes:  </label>
				<div class="controls">
					<input name="notes">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn"  onclick="addUser()"  >Submit

					</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>