<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@ include file="/common/commons.jsp"%>
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
	
	function add(){
		var href="/test_ssh/user/toAddUser";
		window.location.href = href;
	}
	function edit(id){
		var href = "/test_ssh/user/getUser?id="+id;
		window.location.href = href;
	}
</script>
</head>
<body>
<div style="height: 50px; top: 100px"></div>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span4">
			<ul class="nav nav-list">
				<li class="nav-header">
					Intern manage
				</li>
				<li>
					<a href="#">Index</a>
				</li>
				<li>
					<a href="/test_ssh/company/getAllCompany">Company Manage</a>
				</li>
				<li>
					<a href="/test_ssh/user/getAllUser">Faculty Manage</a>
				</li>
				<li>
					<a href="#">Student Manage</a>
				</li>
				
				<li class="nav-header">
					survey manage
				</li>
				<li>
					<a href="#">Student Survey</a>
				</li>
				<li>
					<a href="#">Student Match</a>
				</li>
				<li class="divider">
				</li>
				<li>
					<a href="#">Help</a>
				</li>
			</ul>
		</div>
		<div class="span8" id = "tables">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>
							No.
						</th>
						<th>
							First Name
						</th>
						<th>
							Last Name
						</th>
						<th>
							Telephone
						</th>
						<th>
							Email
						</th>
						<th>
							Operation
						</th>
					</tr>
				</thead>
				<tbody>
					<% int i=0; String color = "";%>
					<c:if test="${!empty userList }">
						<c:forEach items="${userList }" var="user">
							<%
								if(i%2==0)  ;else  color="info";
								i++;
							%>
							<tr class="<%=color %>">
								<td><%=i%></td>
								<td>${user.fname }</td>
								<td>${user.lname }</td>
								<td>${user.tel }</td>
								<td>${user.email }</td>
								<td>
									 <button class="btn btn-primary btn-small" type="button" onclick="edit('${user.user_id }')" >edit</button>
									 <button class="btn btn-primary btn-small" type="button" onclick="del('${user.user_id }')" >del</button>
								</td>
							</tr>				
						</c:forEach>
					</c:if>
				</tbody>
			</table>
			<div><button class="btn btn-info" type="button" onclick="add()" >Add User</button></div>
			
		</div>
	</div>
</div>
</body>
</html>