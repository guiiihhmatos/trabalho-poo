<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Biblioteca - Empréstimos</title>
    <script src="exibirDadosTable.js"></script>
  <script>
        window.onload = function() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "http://localhost:8080/livrariaPoo/api/emprestimos", true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var lista = JSON.parse(xhr.responseText);
                    var table = exibirDados(lista);
                    var container = document.getElementById("dados-container");
                    container.appendChild(table);
                }
            };
            xhr.send();
        }
    </script>
</head>
<body>
	<%@include file="WEB-INF/jspf/navbar.jspf"%>
	<%if(user!=null){ %>
	<div class="m-2">
		<h2>Empréstimos</h2>
		<div id="dados-container"></div>
	</div>
        <%}%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>