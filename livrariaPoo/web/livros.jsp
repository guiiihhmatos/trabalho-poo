<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
<title>Biblioteca - Livros</title>
    <script src="exibirDadosTable.js"></script>
  <script>
        window.onload = function() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "${pageContext.request.contextPath}/api/livros", true);
            xhr.onreadystatechange = function () {
                var errorContainer = document.getElementById("error-container");
                errorContainer.style.display = "none";
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var lista = JSON.parse(xhr.responseText);     
                    var colunas = ["titulo", "autor", "editora", "ano_publicacao","disponibilidade"]; // Especifica as colunas desejadas
                    var titulos = ["Título", "Autor", "Editora", "Ano", "Disponibilidade"]; // Especifica os títulos personalizados                    
                    var table = exibirDados(lista, colunas, titulos);
                    var container = document.getElementById("dados-container");
                    container.appendChild(table);
                    
                } else {                   
                    errorContainer.innerText = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente mais tarde.";
                    errorContainer.style.display = "block";
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
		<h2>Livros</h2>
		<div id="dados-container"></div>
                <div id="error-container" style="display: none; color: red;"></div>
	</div>
        <%}%>
	<%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>