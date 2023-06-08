<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Biblioteca - Usu�rios</title>
</head>
<body>
	<%@include file="WEB-INF/jspf/navbar.jspf"%>
	<%if(user!=null && user.getRole().equals("ADMIN")){ %>

        <%  if(request.getParameter("session-item-end")!= null){
                // Obt�m o login do usu�rio a ser removido
                String sessionToRemove = request.getParameter("sessionId");
                listaUsersArray = (List<User>) application.getAttribute("listaUsers");
                User userToRemove = null;
                for (User u : listaUsersArray) {
                    if (u.getSessionId().equals(sessionToRemove)) {
                        userToRemove = u;
                        break;
                    }
                }
                // Remove o usu�rio da lista, se encontrado
                if (userToRemove != null) {
                    listaUsersArray.remove(userToRemove);
                }
                
                HttpSession sessionToInvalidate = sessionMap.get(sessionToRemove);
                if (sessionToInvalidate != null ) {
                    // Invalida a sess�o do usu�rio
                    sessionToInvalidate.invalidate();
                    sessionMap.remove(sessionToRemove);
                }
            }       
        %>
            <div class="container text-center">
                <h1 class="my-5">Usu�rios Conectados</h1>
                <table class="table table-bordered table-striped" style="max-height: 300px; overflow-y: auto;">
                        <thead class="thead-dark text-center">
                                <tr>
                                        <th>Usu�rio</th>
                                        <th>A��o</th>
                                </tr>
                        </thead>
                        <tbody class="text-center">
                            <%-- Itera sobre a lista de usu�rios --%>
                            <c:forEach var="user" items="${applicationScope.listaUsers}">
                                <tr>
                                    <td>${user.name}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/sessions.jsp">
                                                <input type="hidden" name="sessionId" value="${user.sessionId}">
                                                <button type="submit" class="btn btn-danger" name="session-item-end">Encerrar Sess�o</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                </table>
            </div>	
        <%}%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>