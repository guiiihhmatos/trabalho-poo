<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Index - Web Session</title>
</head>
<body>
	<%@include file="WEB-INF/jspf/navbar.jspf"%>
	<%if(userName!=null){ %>
	<div class="m-2">
		<h2>Página inicial</h2>
	</div>
	<%}%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>