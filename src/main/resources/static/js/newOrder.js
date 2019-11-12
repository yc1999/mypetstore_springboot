$(function () {

    //隐藏确定的提示信息
    var $confirmForm1 = $("#confirmForm1");
    $confirmForm1.hide();
    var $confirmForm2 = $("#confirmForm2");
    $confirmForm2.hide();
    var $confirmForm3 = $("#confirmForm3");
    $confirmForm3.hide();

    //隐藏回退的链接
    $return = $('#BackLink');
    $return.hide();

    //一开始禁用后两个选项卡，保证生成订单按流程来走
    $tabs = $("#tabs");
    $tabs.tabs({
        disabled: [1, 2]
    });

    var dialog1 = $confirmForm1.dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            "Confirm": function () {
                $.post('/order/newOrder', $('#form1').serialize(), function (data) {
                    console.log(data);
                    alert("成功")
                    var datas = jQuery.parseJSON(data);
                    $('#ordername').text(datas.order[0].orderId);
                    $('#tabs3cardType').text(datas.order[0].cardType);
                    $('#tabs3creditCard').text(datas.order[0].creditCard);
                    $('#tabs3expiryDate').text(datas.order[0].expiryDate);
                    $('#tabs3billToFirstName').text(datas.order[0].billToFirstName);
                    $('#tabs3billToLastName').text(datas.order[0].billToLastName);
                    $('#tabs3billAddress1').text(datas.order[0].billAddress1);
                    $('#tabs3billAddress2').text(datas.order[0].billAddress2);
                    $('#tabs3billCity').text(datas.order[0].billCity);
                    $('#tabs3billState').text(datas.order[0].billState);
                    $('#tabs3billZip').text(datas.order[0].billZip);
                    $('#tabs3billCountry').text(datas.order[0].billCountry);
                });
                dialog1.dialog("close");
                $tabs.tabs("disable", 0);
                $tabs.tabs("enable", 2);
                $tabs.tabs("option", "active", 2);
                $return.show();
            },
            Cancel: function () {
                dialog1.dialog("close");
            }
        }
    });

    var dialog2 = $confirmForm2.dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            "Confirm": function () {
                $.post('/order/shippingForm', $('#form2').serialize(), function (data) {
                    var datas = jQuery.parseJSON(data);
                    $('#tab3shipToFirstName').text(datas.order[0].shipToFirstName);
                    $('#tab3shipToLastName').text(datas.order[0].shipToLastName);
                    $('#tab3shipAddress1').text(datas.order[0].shipAddress1);
                    $('#tab3shipAddress2').text(datas.order[0].shipAddress2);
                    $('#tab3shipCity').text(datas.order[0].shipCity);
                    $('#tab3shipState').text(datas.order[0].shipState);
                    $('#tab3shipZip').text(datas.order[0].shipZip);
                    $('#tab3shipCountry').text(datas.order[0].shipCountry);
                });
                dialog2.dialog("close");
                $tabs.tabs("disable", 1);
                $tabs.tabs("enable", 2);
                $tabs.tabs("option", "active", 2);
                $return.show();
            },
            Cancel: function () {
                dialog2.dialog("close");
            }
        }
    });

    var dialog3 = $confirmForm3.dialog({
        autoOpen: false,
        height: 250,
        width: 350,
        modal: true,
        buttons: {
            "Confirm": function () {
                $.post('/order/newOrder', $('#form1').serialize(), function (data) {
                    var datas = jQuery.parseJSON(data);
                    $('#ordername').text(datas.order[0].orderId);
                    $('#tabs3cardType').text(datas.order[0].cardType);
                    $('#tabs3creditCard').text(datas.order[0].creditCard);
                    $('#tabs3expiryDate').text(datas.order[0].expiryDate);
                    $('#tabs3billToFirstName').text(datas.order[0].billToFirstName);
                    $('#tabs3billToLastName').text(datas.order[0].billToLastName);
                    $('#tabs3billAddress1').text(datas.order[0].billAddress1);
                    $('#tabs3billAddress2').text(datas.order[0].billAddress2);
                    $('#tabs3billCity').text(datas.order[0].billCity);
                    $('#tabs3billState').text(datas.order[0].billState);
                    $('#tabs3billZip').text(datas.order[0].billZip);
                    $('#tabs3billCountry').text(datas.order[0].billCountry);
                });
                dialog3.dialog("close");
                $tabs.tabs("disable", 0);
                $tabs.tabs("enable", 1);
                $tabs.tabs("option", "active", 1);
            },
            Cancel: function () {
                dialog3.dialog("close");
            }
        }
    });

    $('#tab1').on('click', function (e) {
        e.preventDefault();
        if ($("#checkbox1").is(":checked")) {
            dialog3.dialog("open");
        } else {
            dialog1.dialog("open");
        }
    });

    $('#tab2').on('click', function (e) {
        e.preventDefault();
        dialog2.dialog("open");
    });
});