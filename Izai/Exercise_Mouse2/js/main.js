let mouseSquare;

$(window).on("load", function(){
    mouseSquare = $(".box").children('span');
});

$(document).on('click', event => {
    if(event.pageY <= ($(window).height() / 2)){
        if(event.pageX <= ($(window).width() / 2)){
            mouseSquare.html("Arriba izquierda");
        }
        else{
            mouseSquare.html("Arriba derecha");
        }
    }
    else
    {
        if(event.pageX <= ($(window).width() / 2)){
            mouseSquare.html("Abajo izquierda");
        }
        else{
            mouseSquare.html("Abajo derecha");
        }
    }
})
  