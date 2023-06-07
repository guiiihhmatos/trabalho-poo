<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="WEB-INF/jspf/html-head-libs.jspf"%>
  <title>Biblioteca - Livros</title>
  <style>
    .table-container {
      height: 400px; /* Defina a altura desejada */
      overflow-y: auto; /* Habilita a rolagem vertical */
    }
  </style>
  <script src="grid.js"></script>
  <script>
    var campos = ["titulo", "autor", "editora", "ano_publicacao","disponibilidade", "isbn", "descricao" ]; // Especifica as colunas desejadas
    var titulos = ["Tí­tulo", "Autor", "Editora", "Ano", "Disponibilidade", "Isbn", "Descrição", "Ação"]; // Especifica os tí­tulos personalizados                    
    var tipos = ["text", "text", "text", "number", "checkbox", "text", "text", "button"]; // Especifica os tí­tulos personalizados                    
    window.onload = function() {
        criarCamposForm();
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
    function submitForm() {
        var data = {};
        
        //validação dos campos do formulário
        var errorContainer = document.getElementById("error-container");
        errorContainer.style.display = "none";
        var camposInvalidos = [];
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            var valor = document.getElementById(campo).value;
            if (valor === "" && campo !== "disponibilidade") {
              camposInvalidos.push(titulos[i]);
            }
            if(campo === "disponibilidade"){
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
        var errorContainer = document.getElementById("error-container");
        //recupera method do form
        var metodo = document.getElementById("metodo").value;
        fetch( "${pageContext.request.contextPath}/api/livros", {
            method: metodo,
            headers: {
                "Content-Type": "application/json"
            },
            body: jsonData
        }).then(response => {              
            if(response.ok){
                listarLivros();                   
                //desabilitar campos de edição
                for (var i = 0; i < campos.length; i++) {
                    var campo = campos[i];
                    document.getElementById(campo).value = "";
                    document.getElementById(campo).disabled = true;
                }
                document.getElementById("btnSalvar").disabled = true;
            } 
            return response.json();                              
          }).then(data => {
                    errorContainer.innerText = "Erro: " + data.message;
                    errorContainer.style.display = "block";                   
            console.log('Resposta:', data);             
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
        //preencher campos do formulário com dados da linha selecionada
        var td = event.target.parentElement.getElementsByTagName("td");
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            if (campo === "disponibilidade") {
                document.getElementById(campo).checked = td[i].innerText === "true";
            } else{
              document.getElementById(campo).value = td[i].innerText;
            }
        }
        //habilita campos de edição
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            document.getElementById(campo).removeAttribute("disabled");
        }
        document.getElementById("btnSalvar").disabled = false;
        document.getElementById("btnDeletar").disabled = false;
        // Obtém uma referência ao elemento do campo oculto
        document.getElementById('metodo').value = 'put';
        // desabilita campo isbn (chave primária)
        document.getElementById("isbn").disabled = true;
        var errorContainer = document.getElementById("error-container");
        errorContainer.style.display = "none";        
    }

    function deletarSelectedItemGrid(){
        var isbn = document.getElementById("isbn").value;
        if(isbn === ""){
            return;
        }
        var errorContainer = document.getElementById("error-container");
        errorContainer.style.display = "none";
        fetch( "${pageContext.request.contextPath}/api/livros/" + isbn, {
            method: 'delete',
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {              
            if(response.ok){
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
            return response.json();                              
          }).then(data => {
                    errorContainer.innerText = "Erro: " + data.message;
                    errorContainer.style.display = "block";                   
            console.log('Resposta:', data);             
        }).catch(error => {              
            errorContainer.innerText = "Erro: " + error;
            errorContainer.style.display = "block";
        });
    }

    function novoLivroForm() {
        for (var i = 0; i < campos.length; i++) {
            var campo = campos[i];
            document.getElementById(campo).value = "";
            document.getElementById(campo).removeAttribute("disabled");
        }
        document.getElementById("btnSalvar").disabled = false;
        var errorContainer = document.getElementById("error-container");
        errorContainer.innerHTML = "";
        //desmarcar item selecionado na tabela
          //desmarcar item selecionado na tabela
        var table = document.getElementById("table");
        var tr = table.getElementsByTagName("tr");
        for (var i = 0; i < tr.length; i++) {
            tr[i].classList.remove("destaque2");
        }
        document.getElementById("btnDeletar").disabled = true;
        //alterar method do form para POST        
        document.getElementById('metodo').value = 'post';
    }

      // Função para criar os campos form
    function criarCamposForm() {
      var formFields = document.getElementById('form-fields');
      formFields.innerHTML = '';

      for (var i = 0; i < campos.length; i++) {
        var campoDiv = document.createElement('div');
        campoDiv.className = 'mb-3';

        var label = document.createElement('label');
        label.innerText = titulos[i];
        campoDiv.appendChild(label);

        var input = document.createElement('input');
        input.type = tipos[i]; 
                        
        if(tipos[i] === 'checkbox'){
          input.className = 'form-check-input ';
          // Adiciona o espaço entre o checkbox e o label usando margens
          input.style.marginLeft = '10px';
        }else{
          input.className = 'form-control';
        }
        input.id = campos[i];
        campoDiv.appendChild(input);

        formFields.appendChild(campoDiv);
      }
    } 

    </script>
</head>
<body>
<%@include file="WEB-INF/jspf/navbar.jspf"%>
 <%if( user != null && user.getRole().equals("ADMIN") ){%>
  <div class="container">
    <div class="row">
      <div class="col-md-12">
        <!-- Espaço para os botões -->
        <button class="btn btn-primary" onclick="novoLivroForm()">Novo</button>
        <button class="btn btn-primary" id="btnDeletar" disabled onclick="deletarSelectedItemGrid()">Deletar</button>
      </div>
    </div>    
    <div class="row">
      <div class="col-md-12">
        <div class="table-container">
          <!-- Espaço para a tabela -->	
            <div id="dados-container"></div>                                
        </div>
      </div>    
    </div>
      
      <div class="row">
          <div class="col-md-4">
            <form id="myForm" >
              <div id="error-container" style="display: none; color: red;"></div>
              <div id="form-fields"></div>
               <input type="hidden" name="metodo" id="metodo" value="post">
              <button type="button" onclick="submitForm()" class="btn btn-primary" id="btnSalvar">Salvar</button>
            </form>
          </div>
      </div>
  </div>

  <%}%>
  <%@include file="WEB-INF/jspf/html-body-libs.jspf"%>
</body>
</html>