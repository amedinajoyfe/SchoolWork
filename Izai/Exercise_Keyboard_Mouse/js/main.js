let mouseSquare;
let keySquare;

$(document).ready(() => {
    mouseSquare = $("#raton").children('div');
    mouseSquare.html("Mouse X: " + "<br>MouseY: ");
    keySquare = $("#teclado").children('div');
    keySquare.html("Tecla: ");
});

$(document).on( "mousemove", function(event) {
    mouseSquare.html("Mouse X: " + event.pageX + "<br>MouseY: " + event.pageY);
    $("#teclado").removeClass("blue");
    $("#raton").removeClass("yellow");
});

$(document).on("keydown", event => {
    keySquare.html("Tecla: " + String.fromCharCode(event.which));
    $("#teclado").addClass("blue");
})

$(document).on("click", () => {
    $("#raton").addClass("yellow");
})