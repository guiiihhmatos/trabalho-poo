<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Empréstimos</title>
        <script src="grid.js"></script>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf"%>
        <%if(user!=null){ %>
            <div class="container">
                <h3 class="my-5">Lista de Empréstimos</h2>
                <div id="dados-container"></div>
                <div id="error-container" style="display: none; color: red;"></div>
                <button class="btn btn-primary" id="btnDevolver" disabled onclick="devolverLivro()">Devolver</button>
            </div>
        <script>
            var campos = ["rowId", "dataFormatada", "titulo", "isbn", "login", "name", "dataDevFormatada"]; // Especifica as colunas desejadas
            var titulos = ["Id Emp.","Data Emprestimo", "Titulo do livro", "ISBN", "login", "name", "Data Devolução"]; // Especifica os tí­tulos personalizados      
            var livroSelParaLocacao = "";
            var rowId = "";
            var errorContainer ;
            window.onload = function () {
                errorContainer = document.getElementById("error-container");
                errorContainer.style.display = "none";
                listarEmprestimos(); 
                
            };

            function listarEmprestimos() {
                 <%if(user.getRole().equals("ADMIN")){ %>  
                var url = "${pageContext.request.contextPath}/api/emprestimo"
                <%}else{%>
                var url = "${pageContext.request.contextPath}/api/emprestimo/<%=user.getId()%>";
                <%}%>
                var xhr = new XMLHttpRequest();
                xhr.open("GET", url, true);
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        var lista = JSON.parse(xhr.responseText);
                        var table = exibirDados(lista, campos, titulos);
                        //registro do evento de click na tabela
                        table.addEventListener("click", clickGridDevolucao);
                        var container = document.getElementById("dados-container");
                        // Limpa o conteúdo existente definindo innerHTML como uma string vazia
                        container.innerHTML = '';
                        container.appendChild(table);
                    } else {
                        if(xhr.status != 200){           
                            errorContainer.innerText = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente mais tarde.";
                            errorContainer.style.display = "block";
                        }
                    }
                };
                xhr.send();
            }
            
        //funcao para capturar evento de click na tabela
    function clickGridDevolucao(event) {
        var table = document.getElementById("table");
        var tr = table.getElementsByTagName("tr");
        event.target.parentElement.classList.add("destaque2");
        
        var td = event.target.parentElement.getElementsByTagName("td");        
        livroSelParaLocacao = td[3].innerText;
        rowId = td[0].innerText;
        document.getElementById("btnDevolver").disabled = false;
    }            
            
        function devolverLivro(){      
        var data = {};     
        data["idUsuario"] =  "<%=user.getId()%>";        
        data["isbn"] = livroSelParaLocacao; 
        data["rowId"] = rowId; 
        var jsonData = JSON.stringify(data);
        fetch( "${pageContext.request.contextPath}/api/emprestimo", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: jsonData
        }).then(response => {              
            if(response.ok){
                listarEmprestimos();
                document.getElementById("btnDevolver").disabled = true;
            } 
        }).catch(error => {              
            errorContainer.innerText = "Erro: " + error;
            errorContainer.style.display = "block";
        });
    }
        </script>            
            
        <%}%>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
    </body>
</html>