<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.io.IOException" %>

<!DOCTYPE html>
<html>
<head>
<%@ include file="WEB-INF/jspf/html-head-libs.jspf" %>
<title>Livraria Web</title>
</head>
<body>
	<%@ include file="WEB-INF/jspf/navbar.jspf" %>
	
	<%
		if (user != null) {
                
			try {
				response.sendRedirect("livros.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	%>

	<%@ include file="WEB-INF/jspf/html-body-libs.jspf" %>
</body>
</html>
