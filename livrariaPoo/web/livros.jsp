<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Livros</title>
        <style>
            .table-container {
                min-height: 400px; /* Defina a altura desejada */
                overflow-y: auto; /* Habilita a rolagem vertical */
            }
        </style>
        <script src="grid.js"></script>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf"%>
        <%if(user!=null){ %>
        <div class="container">

            <div class="row d-flex flex-column justify-content-center">
                <div class="col-md-12">
                    <div class="table-container d-flex flex-column align-items-center">
                        <!-- Espaço para a tabela -->	

                        <h3 class="my-5">Lista de livros <%if(user.getRole().equals("USER")){ %> Disponíveis <%}%> </h3>
                        <div id="dados-container" style="width: 1000px;"></div>

                        <div class="d-flex justify-content-around align-items-center w-25">
                            <%if(user.getRole().equals("USER")){ %>
                            <button class="btn btn-primary" id="btnEmprestimo" disabled onclick="alugarLivro()">Alugar</button>
                            <%}%>

                            <%if(user.getRole().equals("ADMIN")){ %>
                            <button class="btn btn-primary" onclick="novoLivroForm()">Novo</button>
                            <button class="btn btn-danger" id="btnDeletar" disabled onclick="deletarSelectedItemGrid()">Deletar</button>
                            <%}%>
                        </div>
                        <div id="error-container" style="display: none; color: red;"></div>            
                    </div>
                </div>
                <%if(user.getRole().equals("ADMIN")){ %>
                <div class="row d-flex flex-column justify-content-center align-items-center" id="novo-livro">
                    <div class="col-md-4">

                        <h3 class="my-5 text-center">Cadastrar novo livro</h3>

                        <form id="myForm" class="mb-5">

                            <div id="form-fields">

                                <div class="mb-3">
                                    <label for="isbn">Isbn</label>
                                    <input type="text" class="form-control w-100" id="isbn" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="titulo">Tí­tulo</label>
                                    <input type="text" class="form-control" id="titulo" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="autor">Autor</label>
                                    <input type="text" class="form-control" id="autor" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="editora">Editora</label>
                                    <input type="text" class="form-control" id="editora" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="ano_publicacao">Ano</label>
                                    <input type="number" class="form-control" id="ano_publicacao" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="disponibilidade">Disponibilidade</label>
                                    <input type="checkbox" class="form-check-input" id="disponibilidade" disabled>
                                </div>

                                <div class="mb-3">
                                    <label for="descricao">Descrição</label>
                                    <input type="text" class="form-control" id="descricao" disabled>
                                </div>
                                <input type="hidden" name="metodo" id="metodo" value="post">
                            </div>
                            <button type="button" onclick="submitForm()" class="btn btn-primary" id="btnSalvar">Salvar</button>
                        </form>
                    </div>
                </div>

                <%}%>
            </div>
        </div>

        <script>

            var campos = ["titulo", "autor", "editora", "ano_publicacao", "disponibilidade", "isbn", "descricao"]; // Especifica as colunas desejadas
            var titulos = ["Tí­tulo", "Autor", "Editora", "Ano", "Disp.", "Isbn", "Descrição"]; // Especifica os tí­tulos personalizados      
            var livroSelParaLocacao = "";
            var errorContainer;
            window.onload = function () {
                errorContainer = document.getElementById("error-container");
                errorContainer.style.display = "none";
                listarLivros();

            <%if(user.getRole().equals("ADMIN")){ %>
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("btnSalvar").disabled = true;
                document.getElementById("novo-livro").classList.add("d-none");
            <%}%>
            };

            function listarLivros() {
            <%if(user.getRole().equals("ADMIN")){ %>
                var url = "${pageContext.request.contextPath}/api/livros"
            <%}else{%>
                var url = "${pageContext.request.contextPath}/api/livros/disponiveis"
            <%}%>

                var xhr = new XMLHttpRequest();
                xhr.open("GET", url, true);
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        var lista = JSON.parse(xhr.responseText);
                        var table = exibirDados(lista, campos, titulos);
            <%if(user.getRole().equals("ADMIN")){ %>
                        //registro do evento de click na tabela para edicao via form
                        table.addEventListener("click", clickGrid);
            <%}else{%>
                        table.addEventListener("click", clickGridLocacao);
            <%}%>
                        var container = document.getElementById("dados-container");
                        // Limpa o conteúdo existente definindo innerHTML como uma string vazia
                        container.innerHTML = '';
                        container.appendChild(table);
                    } else {
                        if (xhr.status != 200) {
                            errorContainer.innerText = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente mais tarde.";
                            errorContainer.style.display = "block";
                        }
                    }
                };
                xhr.send();
            }
            function alugarLivro() {
                var data = {};
                data["idUsuario"] = "<%=user.getId()%>";
                data["isbn"] = livroSelParaLocacao;
                var jsonData = JSON.stringify(data);
                fetch("${pageContext.request.contextPath}/api/emprestimo", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: jsonData
                }).then(response => {
                    if (response.ok) {
                        listarLivros();
                        document.getElementById("btnEmprestimo").disabled = true;
                    }
                }).catch(error => {
                    errorContainer.innerText = "Erro: " + error;
                    errorContainer.style.display = "block";
                });
            }
            function submitForm() {
                var data = {};
                //validação dos campos do formulário        
                errorContainer.style.display = "none";
                var camposInvalidos = [];
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    var valor = document.getElementById(campo).value;
                    if (valor.trim() === "" && campo !== "disponibilidade") {
                        camposInvalidos.push(titulos[i]);
                    }
                    if (campo === "disponibilidade") {
                        valor = document.getElementById(campo).checked ? "true" : "false";
                    }
                    //preenche objeto data com os campos do formulário
                    data[campo] = valor;
                }
                if (camposInvalidos.length > 0) {
                    errorContainer.innerText = "Os seguintes campos são obrigatórios: " + camposInvalidos.join(", ");
                    errorContainer.style.display = "block";
                    return;
                }

                var jsonData = JSON.stringify(data);
                //recupera method do form
                var metodo = document.getElementById("metodo").value;

                fetch("${pageContext.request.contextPath}/api/livros", {
                    method: metodo,
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: jsonData
                }).then(response => {
                    if (response.ok) {
                        listarLivros();
                        //desabilitar campos de edição
                        for (var i = 0; i < campos.length; i++) {
                            var campo = campos[i];
                            document.getElementById(campo).value = "";
                            document.getElementById(campo).disabled = true;
                        }
                        document.getElementById("btnSalvar").disabled = true;
                        document.getElementById("novo-livro").classList.add("d-none");
                    }
                }).catch(error => {
                    errorContainer.innerText = "Erro: " + error;
                    errorContainer.style.display = "block";
                });
            }
            //funcao para capturar evento de click na tabela
            function clickGrid(event) {
                var table = document.getElementById("table");
                var tr = table.getElementsByTagName("tr");
                event.target.parentElement.classList.add("destaque2");
                preencherCampos(event);
                //habilita campos de edição
                habilitaCamposEdicao();
                // Obtém uma referência ao elemento do campo oculto
                document.getElementById('metodo').value = 'put';
                // desabilita campo isbn (chave primária)
                document.getElementById("isbn").disabled = true;
                errorContainer.style.display = "none";
                document.getElementById("novo-livro").classList.remove("d-none");
            }

            //funcao para capturar evento de click na tabela
            function clickGridLocacao(event) {
                var table = document.getElementById("table");
                var tr = table.getElementsByTagName("tr");
                event.target.parentElement.classList.add("destaque2");

                var td = event.target.parentElement.getElementsByTagName("td");
                livroSelParaLocacao = td[5].innerText;
                document.getElementById("btnEmprestimo").disabled = false;
            }
            function preencherCampos(event) {
                //preencher campos do formulário com dados da linha selecionada
                var td = event.target.parentElement.getElementsByTagName("td");
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    if (campo === "disponibilidade") {
                        document.getElementById(campo).checked = td[i].innerText === "true";
                    } else {
                        document.getElementById(campo).value = td[i].innerText;
                    }
                }
            }
            function habilitaCamposEdicao() {
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).removeAttribute("disabled");
                }
                document.getElementById("btnSalvar").disabled = false;
                document.getElementById("btnDeletar").disabled = false;
            <%if(user.getRole().equals("USER")){ %>
                document.getElementById("btnEmprestimo").disabled = false;
            <%}%>
            }

            function deletarSelectedItemGrid() {
                var isbn = document.getElementById("isbn").value;
                if (isbn === "") {
                    return;
                }
                errorContainer.style.display = "none";

                fetch("${pageContext.request.contextPath}/api/livros/" + isbn, {
                    method: 'delete',
                    headers: {
                        "Content-Type": "application/json"
                    }
                }).then(response => {
                    if (response.ok) {
                        listarLivros();
                        //desabilitar campos de edição
                        for (var i = 0; i < campos.length; i++) {
                            var campo = campos[i];
                            document.getElementById(campo).value = "";
                            document.getElementById(campo).disabled = true;
                        }
                        document.getElementById("btnSalvar").disabled = true;
                        document.getElementById("btnDeletar").disabled = true;
                    }
                }).catch(error => {
                    errorContainer.innerText = "Erro: " + error;
                    errorContainer.style.display = "block";
                });
            }

            function novoLivroForm() {
                
                document.getElementById("novo-livro").classList.remove("d-none");
                
                for (var i = 0; i < campos.length; i++) {
                    document.getElementById(campos[i]).value = "";
                    document.getElementById(campos[i]).removeAttribute("disabled");
                }

                document.getElementById("btnSalvar").disabled = false;
                document.getElementById("error-container").innerHTML = "";
                var table = document.getElementById("table");
                var tr = table.getElementsByTagName("tr");
                for (var i = 0; i < tr.length; i++) {
                    tr[i].classList.remove("destaque2");
                }

                document.getElementById("btnDeletar").disabled = true;
                document.getElementById('metodo').value = 'post';
            }
        </script>
        <%}%>

        <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
    </body>
</html>