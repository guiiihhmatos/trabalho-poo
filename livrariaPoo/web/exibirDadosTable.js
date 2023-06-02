function exibirDados(lista, colunas, titulos) {
    var table = document.createElement("table");
    table.classList.add("table");

    var thead = document.createElement("thead");
    var trHead = document.createElement("tr");

    for (var i = 0; i < colunas.length; i++) {
        var th = document.createElement("th");
        th.innerText = titulos[i];
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

        tbody.appendChild(trItem);
    }

    table.appendChild(thead);
    table.appendChild(tbody);

    return table;
}
