let x = $(document);
let ps = [null, null, null], btn1 = null, btnFont = null, btnColor = null, btnJoyfe = null, btnGoogle = null, btnYoutube = null, btnHeaders = null, btnElements = null, btnHideStrong = null;
let money = 10;
let colours = [-1, -1, -1]
x.on('DOMContentLoaded', inicializarEventos);
function inicializarEventos() {
    ps = $('p');
    btn1 = $("#btn1");
    btnFont = $("#btnFont");
    btnColor = $("#btnColor");
    // btnJoyfe = $("#btnJoyfe");
    // btnGoogle = $("#btnGoogle");
    // btnYoutube = $("#btnYoutube");
    btnHeaders = $("#btnHeaders");
    btnElements = $("#btnElements");
    btnHideStrong = $("#btnHideStrong");

    btn1.on('click', tirada);
    btnFont.on('click', changeFont);
    btnColor.on('click', changeColor);
    // btnJoyfe.on("click", null, 1, changelink); //Esta parte no va todavía, averiguar como pasar parametros en el on click
    // btnGoogle.on('click', changelink(1));
    // btnYoutube.on('click', changelink(2));
    btnHeaders.on('click', changeHeaders);
    btnElements.on('click', changeElements);
    btnHideStrong.on('click', hideStrong);
}
function tirada() {
    money -= 1;

    if(money < 0){
        btn1.css("visibility", "hidden");
        return;
    }
    let colores = ["red", "green", "gray"];
    for (let i = 0; i < 3; i++) {
        colours[i] = Math.floor(Math.random() * 3);
        ps.eq(i).css('background-color', colores[colours[i]]);
    }

    if(colours[0] == colours[1] && colours[0] == colours[2]){
        // alert("Acierto");
        money += 10;
    }
    $("#money").html("Dinero: " + money);
}

function changeFont(){
    if($(".noticia").css("color").toString() == "rgb(255, 0, 0)")
    {
        $(".noticia").css("color", "black");
        return;
    }
    $(".noticia").css("color", "red");
}

function changeColor(){
    if($(".noticia").css("font-family").toString() == '"Courier New"')
    {
        $(".noticia").css("font-family", "Franklin Gothic Medium");
        return;
    }
    $(".noticia").css("font-family", "Courier New");
}

// function changelink(option){
//     alert(option);
    // if(option == 0){
    //     $('a').eq(0).prop("href", "https://joyfe.iepgroup.es/").html("Colegio JOYFE")
    // }
    // else if(option == 1){
    //     $('a').eq(0).prop("href", "https://www.google.com/").html("Google")
    // }
    // else{
    //     $('a').eq(0).prop("href", "https://www.youtube.com/").html("Youtube")
    // }
// }

function changeHeaders(){
    $('th').toggleClass("headers");
}

function changeElements(){
    $('td').toggleClass("element");
}

function hideStrong(){
    let variables = $('strong');
    for (let index = 0; index < 3; index++) {
        variables.eq(index).replaceWith(variables.eq(index).html());
    }
}