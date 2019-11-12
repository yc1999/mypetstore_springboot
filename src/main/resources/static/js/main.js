$(function () {

    var $loginSuccess = $('#mainLoginSuccess');
    var $accountInformation = $('#accountInformation');
    var $nav = $('nav');
    var $adminLogin = $('#adminLogin');

    //隐藏登录成功的提示信息
    $loginSuccess.hide();
    $accountInformation.hide();

    //隐藏管理员登录窗口
    $adminLogin.hide();

    /*双模式窗口*/
    var dialog, form,
        username = $("#username"),
        password = $("#password"),
        allFields = $([]).add(username).add(password),


        dialog = $("#dialog-form").dialog({
            autoOpen: false,
            height: 400,
            width: 350,
            modal: true,
            buttons: {
                "Log in": function () {
                    $.post('/account/signon', $('#login').serialize(), function (data) {
                        console.log(data);
                        if (data.indexOf('zjxadminasdsdsdsdscczcs') >= 0) {
                            dialog.dialog("close");
                            $adminLogin.dialog();
                        }
                        else {
                            var msg = 'Username or Password error!';

                            if (data.indexOf('Username or Password error!') >= 0 || data.indexOf("checkCode can't be null!") >= 0
                                || data.indexOf('checkCode error!') >= 0) {
                                $('.validateTips').css('color', 'red');
                                $('.validateTips').text(data);
                            } else {
                                var infoDialog = $loginSuccess.dialog({
                                    buttons: {
                                        Close: function () {
                                            infoDialog.dialog("close");
                                        }
                                    }
                                });
                                dialog.dialog("close");
                                var msg = "Welcome " + data + "!";
                                $('#welcome h2').text(msg);
                                $nav.html("<a href=\"signoff\">Sign Out</a>\n" + "<a href=\"#\" id=\"myaccount\">My Account</a>");
                                // dialogstatus.dialog("open");
                            }
                        }
                    });
                },
                Cancel: function () {
                    dialog.dialog("close");
                }
            },
            close: function () {
                form[0].reset();
                allFields.removeClass("ui-state-error");
            }
        });

    form = dialog.find("form").on("submit", function (event) {
        event.preventDefault();
    });

    $("#signIn").on("click", function (e) {
        e.preventDefault();
        dialog.dialog("open");
    });

    /*滚动图片*/
    var $slider = $('.slider');
    $slider.each(function () {              // For every slider
        var $this = $(this);                    // Current slider
        var $group = $this.find('.slide-group'); // Get the slide-group (container)
        var $slides = $this.find('.slide');       // Create jQuery object to hold all slides
        var buttonArray = [];                    // Create array to hold navigation buttons
        var currentIndex = 0;                     // Hold index number of the current slide
        var timeout;                              // Sets gap between auto-sliding

        function move(newIndex) {          // Creates the slide from old to new one
            var animateLeft, slideLeft;      // Declare variables

            advance();                       // When slide moves, call advance() again

            // If it is the current slide / animating do nothing
            if ($group.is(':animated') || currentIndex === newIndex) {
                return;
            }

            buttonArray[currentIndex].removeClass('active'); // Remove class from item
            buttonArray[newIndex].addClass('active');        // Add class to new item

            if (newIndex > currentIndex) {   // If new item > current
                slideLeft = '100%';            // Sit the new slide to the right
                animateLeft = '-100%';         // Animate the current group to the left
            } else {                         // Otherwise
                slideLeft = '-100%';           // Sit the new slide to the left
                animateLeft = '100%';          // Animate the current group to the right
            }
            // Position new slide to left (if less) or right (if more) of current
            $slides.eq(newIndex).css({left: slideLeft, display: 'block'});

            $group.animate({left: animateLeft}, function () {    // Animate slides and
                $slides.eq(currentIndex).css({display: 'none'}); // Hide previous slide
                $slides.eq(newIndex).css({left: 0}); // Set position of the new item
                $group.css({left: 0});               // Set position of group of slides
                currentIndex = newIndex;               // Set currentIndex to the new image
            });
        }

        function advance() {                     // Used to set
            clearTimeout(timeout);                 // Clear previous timeout
            timeout = setTimeout(function () {      // Set new timer
                if (currentIndex < ($slides.length - 1)) { // If slide < total slides
                    move(currentIndex + 1);            // Move to next slide
                } else {                             // Otherwise
                    move(0);                           // Move to the first slide
                }
            }, 4000);                              // Milliseconds timer will wait
        }

        $.each($slides, function (index) {
            // Create a button element for the button
            var $button = $('<button type="button" class="slide-btn">&bull;</button>');
            if (index === currentIndex) {    // If index is the current item
                $button.addClass('active');    // Add the active class
            }
            $button.on('click', function () { // Create event handler for the button
                move(index);                   // It calls the move() function
            }).appendTo('.slide-buttons');   // Add to the buttons holder
            buttonArray.push($button);       // Add it to the button array
        });

        advance();                          // Script is set up, advance() to move it

        //     //控制悬浮窗的显现
        //     $('.slide-group img').on('mouseover',function (e) {
        //         advance();
        //         clearTimeout(timeout);
        //         $.get('suspend','categoryId=' + $(this).attr("id").substring(1, $(this).attr("id").length), function (data) {
        //             var message = data.split("*");
        //             for(var i = 1; i <= parseInt(message[0]); i ++){
        //                 $('#productId').append(message[i] + " ");
        //             }
        //             for(var i = parseInt(message[0])+1; i < message.length; i ++){
        //                 $('#itemId').append(message[i] + " ");
        //             }
        //         });
        //         //获取鼠标位置函数
        //         var mousePos = mousePosition(e);
        //         var  xOffset = -15;
        //         var  yOffset = -25;
        //         $("#inform").css("display","block").css("position","absolute").css("top",(mousePos.y - yOffset) + "px").css("left",(mousePos.x + xOffset) + "px").fadeIn(100);
        //     }).delay(2000);
        //     $('.slide-group img').on('mouseout',function () {
        //         $('#productId').text("");
        //         $('#itemId').text("");
        //         $("#inform").fadeOut(100).css("display","none");
        //         timeout = setTimeout(function () {      // Set new timer
        //             if (currentIndex < ($slides.length - 1)) { // If slide < total slides
        //                 move(currentIndex + 1);            // Move to next slide
        //             } else {                             // Otherwise
        //                 move(0);                           // Move to the first slide
        //             }
        //         }, 4000);                              // Milliseconds timer will wait
        //     });
        //
    });

    /*下拉框 + 菜单*/
    $("#leftNav").accordion({
        collapsible: true,
        active: false,
        heightStyle: "content",

        activate: function (event, ui) {
            $('#menu1,#menu2,#menu3,#menu4,#menu5').css('visibility', 'visible');
        }
    });
    var m = new Map();
    m.set("fish", "\#menu1");
    m.set("dog", "\#menu2");
    m.set("cat", "\#menu3");
    m.set("reptiles", "\#menu4");
    m.set("bird", "\#menu5");

    $('#menu1,#menu2,#menu3,#menu4,#menu5').menu();

    /*菜单*/
    $('#leftNav h3').on('click', function () {
        $('#leftNav h3.clicked').removeClass('clicked');
        $(this).addClass('clicked');
    });

    /*下拉 通过事件委托给动态生成的元素绑定事件*/
    $nav.on('mouseover', '#myaccount,#myaccount a', function () {
        console.log("A");
        $accountInformation.show();
    });
    $nav.on('mouseout', '#myaccount,#myaccount a', function () {
        $accountInformation.hide();
    });
    $accountInformation.on('mouseover', function () {
        $accountInformation.show();
    });
    $accountInformation.on('mouseout', function () {
        $accountInformation.hide();
    });

    // //获取鼠标坐标
    // function mousePosition(ev){
    //     ev = ev || window.event;
    //     if(ev.pageX || ev.pageY){
    //         return {x:ev.pageX, y:ev.pageY};
    //     }
    //     return {
    //         x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
    //         y:ev.clientY + document.body.scrollTop - document.body.clientTop
    //     };
    // }

});