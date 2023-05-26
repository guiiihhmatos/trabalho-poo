<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Usuários - Web Session</title>
</head>
<body>
	<%@include file="WEB-INF/jspf/navbar.jspf"%>
	<%if(userName!=null){ %>
	<div class="m-2">
		<h2>Usuários</h2>
		<%
			System.out.println("listaUsersArray: " + listaUsersArray);
			for(Object user: listaUsersArray){
				System.out.println("user: " + user);
		%>
		<h5><%=user%></h5>
		<%}%>
	</div>
	<%}%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>