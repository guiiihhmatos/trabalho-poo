<%@ page pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/css_include.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Usuarios</title>
        <script src="exibirDadosTable.js"></script>
        <script>
            var campos = ["name", "login", "role"]; // Especifica as colunas desejadas
            var titulos = ["name", "login", "role"]; // Especifica os tí­tulos personalizados

            window.onload = function () {
                listarUsuarios();

                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("btnSalvar").disabled = true;
            }

            function listarUsuarios() {
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "${pageContext.request.contextPath}/api/users", true);
                xhr.onreadystatechange = function () {
                    
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

            <div class="row">
                <div class="col-md-8">
                    <div class="table-container d-flex flex-column align-items-center">
                        <!-- Espaço para a tabela -->	

                        <h3 class="my-5">Lista de Usuários</h3>
                        <div id="dados-container"></div>

                        <div class="d-flex justify-content-around align-items-center w-25">
                            <button class="btn btn-primary" onclick="()">Novo</button>
                            <button class="btn btn-danger" id="btnDeletar" disabled onclick="deletarSelectedItemGrid()">Deletar</button>
                        </div>

                    </div>
                </div>      
             
            </div>
        </div>

        <%}%>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
    </body>
</html>