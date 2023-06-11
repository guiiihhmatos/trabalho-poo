function exibirDados(lista, colunas, titulos) {
  var table = document.createElement("table");
   //add table id
   table.setAttribute("id", "table");
  table.classList.add("table");
  table.classList.add("table-bordered");
  table.classList.add("table-striped");

  var thead = document.createElement("thead");
  var trHead = document.createElement("tr");


  thead.classList.add("thead-dark");
  thead.classList.add("text-center");
  for (var i = 0; i < colunas.length; i++) {
    var th = document.createElement("th");
    th.innerText = titulos[i];
    th.setAttribute("data-coluna", colunas[i]);
   // th.setAttribute("id", colunas[i]);
    trHead.appendChild(th);
  }
  
  thead.appendChild(trHead);

  var tbody = document.createElement("tbody");

  for (var i = 0; i < lista.length; i++) {
    var item = lista[i];
    var trItem = document.createElement("tr");

    for (var j = 0; j < colunas.length; j++) {
      var coluna = colunas[j];
      var td = document.createElement("td");
      td.innerText = item[coluna];
           
      trItem.appendChild(td);
    }


    trItem.addEventListener("click", function () {
      
    var linhas = Array.from(tbody.getElementsByTagName("tr"));
    linhas.forEach(function (linha) {
        linha.classList.remove('destaque2');
      });            
    });

    // Adicionar classe de destaque ao passar o mouse sobre a linha
    trItem.addEventListener("mouseover", function () {
      this.classList.add("destaque");
    });

    // Remover classe de destaque ao retirar o mouse da linha
    trItem.addEventListener("mouseout", function () {
      this.classList.remove("destaque");
    });
    
    tbody.appendChild(trItem);
  }

  table.appendChild(thead);
  table.appendChild(tbody);

  // Adicionar funcionalidades de filtragem e ordenação
  adicionarFuncionalidades(table);

  var primeiraCelula = trHead.querySelector("th");
  ordenarTabela(table, primeiraCelula);
  // Cria um novo evento de clique
//  var eventoClique = new MouseEvent("click", {
  //  bubbles: true,
    //cancelable: true,
    //view: window
  //});

  // Dispara o evento de clique no elemento do título da coluna para ordenar a tabela
  //primeiraCelula.dispatchEvent(eventoClique);

  return table;
}

function adicionarFuncionalidades(table) {
  var ths = table.getElementsByTagName("th");

  for (var i = 0; i < ths.length; i++) {
    var th = ths[i];
    th.addEventListener("click", function () {
      ordenarTabela(table, this);
    });
  }

  var filtroInput = document.createElement("input");
  filtroInput.classList.add("form-control");
  filtroInput.setAttribute("type", "text");
  filtroInput.setAttribute("placeholder", "Filtrar...");
  filtroInput.addEventListener("input", function () {
    filtrarTabela(table, this.value);
  });

  var filtroRow = table.insertRow(0);
  var filtroCell = filtroRow.insertCell();
  filtroCell.setAttribute("colspan", table.rows[1].cells.length);
  filtroCell.appendChild(filtroInput);
}

function ordenarTabela(table, th) {
  var coluna = th.getAttribute("data-coluna");
  var indiceColuna = Array.from(th.parentNode.children).indexOf(th);
  var ordenacao = th.classList.contains("ascendente") ? "descendente" : "ascendente";

  var tbody = table.getElementsByTagName("tbody")[0];
  var rows = Array.from(tbody.getElementsByTagName("tr"));

  rows.sort(function (a, b) {
    var valorA = a.cells[indiceColuna].innerText.toLowerCase();
    var valorB = b.cells[indiceColuna].innerText.toLowerCase();

    if (coluna === "numero") {
      valorA = parseFloat(valorA);
      valorB = parseFloat(valorB);
    }

    if (valorA < valorB) {
      return ordenacao === "ascendente" ? -1 : 1;
    }
    if (valorA > valorB) {
      return ordenacao === "ascendente" ? 1 : -1;
    }
    return 0;
  });

  for (var i = 0; i < rows.length; i++) {
    tbody.appendChild(rows[i]);
  }

  // Remover classes de ordenação de todas as colunas
  var ths = table.getElementsByTagName("th");
  for (var i = 0; i < ths.length; i++) {
    ths[i].classList.remove("ascendente");
    ths[i].classList.remove("descendente");

    // Remover ícones anteriores
    var icones = ths[i].querySelectorAll(".icone-ordenacao");
    for (var j = 0; j < icones.length; j++) {
      icones[j].remove();
    }
  }

  // Adicionar classe de ordenação à coluna selecionada e novo ícone
  th.classList.add(ordenacao);
  var icone = document.createElement("span");
  icone.classList.add("icone-ordenacao");
  icone.innerHTML = ordenacao === "ascendente" ? "&#x25BC;" : "&#x25B2;";
  th.appendChild(icone);
}

function filtrarTabela(table, filtro) {
  var filtroLowerCase = filtro.toLowerCase();
  var tbody = table.getElementsByTagName("tbody")[0];
  var rows = tbody.getElementsByTagName("tr");

  for (var i = 0; i < rows.length; i++) {
    var row = rows[i];
    var cells = row.getElementsByTagName("td");
    var correspondencia = false;

    for (var j = 0; j < cells.length; j++) {
      var cell = cells[j];
      var valor = cell.innerText.toLowerCase();

      if (valor.includes(filtroLowerCase)) {
        correspondencia = true;
        break;
      }
    }

    row.style.display = correspondencia ? "" : "none";
  }
}
         
// Estilos CSS para a linha destacada
var destaqueStyles = document.createElement("style");
destaqueStyles.innerHTML = `
.destaque {
  background-color: #f2f2f2;
}
.destaque2 {
  background-color: #265a88;
  color: white;  
}
`;
document.head.appendChild(destaqueStyles);