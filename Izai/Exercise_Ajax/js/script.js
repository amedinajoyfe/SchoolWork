document.getElementById("loadData").addEventListener("click", loadData);
function loadData() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "archivo.json", true);
    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            var data = JSON.parse(xhr.responseText);
            displayData(data);
        } else {
            console.error("Error al cargar los datos: " + xhr.status);
        }
    };
    xhr.onerror = function () {
        console.error("Error de red al cargar los datos.");
    };
    xhr.send();
}
function displayData(data) {
    var result = document.getElementById("result");
    for(element in data)
    {
        result.innerHTML += element;
    }
    // data.forEach(element => {
    //     result.innerHTML += "Nombre: " + element.nombre + ", Edad: " + element.edad + "<br>";
    // });
}
