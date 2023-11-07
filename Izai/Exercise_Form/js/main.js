let nameForm;

$(document).ready(() => {
    $('#tbName').on('focusout', event => {
        $('#nameErrorEmpty').addClass('hidden');

        if($('#tbName').val() == ""){
            $('#nameErrorEmpty').removeClass('hidden');
        }
    });
    $('#tbEmail').on('focusout', event => {
        $('#emailErrorFormat').addClass('hidden');
        $('#emailErrorEmpty').addClass('hidden');

        if($('#tbEmail').val() == ""){
            $('#emailErrorEmpty').removeClass('hidden');
        }
        else if(!($('#tbEmail').val().match(new RegExp(".+@.+\\..+")))){
            $('#emailErrorFormat').removeClass('hidden');
        }
    });

    $('#tbComentaries').on('focusout', event => {
        $('#comentaryErrorEmpty').addClass('hidden');
        $('#comentaryErrorLength').addClass('hidden');
        
        if($('#tbComentaries').val() == ""){
            $('#comentaryErrorEmpty').removeClass('hidden');
        }
        else if($('#tbComentaries').val().length > 5){
            $('#comentaryErrorLength').removeClass('hidden');
        }
    });
    $('#tbPassword').on('focusout', event => {
        $('#passwordErrorFormat').addClass('hidden');

        if(!($('#tbPassword').val().match(new RegExp("(?=.{6,})(?=.*[A-Z])(?=.*[0-9])")))){
            $('#passwordErrorFormat').removeClass('hidden');
        }
    });
});