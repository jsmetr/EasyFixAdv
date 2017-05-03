/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var unameField;
var pswField;
var RESTaddr;

function init() {
    unameField = document.getElementById("form2");
    pswField = document.getElementById("form3");
    grabAddr();
}

function grabAddr() {
    console.log("herp");
    var addr = document.location.href.toString().split("/");
    var REST = "";
    for (var i = 1; i < addr.length; i++) {
        if (!addr[i].includes("html") && addr[i].toString().length > 0) {
            REST = REST + addr[i] + "/";
        }
    }
    RESTaddr = addr[1].toString()+"//" + REST;
    console.log(addr);
    console.log(REST);
    testrest();
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') !== -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function login() {
    var url = RESTaddr + "webresources/Users/Login/" + unameField.value + "/" + pswField.value;
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = logincallback;
    req.send(null);
}

function redirect(sessionId) {
    var url = RESTaddr + "webresources/Users/MyRole/" + sessionId;
    console.log("redirecting, " + url);
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = redircallback;
    req.send(null);
}

function logincallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
            if (req.responseText !== "FAILURE") {
                localStorage.setItem("sessionId", req.responseText);
                redirect(req.responseText);
            } else {
                alert("Your username and password are invalid.")
            }
        }
    }
}

function redircallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
            window.location.replace(req.responseText + ".html");
        }
    }
}

function populate() {
    var url = RESTaddr + "webresources/Testing/populate";
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = popcallback;
    req.send(null);
}

function popcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            if (req.responseText !== "FAILURE") {
                console.log(req.responseText);
            }
        }
    }
}

function testrest() {
    var url = RESTaddr + "webresources/Testing/TickTock";
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = resttestcallback;
    req.send(null);
}

function resttestcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            if (req.responseText !== "FAILURE") {
                console.log(req.responseText);
            }
            populate();
        }
    }
}
