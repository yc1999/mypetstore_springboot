$(function () {
    var $loginSuccess = $('#cartLoginSuccess');
    var $loginFirst = $('#loginFirst');
    var $confirmRemove = $('#confirmRemove');

    //隐藏提示信息
    $loginSuccess.hide();
    $loginFirst.hide();
    $confirmRemove.hide();

    var m = new Map();
    var $list, $newItemForm, $newItemButton, $input;
    var item = '';                                 // item is an empty string

    $list = $('tbody');                               // Cache the unordered list
    $add = $("#add");
    $newItemForm = $('#newItemForm');              // Cache form to add new items
    $newItemButton = $('#newItemButton');          // Cache button to show form
    $input = $('td.quantity input');
    $input.spinner({
        min: 1,
        hold: true,
        create: function (event, ui) {
            var $this = $(this);
            m[this.id] = $this.val();
        },

        spin: function (event, ui) {
            var $this = $(this);
            m[this.id] = $this.val();
            $this.spinner().spinner("value", $this.val());
            var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * ui.value;
            $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
            var itemId = $this.parent().parent().parent().children(":first-child").children().text();

            $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + ui.value.toString() + '&' + 'status=0', function (data) {
                $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
            });
        },
        change: function (event, ui) {
            var $this = $(this);
            m[this.id] = $this.val();
            $this.spinner().spinner("value", $this.val());
            var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * $this.spinner("value");
            $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
            var itemId = $this.parent().parent().parent().children(":first-child").children().text();
            for (var key in m) {
                $('\#' + key).spinner().spinner("value", m[key]);
            }
            $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + $this.spinner("value").toString() + '&' + 'status=0', function (data) {
                $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
            });
        }
    });

    for (var key in m) {
        $('\#' + key).spinner().spinner("value", m[key]);
    }


    $('tbody tr').hide().each(function (index) {          // Hide table a tr
        $(this).delay(450 * index).fadeIn(1600);     // Then fade them in
    });

    $newItemButton.show();                         // Show the button
    $newItemForm.hide();                           // Hide the form
    $('#showForm').on('click', function () {        // When click on add item button
        $newItemButton.hide();                       // Hide the button
        $newItemForm.show();                         // Show the form
    });

    // 点击表格一行中的一个单元格，若该单元格不是调整物品个数的单元格，则选中一行准备删除
    $list.on('click', 'td', function () {

        var $this = $(this);               // Cache the element in a jQuery object
        if ($this.hasClass('quantity') || $this.attr("id") === "emptyMsg") {

        } else {
            var complete = $this.parent().hasClass('complete');  // Is item complete
            item = $this.parent().text().trim();             // Get the text from the list item
            var msg = item.split(/\s+/g);
            var description = msg[2];
            for (var i = 3; i < msg.length - 3; i++) {
                description += " " + msg[i];
            }
            var quantity = $this.siblings('.quantity').children().children().val();

            if (complete === true) {           // Check if item is complete
                var confirmRemoveDialog = $confirmRemove.dialog({
                    buttons: {
                        OK: function () {
                            confirmRemoveDialog.dialog('close');
                            $this.parent().animate({
                                // If so, animate opacity + padding
                                opacity: 0.0,
                                paddingLeft: '+=180'
                            }, 100, 'swing', function () {    // Use callback when animation completes
                                m.delete($this.parent().children('.quantity').children().children().attr("id"));
                                $this.parent().remove();                // Then completely remove this item
                                $.get("/order/removeItemFromCart", "workingItemId=" + $this.parent().children(":first").children().text(), function (data) {
                                    console.log("这是removeItemFromCart "+data);
                                    $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                });                                     //更新总金额
                            });
                        },
                        Cancel: function () {
                            confirmRemoveDialog.dialog('close');
                            $this.parent().remove();
                            $list.append('<tr title=\"By clicking, you can remove items!\">' + '<td>' + '<a href="viewItem?itemId=' + msg[0] + '">' + msg[0] + '</a></td>'
                                + ' <td>' + msg[1] + '</td>' + ' <td>' + description + '</td>' + ' <td>' + msg[msg.length - 3] + '</td>'
                                + ' <td  class="quantity"><input id="' + msg[0] + '" value="' + quantity + '"/>' + '</td>' + ' <td>' + formatMoney(msg[msg.length - 2].substring(1, msg[msg.length - 2].length), 2, "$") + '</td>' + ' <td>' + formatMoney(msg[msg.length - 2].substring(1, msg[msg.length - 2].length), 2, "$") + '</td>' + '</tr>');
                            $('\#' + msg[0]).spinner({
                                min: 1,
                                hold: true,
                                create: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                },
                                spin: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                    $this.spinner().spinner("value", $this.val());
                                    var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * ui.value;
                                    $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                                    var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                                    for (var key in m) {
                                        $('\#' + key).spinner().spinner("value", m[key]);
                                    }
                                    $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + ui.value.toString() + '&' + 'status=0', function (data) {
                                        $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                    });
                                },
                                change: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                    $this.spinner().spinner("value", $this.val());
                                    var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * $this.spinner("value");
                                    $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                                    var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                                    for (var key in m) {
                                        $('\#' + key).spinner().spinner("value", m[key]);
                                    }
                                    $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + $this.spinner("value").toString() + '&' + 'status=0', function (data) {
                                        $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                    });
                                }
                            });

                            for (var key in m) {
                                $('\#' + key).spinner().spinner("value", m[key]);
                            }
                        }
                    }
                });

            } else {                           // Otherwise indicate it is complete
                $this.parent().remove();                  // Remove the list item
                $list                            // Add back to end of list as complete
                    .append('<tr class=\"complete\" title=\"By clicking, you can remove items!\">' + '<td style="background-color: #999999">' + '<a href="viewItem?itemId=' + msg[0] + '">' + msg[0] + '</a></td>'
                        + ' <td style="background-color: #999999">' + msg[1] + '</td>' + ' <td style="background-color: #999999">' + description + '</td>' + ' <td style="background-color: #999999">' + msg[msg.length - 3] + '</td>'
                        + ' <td  class="quantity" style="background-color: #999999"><input id="' + msg[0] + '" value="' + quantity + '"/>' + '</td>' + ' <td style="background-color: #999999">' + formatMoney(msg[msg.length - 2].substring(1, msg[msg.length - 2].length), 2, "$") + '</td>' + ' <td style="background-color: #999999">' + formatMoney(msg[msg.length - 2].substring(1, msg[msg.length - 2].length), 2, "$") + '</td>' + '</tr>')
                    .hide().fadeIn(300);           // Hide it so it can be faded in <a href="viewItem?itemId='+ msg[0]+ '>
                $('\#' + msg[0]).spinner({
                    min: 1,
                    hold: true,
                    create: function (event, ui) {
                        var $this = $(this);
                        m[this.id] = $this.val();

                    },
                    spin: function (event, ui) {
                        var $this = $(this);
                        m[this.id] = $this.val();
                        $this.spinner().spinner("value", $this.val());
                        var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * ui.value;
                        $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                        var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                        for (var key in m) {
                            $('\#' + key).spinner().spinner("value", m[key]);
                        }
                        $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + ui.value + '&' + 'status=0', function (data) {
                            $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                        });
                    },
                    change: function (event, ui) {
                        var $this = $(this);
                        m[this.id] = $this.val();
                        $this.spinner().spinner("value", $this.val());
                        var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * $this.spinner("value");
                        $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                        var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                        for (var key in m) {
                            $('\#' + key).spinner().spinner("value", m[key]);
                        }
                        $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + $this.spinner("value").toString() + '&' + 'status=0', function (data) {
                            $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                        });
                    }
                });

                for (var key in m) {
                    $('\#' + key).spinner().spinner("value", m[key]);
                }
            }
        }                                    // End of else option
    });                                  // End of event handler


    /*数字格式化*/
    function formatMoney(number, places, symbol, thousand, decimal) {
        number = number || 0;
        //保留的小位数 可以写成 formatMoney(542986,3) 后面的是保留的小位数，否则默 认保留两位
        places = !isNaN(places = Math.abs(places)) ? places : 2;
        //symbol表示前面表示的标志是￥ 可以写成 formatMoney(542986,2,"$")
        symbol = symbol !== undefined ? symbol : "￥";
        //thousand表示每几位用,隔开,是货币标识
        thousand = thousand || ",";
        //decimal表示小数点
        decimal = decimal || ".";
        //negative表示如果钱是负数有就显示“-”如果不是负数 就不显示负号
        //i表示处理过的纯数字
        var negative = number < 0 ? "-" : "",
            i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
            j = (j = i.length) > 3 ? j % 3 : 0;
        return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "￥1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
    }

    /*按钮添加*/
    $add.on('click', null, function () {
        $('p.errorText').text("");
        console.log($('#emptyMsg').children().text());
        $.get('/order/updateCartQuantities', 'itemId=' + $('#itemDescription').val() + '&' + 'status=1', function (data) {
            if (data == 'e') {
                $('p.errorText').text("Sorry, can't find such itemId!")//输出错误信息
            } else {
                var message = data.split('*');
                console.log(message[0] == 'y');
                if (message[0] == 'y') {
                    for (var key in m) {
                        if (key == message[1]) {
                            m[key]++;
                            console.log(m[key]);
                            $('\#' + key).spinner().spinner("value", m[key]);
                            $('\#' + key).spinner({
                                min: 1,
                                hold: true,
                                create: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                    $.get('/order/updateCartQuantities', 'itemId=' + this.id + '&' + 'quantity=' + $this.val() + '&' + 'status=0', function (data) {
                                        $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                    });
                                },
                                spin: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                    $this.spinner().spinner("value", $this.val());
                                    var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * ui.value;
                                    $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                                    var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                                    for (var key in m) {
                                        $('\#' + key).spinner().spinner("value", m[key]);
                                    }
                                    $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + ui.value + '&' + 'status=0', function (data) {
                                        $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                    });
                                },
                                change: function (event, ui) {
                                    var $this = $(this);
                                    m[this.id] = $this.val();
                                    $this.spinner().spinner("value", $this.val());
                                    var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * $this.spinner("value");
                                    $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                                    var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                                    for (var key in m) {
                                        $('\#' + key).spinner().spinner("value", m[key]);
                                    }
                                    $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + $this.spinner("value").toString() + '&' + 'status=0', function (data) {
                                        $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                    });
                                }
                            });
                            break;
                        }


                    }

                } else {
                    $.get('order/addItemToCart', 'workingItemId=' + message[1], function (data) {
                    });
                    $list.append('<tr title=\"By clicking, you can remove items!\">' + '<td>' + '<a href="viewItem?itemId=' + message[1] + '">' + message[1] + '</a></td>'
                        + ' <td>' + message[2] + '</td>' + ' <td>' + message[3] + '</td>' + ' <td>' + message[4] + '</td>'
                        + ' <td  class="quantity"><input id="' + message[1] + '" value="1"/>' + '</td>' + ' <td>' + formatMoney(message[5], 2, "$") + '</td>' + ' <td>' + formatMoney(message[5], 2, "$") + '</td>' + '</tr>');

                    $('\#' + message[1]).spinner({
                        min: 1,
                        hold: true,
                        create: function (event, ui) {
                            var $this = $(this);
                            m[this.id] = $this.val();
                            $.get('/order/updateCartQuantities', 'itemId=' + this.id + '&' + 'quantity=' + $this.val() + '&' + 'status=0', function (data) {
                                console.log(data + "95494");
                                $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                            });
                        },
                        spin: function (event, ui) {
                            var $this = $(this);
                            m[this.id] = $this.val();
                            $this.spinner().spinner("value", $this.val());
                            var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * ui.value;
                            $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                            var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                            for (var key in m) {
                                $('\#' + key).spinner().spinner("value", m[key]);
                            }
                            $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + ui.value + '&' + 'status=0', function (data) {
                                $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                            });
                        },
                        change: function (event, ui) {
                            var $this = $(this);
                            m[this.id] = $this.val();
                            $this.spinner().spinner("value", $this.val());
                            var total = $this.parent().parent().next().text().substring(1, $this.parent().parent().next().text().length) * $this.spinner("value");
                            $this.parent().parent().next().next().text(formatMoney(total, 2, "$"));
                            var itemId = $this.parent().parent().parent().children(":first-child").children().text();
                            for (var key in m) {
                                $('\#' + key).spinner().spinner("value", m[key]);
                            }
                            $.get('/order/updateCartQuantities', 'itemId=' + itemId + '&' + 'quantity=' + $this.spinner("value").toString() + '&' + 'status=0', function (data) {
                                $('tfoot tr td').text('Sub Total: ' + formatMoney(data, 2, "$"));
                                console.log(data + "56548");
                            });
                        }
                    });

                    for (var key in m) {
                        $('\#' + key).spinner().spinner("value", m[key]);
                    }
                }
            }
        });
        $('input:text').val('');
    });

    /*点击了结账按钮*/
    $('#checkout').on('click', function (e) {
        var account = "${sessionScope.account}";
        if (account == null) {
            e.preventDefault();
            $('#loginFirst').dialog();
        }
    });

});