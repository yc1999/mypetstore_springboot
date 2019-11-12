$(function () {
    $('#checkCodeInput').on('blur', function () {
        $.get('/account/accountCheckCode', "checkCode=" + $(this).val(), function (data) {
            if (data != "1") {
                $('#checkCodeError').text(data);
            }
        });
    });
});