/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var req2;
var req3;
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
    var ownSel = document.getElementById('dev-owner');
    $('#customer').empty();
    $('#dev-owner').empty();
    console.log(XML);
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var option = document.createElement("option");
            var option2 = option.cloneNode();
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            option2.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option2.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            custSel.appendChild(option);
            ownSel.appendChild(option2);
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
    var devSel = document.getElementById('device');
    $('#device').empty();
    console.log(XML);
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var option = document.createElement("option");
            var device = XML.childNodes[0].childNodes[loop];
            option.value = [device.getElementsByTagName("id")[0].childNodes[0].nodeValue, device.getElementsByTagName("type")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue];
            option.innerHTML = "" + device.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            devSel.appendChild(option);
        }
    }
    getTechnicians();
}

function getTechnicians() {
    var values = document.getElementById('device').value.toString().split(',');
    var url = RESTaddr + "webresources/Users/GetTech/" + values[1] + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = techselcallback;
    req.send(null);
}

function techselcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            fileTechs(req.responseXML);
        }
    }
}

function fileTechs(XML) {
    var techSel = document.getElementById('technician');
    $('#technician').empty();
    var option = document.createElement("option");
    option.value = "automate";
    option.innerHTML = "automate";
    techSel.appendChild(option);
    var option = document.createElement("option");
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            option = document.createElement("option");
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            techSel.appendChild(option);
        }
    }
    console.log(document.getElementById('device').value.toString().split(',')[1]);
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
    var deviceid=document.getElementById('device').value.toString().split(',')[0].toString();
    console.log(deviceid);
    var technician=document.getElementById('technician').value;
    console.log(document.getElementById('device').value.toString().split(',')[0]);

    newTaskTemp.querySelector('.widget-user-username').innerText = title;
    newTaskTemp.querySelector('#device-name').innerText = device;
    newTaskTemp.querySelector('#customerRequest').innerText = customer;
    newTaskTemp.querySelector('#due-dateTask').innerText = dueDate;

    var url = RESTaddr + "webresources/Devices/CreateAssignment/"+title+"/"+deviceid+"/"+dueDate+"/"+prio+"/"+customer+"/"+technician+"/"+localStorage.getItem("sessionId");
    req = initRequest();
    ///CreateAssignment/{title}/{deviceid}/{deadline}/{prio}/{customer}/{technician}/{sessionId}
    req.open("POST", url, true);
    req.onreadystatechange = taskaddcallback;
    req.send(null);

    taskList.appendChild(newTaskTemp);
    /*
    document.getElementById('addNewTaskForm').reset();
    document.body.className ="dashboard";
    document.getElementById('modal-add-task').className="modal fade";
    document.getElementById('modal-add-task').style.display="none";
    document.getElementsByClassName('modal-backdrop fade in').entries()[0].
    */
    //document.body.className.replace("no-javascript","")"dashboard";
}

function taskaddcallback(){
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
}

function getDeviceTypes(){
    var url = RESTaddr + "webresources/Devices/Devicetypes";
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = gettypescallback;
    req3.send(null);
}

function gettypescallback(){
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            fileTypes(req3.responseXML);
        }
    }
}

function fileTypes(){
    var techSel = document.getElementById('technician');
    $('#technician').empty();
    var option = document.createElement("option");
    option.value = "automate";
    option.innerHTML = "automate";
    techSel.appendChild(option);
    var option = document.createElement("option");
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            option = document.createElement("option");
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            techSel.appendChild(option);
        }
    }
}

function addDevice() {
    ///AddDevice/{type}/{name}/{owner}/{manufacturer}/{model}/{sessionId}
    var devtype= document.getElementById('dev-types').value;
    console.log(devtype);
    var name = document.getElementById('dev-name').value;
    var owner = document.getElementById('owner').value;
    var manu = document.getElementById('manufacturer').value;
    var url = RESTaddr + "webresources/Devices/AddDevice/" +devtype+ "/" +name+ "/" +owner+ "/" +manu+ "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = techselcallback;
    req.send(null);
    document.getElementById('addDeviceForm').reset();
}

function adddevicecallback(){
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
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