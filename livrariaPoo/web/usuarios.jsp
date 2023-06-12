<%@ page pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/css_include.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
        <title>Biblioteca - Usuarios</title>
         <script src="grid.js"></script>
       
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf"%>
        <%if(user!=null && user.getRole().equals("ADMIN")){ %>){ %>
        <div class="container">

            <div class="row">
                <div class="col-md-8">
                    <div class="table-container d-flex flex-column align-items-center">
                        <!-- Espaço para a tabela -->	

                        <h3 class="my-5">Lista de Usuários</h3>
                        <div id="dados-container"></div>

                        <div class="d-flex justify-content-around align-items-center w-25">
                            <button class="btn btn-primary" onclick="novoForm()">Novo</button>
                            <button class="btn btn-danger" id="btnDeletar" disabled onclick="deletarSelectedItemGrid()">Deletar</button>
                        </div>

                    </div>
                </div>      
                   <div class="col-md-4">
          
          <h3 class="my-5 text-center">Cadastrar novo usuário</h3>
          
        <form id="myForm" class="mb-5">
          <div id="error-container" style="display: none; color: red;"></div>
          <!-- campos do formulário dinâmicos -->
          <div id="form-fields">

            <div class="mb-3">
              <label for="login">Login</label>
              <input type="text" class="form-control w-100" id="login" disabled>
            </div>

            <div class="mb-3">
              <label for="name">Nome</label>
              <input type="text" class="form-control" id="name" disabled>
            </div>

            <div class="mb-3">
              <label for="role">Função</label>
              <select class="form-group" id="role" disabled>
                <option value="USER">USER</option>
                <option value="ADMIN">ADMIN</option>
              </select>
              
            </div>
              
             <div class="mb-3">
              <label for="passwordHash">Senha</label>
              <input type="password" class="form-control" id="passwordHash" disabled >
            </div>
              
            <div class="mb-3">
                <input type="hidden" name="metodo" id="metodo" value="post">
            </div>
          <button type="button" onclick="submitForm()" class="btn btn-primary" id="btnSalvar">Salvar</button>
        </form>
      </div>
            </div>
        </div>
 <script>
            var campos = ["name", "login", "role"]; // Especifica as colunas desejadas
            var titulos = ["Nome", "Login", "Função"]; // Especifica os tí­tulos personalizados
            var errorContainer ;
            window.onload = function () {
                errorContainer = document.getElementById("error-container");
                errorContainer.style.display = "none";
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
                         if(xhr.status != 200){
                            errorContainer.innerText = "Ocorreu um erro ao carregar os dados. Por favor, tente novamente mais tarde.";
                            errorContainer.style.display = "block";
                         }
                    }
                };
                xhr.send();
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
        document.getElementById("login").disabled = true;
        errorContainer.style.display = "none";        
    }
    function preencherCampos(event){
        //preencher campos do formulário com dados da linha selecionada
        var td = event.target.parentElement.getElementsByTagName("td");
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            document.getElementById(campo).value = td[i].innerText;
        }
    }
    function habilitaCamposEdicao(){
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            document.getElementById(campo).removeAttribute("disabled");
        }        
        document.getElementById("passwordHash").removeAttribute("disabled");            
        document.getElementById("btnSalvar").disabled = false;
        document.getElementById("btnDeletar").disabled = false;
    }

    function deletarSelectedItemGrid(){
        var login = document.getElementById("login").value;
        if(login === ""){
            return;
        }        
        errorContainer.style.display = "none";
        
        fetch( "${pageContext.request.contextPath}/api/users/" + login, {
            method: 'delete',
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {              
            if(response.ok){
                listarUsuarios();                   
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).value = "";
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("passwordHash").disabled=true;
                document.getElementById("btnSalvar").disabled = true;
                document.getElementById("btnDeletar").disabled = true;
            } 
        }).catch(error => {              
            errorContainer.innerText = "Erro: " + error;
            errorContainer.style.display = "block";
        });
    }

    function novoForm() {
        for (var i = 0; i < campos.length; i++) {
            document.getElementById(campos[i]).value = "";
            document.getElementById(campos[i]).removeAttribute("disabled");
        }
        document.getElementById("passwordHash").value = "";
        document.getElementById("passwordHash").removeAttribute("disabled");

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
    function submitForm() {
        var data = {};        
        //validação dos campos do formulário        
        errorContainer.style.display = "none";
        var camposInvalidos = [];
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            var valor = document.getElementById(campo).value;
            if (valor.trim() === "") {
              camposInvalidos.push(titulos[i]);
            }
            //preenche objeto data com os campos do formulário
            data[campo] = valor;
        }
        var passwordHash = document.getElementById("passwordHash").value;
        if(passwordHash.trim() === "") {
            camposInvalidos.push("Senha");
        }
        data["passwordHash"] = passwordHash;        
        document.getElementById("passwordHash").value = "";
        
        if (camposInvalidos.length > 0) {
            errorContainer.innerText = "Os seguintes campos são obrigatórios: " + camposInvalidos.join(", ");
            errorContainer.style.display = "block";
            return;
        }
      
        var jsonData = JSON.stringify(data);
        //recupera method do form
        var metodo = document.getElementById("metodo").value;
        fetch( "${pageContext.request.contextPath}/api/users", {
            method: metodo,
            headers: {
                "Content-Type": "application/json"
            },
            body: jsonData
        }).then(response => {              
            if(response.ok){
                listarUsuarios();
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).value = "";
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("passwordHash").disabled=true;
                document.getElementById("btnSalvar").disabled = true;
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