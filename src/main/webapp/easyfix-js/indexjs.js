/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var unameField;
var pswField;

function init() {
    unameField = document.getElementById("form2");
    pswField = document.getElementById("form3");
    console.log(unameField.value + " " + pswField.value);
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
    var url = "http://localhost:8080/EasyFix/webresources/Users/Login/" + unameField.value + "/" + pswField.value;
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = logincallback;
    req.send(null);
}

function redirect(sessionId) {
    var url = "http://localhost:8080/EasyFix/webresources/Users/MyRole/"+sessionId;
    //var url = "http://localhost:8080/EasyFix/webresources/Users/MyRole/"+localStorage.getItem("sessionId");
    //var url="http://localhost:8080/EasyFix/webresources/Testing/Response"; /Check/{sessionId}
    //var url="http://localhost:8080/EasyFix/webresources/Users/Check/"+sessionId;
    console.log("redirecting, "+url);
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
            window.location.replace(req.responseText+".html");
            /*
            if (req.responseText.includes("manager")) {
                window.location.replace("manager.html");
            } else if (req.responseText == "technician") {
                window.location.replace("taskboard.html");
            } else if (req.responseText == "clerk") {
                window.location.replace("taskboard.html");
            } else {
                window.location.replace("taskboard.html");
            }
            */
        }
    }
}