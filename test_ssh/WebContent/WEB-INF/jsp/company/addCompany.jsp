<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/common/commons.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function addCompany(){
		var form = document.forms[0];
		form.action = "/test_ssh/company/addCompany";
		form.method="post";
		form.submit();
	}
</script>
</head>
<body>
	<h1>&nbsp;&nbsp;&nbsp;&nbsp;Add company</h1>
	<div class="view">
		<form class="form-horizontal">
				
			<div class="control-group">
				<label class="control-label"  
					 >* Company Name:</label>
				<div class="controls">
					<input name="name">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Address:</label>
				<div class="controls">
					<input name="address">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*City: </label>
				<div class="controls">
					<input name="city">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Post Code:  </label>
				<div class="controls">
					<input name="post_code">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"  
					 >*Phone: </label>
				<div class="controls">
					<input name="tel">
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
					 >Fax:  </label>
				<div class="controls">
					<input name="fax">
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