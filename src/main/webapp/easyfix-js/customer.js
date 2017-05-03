/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var RESTaddr;

function init() {
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

function logout() {
    var url = RESTaddr + "webresources/Users/Logout/"+localStorage.getItem("sessionId");
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = logoutcallback;
    req.send(null);
}

function logoutcallback(){
    if (req.readyState == 4){
        if(req.status == 200){
            localStorage.setItem("sessionId","");
            window.location.replace("index.html");
        }
    }
}
