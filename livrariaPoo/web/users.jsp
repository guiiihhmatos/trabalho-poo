<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Usuários - Web Session</title>
</head>
<body>
	<%@include file="WEB-INF/jspf/navbar.jspf"%>
	<%if(user!=null && user.getRole().equals("ADMIN")){ %>
	<div class="m-2">
		<h2>Usuários</h2>
		
		<%                    
                    for(Object u: listaUsersArray){                       
		%>
                        <h5><%=((User) u).getName()%></h5>
                    <%}%>
	</div>
	<%}else{
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/index.jsp");
        }%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>