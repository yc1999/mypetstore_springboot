var xmlHttpRequest;

function createXMLHttpRequest() {
    if (window.XMLHttpRequest) {
        xmlHttpRequest = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        xmlHttpRequest = new ActiveXObject("Msxml2.XMLHTTP");
    }
    else {
        xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function sendRequest1(url) {
    createXMLHttpRequest();
    xmlHttpRequest.open("GET", url, true);
    xmlHttpRequest.onreadystatechange = processResponse1;
    xmlHttpRequest.send(null);
}

function processResponse1() {
    if (xmlHttpRequest.readyState == 4) {
        if (xmlHttpRequest.status == 200) {
            var responseInfo = xmlHttpRequest.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
            var div = document.getElementById("usernameMsg1");

            if (responseInfo == "Exist") {
                div.innerHTML = "<font color='red'>用户名已存在！</font>";
            }
            else if (responseInfo == "NotExist") {
                div.innerHTML = "<font color='green'>用户名可用</font>";
            }
            else {
                div.innerHTML = "<font color='green'>请输入用户名</font>";
            }
        }
    }
}

function usernameIsExist1() {
    var username = document.getElementsByName("username")[0].value;
    sendRequest1("/account/usernameIsExist?username=" + username);
}

function sendRequest2(url) {
    createXMLHttpRequest();
    xmlHttpRequest.open("GET", url, true);
    xmlHttpRequest.onreadystatechange = processResponse2;
    xmlHttpRequest.send(null);
}

function processResponse2() {
    if (xmlHttpRequest.readyState == 4) {
        if (xmlHttpRequest.status == 200) {
            var responseInfo = xmlHttpRequest.responseXML.getElementsByTagName("msg")[0].firstChild.nodeValue;
            var div = document.getElementById("usernameMsg2");

            if (responseInfo == "Exist") {
                div.innerHTML = "<font color='red'>用户名已存在！</font>";
            }
            else if (responseInfo == "NotExist") {
                div.innerHTML = "<font color='green'>用户名可用</font>";
            }
            else {
                div.innerHTML = "<font color='green'>请输入用户名</font>";
            }
        }
    }
}

function usernameIsExist2() {
    var username = document.getElementsByName("username")[0].value;
    sendRequest2("/account/usernameIsExist?username=" + username);
}

function emailIsLegal() {
    var email = document.getElementsByName("email")[0].value.replace(/^\s+|\s+$/g, "").toLowerCase();//去除前后空格并转小写
    var reg = /^[a-z0-9](\w|\.|-)*@([a-z0-9]+-?[a-z0-9]+\.){1,3}[a-z]{2,4}$/i;

    if (email.match(reg) == null) {
        document.getElementById("emailMsg").innerHTML = "<b>请输入有效的邮箱</b>";
    } else {
        document.getElementById("emailMsg").innerHTML = null;
    }
}

function phoneIsLegal() {
    var phoneNumber = document.getElementsByName("phone")[0].value;
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
    if (!myreg.test(phoneNumber)) {
        document.getElementById("phoneMsg").innerHTML = "<b>请输入有效的号码</b>";
        return false;
    }
    else {
        document.getElementById("phoneMsg").innerHTML = null;
    }
}

