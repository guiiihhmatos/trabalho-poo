<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Empréstimos</title>
        <script src="exibirDadosTable.js"></script>
        <script>
            var campos = ["nome_usuario", "titulo_livro", "editora", "ano_emprestimo"]; // Especifica as colunas desejadas
            var titulos = ["Nome do Usuario", "Titulo do livro", "Ano do Emprestimo"]; // Especifica os tí­tulos personalizados      
            window.onload = function () {
                listarEmprestimos();
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("btnSalvar").disabled = true;
            };

            function listarEmprestimos() {
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "${pageContext.request.contextPath}/api/emprestimo", true);
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
            <div class="container">
                <h3 class="my-5">Lista de Empréstimos</h2>
                <div id="dados-container"></div>
            </div>
        <%}%>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
    </body>
</html>