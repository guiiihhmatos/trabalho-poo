<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Empréstimos</title>
        <script src="exibirDadosTable.js"></script>
        <script>
            var campos = ["titulo", "autor", "editora", "ano_publicacao", "disponibilidade", "isbn", "descricao"]; // Especifica as colunas desejadas
            var titulos = ["Tí­tulo", "Autor", "Editora", "Ano", "Disponibilidade", "Isbn", "Descrição"]; // Especifica os tí­tulos personalizados      
            window.onload = function () {
                listarLivros();
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("btnSalvar").disabled = true;
            };

            function listarLivros() {
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "${pageContext.request.contextPath}/api/livros", true);
                xhr.onreadystatechange = function () {
                    var errorContainer = document.getElementById("error-container");
                    errorContainer.style.display = "none";
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        var lista = JSON.parse(xhr.responseText);
                        var table = exibirDados(lista, campos, titulos);
                        //registro do evento de click na tabela
                        table.addEventListener("click", clickGrid);
                        var container = document.getElementById("dados-container");
                        // Limpa o conteúdo existente definindo innerHTML como uma string vazia
                        container.innerHTML = '';
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
            <h2>Empréstimos</h2>
            <div id="dados-container"></div>
        </div>
        <%}%>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
    </body>
</html>