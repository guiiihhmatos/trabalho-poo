<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
  <title>Biblioteca - Livros</title>
  <style>
    .table-container {
      height: 300px; /* Defina a altura desejada */
      overflow-y: auto; /* Habilita a rolagem vertical */
    }
  </style>
  <script src="exibirDadosTable.js"></script>
  <script>
        var campos = ["titulo", "autor", "editora", "ano_publicacao","disponibilidade"]; // Especifica as colunas desejadas
        var titulos = ["Tí­tulo", "Autor", "Editora", "Ano", "Disponibilidade"]; // Especifica os tí­tulos personalizados                    
        window.onload = function() {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "${pageContext.request.contextPath}/api/livros", true);
            xhr.onreadystatechange = function () {
                var errorContainer = document.getElementById("error-container");
                errorContainer.style.display = "none";
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var lista = JSON.parse(xhr.responseText);     
                    var table = exibirDados(lista, campos, titulos);
                    var container = document.getElementById("dados-container");
                    container.appendChild(table);
                    
                } else {                   
                    errorContainer.innerText = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente mais tarde.";
                    errorContainer.style.display = "block";
                }
            };
            xhr.send();
            criarCamposDinamicos();
        }
    </script>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>
 <%if(user!=null){ %>
  <div class="container">
    <div class="row">
      <div class="col-md-8">
        <div class="table-container">
          <!-- Espaço para a tabela -->	
		<div id="dados-container"></div>                                
                <div id="error-container" style="display: none; color: red;"></div>
        </div>
      </div>
      <div class="col-md-4">
        <form>
          <h3>Formulário</h3>
          <!-- campos do formulário dinâmicos -->
          <div id="form-fields"></div>
          <button class="btn btn-primary">Salvar</button>
        </form>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <!-- Espaço para os botões -->
        <button class="btn btn-primary">Novo</button>        
        <button class="btn btn-primary">Deletar</button>
      </div>
    </div>
  </div>

  <%}%>
  <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>