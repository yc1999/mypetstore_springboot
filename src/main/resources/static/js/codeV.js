$(function () {
    function codeV() {
        var password = document.getElementById("password");
        var repeatedPassword = document.getElementById("repeatedPassword");
        var str1 = password.value;
        var str2 = repeatedPassword.value;
        if (str1 != str2) {
            repeatedPassword.className = 'fail';
        }
        else {
            repeatedPassword.className = 'pass';
        }
    }

    $('#repeatedPassword').on('input', function () {
        codeV();
    });


});
