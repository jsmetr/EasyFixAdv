/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var req2;
var RESTaddr;

function init() {
    grabAddr();
    whoAmI();
    getCustomers();
}

function grabAddr() {
    var addr = document.location.href.toString().split("/");
    var REST = "";
    for (var i = 1; i < addr.length; i++) {
        if (!addr[i].includes("html") && addr[i].toString().length > 0 && (typeof addr[i] !== 'undefined')) {
            REST = REST + addr[i] + "/";
            console.log(REST);
        }
    }
    RESTaddr = addr[1].toString() + "//" + REST;
    console.log("done");
}

function getRESTAddr() {
    return RESTaddr;
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
    var url = RESTaddr + "webresources/Users/Logout/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = logoutcallback;
    req.send(null);
}

function logoutcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            localStorage.setItem("sessionId", "");
            window.location.replace("index.html");
        }
    }
}

function whoAmI() {
    console.log("hei");
    var url = RESTaddr + "webresources/Users/View/Myself/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = whoCallback;
    req.send(null);
}

function whoCallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            crntUser = req.responseXML.childNodes;
            console.log(crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue);
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            document.getElementById("user-name").innerHTML = fname + " " + lname;
            console.log(crntUser[0].getElementsByTagName("access")[0].childNodes[0].nodeValue);
            myJobs = [];
            for (loop = 0; loop < crntUser[0].getElementsByTagName("jobs").length; loop++) {
                myJobs.push(String(crntUser[0].getElementsByTagName("jobs")[loop].childNodes[0].nodeValue));
            }
        }
    }
}

/*
 * Retrieves a list of customers from the backend and files them into the selector (on callback).
 */
function getCustomers() {
    var url = RESTaddr + "webresources/Users/View/AllCustomers/" + localStorage.getItem("sessionId");
    console.log(url);
    req2 = initRequest();
    req2.open("GET", url, true);
    req2.onreadystatechange = custselcallback;
    req2.send(null);
}

function custselcallback() {
    if (req2.readyState == 4) {
        if (req2.status == 200) {
            fileCustomers(req2.responseXML);
        }
    }
}

function fileCustomers(XML) {
    var custSel = document.getElementById('customer');
    $('#customer').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var option = document.createElement("option");
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            console.log(option);
            custSel.appendChild(option);
        }
    }
    getDevices();
}

function getDevices() {
    var url = RESTaddr + "webresources/Devices/" + document.getElementById('customer').value + "/" + localStorage.getItem("sessionId");
    console.log(url);
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = devselcallback;
    req.send(null);
}

function devselcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            fileDevices(req.responseXML);
        }
    }
}

function fileDevices(XML) {
    console.log(XML);
    var devSel = document.getElementById('device');
    $('#device').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            console.log(XML.childNodes[0]);
            console.log(XML.childNodes[0].childNodes[0]);
            var option = document.createElement("option");
            var device = XML.childNodes[0].childNodes[loop];
            console.log(device);
            console.log(device.getElementsByTagName("type")[0].childNodes[1]);
            console.log(device.getElementsByTagName("id")[0].childNodes[0]);
            option.value = [device.getElementsByTagName("id")[0].childNodes[0].nodeValue,device.getElementsByTagName("type")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue];
            option.innerHTML = "" + device.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            devSel.appendChild(option);
        }
    }
    //getTechnicians();
}

function addNewTask() {
    var taskList = document.getElementById('new-tasks');
    var newTaskTemp = document.getElementById('newTask-template').content.cloneNode(true);

    var title = document.getElementById('title').value;
    console.log(title);
    var customer = document.getElementById('customer').value;
    console.log(customer);
    var device = document.getElementById('device').value;
    console.log(device);
    var dueDate = document.getElementById('due-date').value;
    console.log(dueDate);
    var prio = document.getElementsByClassName("btn active").item(0).id;
    console.log(prio);

    newTaskTemp.querySelector('.widget-user-username').innerText = title;
    newTaskTemp.querySelector('#device-name').innerText = device;
    newTaskTemp.querySelector('#customerRequest').innerText = customer;
    newTaskTemp.querySelector('#due-dateTask').innerText = dueDate;

    //var url = RESTaddr + "webresources/Devices/CreateAssignment/"+title+"/"+deviceid+"/"+deadline+"/"+prio+"/"+customer+"/"+technician+"/"+localStorage.getItem("sessionId");

    taskList.appendChild(newTaskTemp);
    document.getElementById('addNewTaskForm').reset();
}


/* 
 //
 function fileSelections(XML){
 var uSel = document.getElementById("mpUserSelect");
 $('#mpUserSelect').empty();
 if(XML.childNodes[0].childNodes.length >0){
 for(loop=0;loop < XML.childNodes[0].childNodes.length;loop++){
 var option = document.createElement("option");
 var user = XML.childNodes[0].childNodes[loop];
 option.value=user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
 option.innerHTML=""+user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue+" "+user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
 uSel.appendChild(option);
 }
 }
 uSel.value=selUser;
 }
 */