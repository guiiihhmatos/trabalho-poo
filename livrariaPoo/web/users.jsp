<%@ page import="java.net.URL, java.io.BufferedReader, java.io.InputStreamReader, java.util.stream.Collectors, org.json.JSONArray, org.json.JSONObject" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%

        // Usuário autenticado, continua com a exibição dos usuários

        String contextPath = request.getContextPath();
        String apiEndpoint = "http://localhost:8080" + contextPath + "/api/users";

        // Faz a requisição HTTP para a API e obtém os dados
        URL apiUrlObj = new URL(apiEndpoint);
        BufferedReader reader = new BufferedReader(new InputStreamReader(apiUrlObj.openStream()));
        String responseData = reader.lines().collect(Collectors.joining());
        reader.close();

        // Processa os dados obtidos
        JSONArray jsonArray = new JSONArray(responseData);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Usuários</title>
</head>
<body>
    <h1>Usuários</h1>
    <table>
        <thead>
            <tr>
                <th>Login</th>
                <th>Nome</th>
                <th>Role</th>
            </tr>
        </thead>
        <tbody>
            <% for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String login = item.getString("login");
                String name = item.getString("name");
                String role = item.getString("role");
            %>
                <tr>
                    <td><%= login %></td>
                    <td><%= name %></td>
                    <td><%= role %></td>
                </tr>
            <% } %>
        </tbody>
    </table>
</body>
</html>
