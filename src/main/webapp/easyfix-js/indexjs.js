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
    RESTaddr = addr[1].toString() + "//" + REST;
    console.log(addr);
    console.log(REST);
    getDeviceTypes();
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

function getDeviceTypes() {
    var url = RESTaddr + "webresources/Devices/Devicetypes";
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = gettypescallback;
    req.send(null);
}

function gettypescallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            fileTypes(req.responseXML);
        }
    }
}

function fileTypes(XML) {
    console.log(XML);
    var menuitems = document.getElementById('dropdowncontents');
    console.log(menuitems);
    $('#dropdowncontents').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var type = XML.childNodes[0].childNodes[loop];
            filetype(type,menuitems);
        }
    }
    console.log(menuitems);
    getFreshReviews();
}

function filetype(type,menu){
            var option = document.createElement('a');
            option.classList.add("dropdown-item");
            var typename = type.getElementsByTagName("name")[0].childNodes[0].nodeValue.toString();
            option.onclick = function () {
                getReviews(typename);
            };
            option.value = type.getElementsByTagName("name")[0].childNodes[0].nodeValue.toString();
            option.innerHTML = "" + type.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            menu.appendChild(option);
    
    
}

function getFreshReviews() {
    //getReviews/ByType/{type}/{amount}
    var url = RESTaddr + "webresources/Devices/getReviews/Latest/4";
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = getlatestreviewcallback;
    req.send(null);
}

function getlatestreviewcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            fileReviews(req.responseXML);
        }
    }
}

function getReviews(typename) {
    console.log(typename);
    var url = RESTaddr + "webresources/Devices/getReviews/ByType/" + typename;
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = getreviewcallback;
    req.send(null);
}

function getreviewcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            fileReviews(req.responseXML);
        }
    }
}

function fileReviews(XML) {
    console.log(XML);
    console.log(XML.childNodes[0].childNodes.length);
    for (loop = 0; loop < 4; loop++) {
        var card = "#reviewname" + loop
        if (XML.childNodes[0].childNodes.length <= loop) {
            console.log("hide");
            document.getElementById("reviewname" + loop);
        } else {
            console.log("run");
            var title = document.getElementById("reviewname" + loop);
            var body = document.getElementById("reviewbody" + loop);
            var review = XML.childNodes[0].childNodes[loop];
            console.log(review);
            console.log(review.getElementsByTagName("title")[0].childNodes[0].nodeValue.toString());
            console.log(review.getElementsByTagName("body")[0].childNodes[0].nodeValue);
            console.log(title);
            console.log(body);
            title.innerHTML = review.getElementsByTagName("title")[0].childNodes[0].nodeValue.toString();
            body.innerHTML = review.getElementsByTagName("body")[0].childNodes[0].nodeValue.toString();
        }
    }
}

