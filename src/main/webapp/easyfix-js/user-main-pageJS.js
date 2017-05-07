/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var RESTaddr;

function init() {
    grabAddr();
    whoAmI();
}

function grabAddr() {
    var addr = document.location.href.toString().split("/");
    var REST = "";
    for (var i = 1; i < addr.length; i++) {
        if (!addr[i].includes("html") && addr[i].toString().length > 0) {
            REST = REST + addr[i] + "/";
        }
    }
    RESTaddr = addr[1].toString()+"//" + REST;
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

function whoAmI(){
    console.log("hei");
    var url= RESTaddr + "webresources/Users/View/Myself/"+localStorage.getItem("sessionId");
    req=initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = whoCallback;
    req.send(null);
}

function whoCallback(){
    if (req.readyState == 4){
        if(req.status == 200){
            crntUser=req.responseXML.childNodes;
            console.log(crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue);
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            document.getElementById("user-name").innerHTML= fname+ " " + lname;
            console.log(crntUser[0].getElementsByTagName("access")[0].childNodes[0].nodeValue);
            myJobs = [];
            for(loop=0;loop<crntUser[0].getElementsByTagName("jobs").length;loop++){
                myJobs.push(String(crntUser[0].getElementsByTagName("jobs")[loop].childNodes[0].nodeValue));
            }
//            if(crntUser[0].getElementsByTagName("access")[0].childNodes[0].nodeValue<2){
//                //hide UI stuff
//                $("#addNewUserButton").hide();
//                $("#newTaskButton").hide();
//                $("#taskManagementButton").hide();
//            }
//            retrieveAll();
        }
    }
}
