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
}

function clerkInit(){
    init();
    getCustomers();
    getAssignments();
}

function initForProfile() {
    grabAddr();
    printProfile();
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
    if (XML.childNodes[0].childNodes.length > 0) {
        var option = document.createElement("option");
        option.value = "automate";
        option.innerHTML = "automate";
        techSel.appendChild(option);
        var option = document.createElement("option");
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            option = document.createElement("option");
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            option.innerHTML = "" + user.getElementsByTagName("firstName")[0].childNodes[0].nodeValue + " " + user.getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            techSel.appendChild(option);
        }
    } else {
        var option = document.createElement("option");
        option.value = "noqualified";
        option.innerHTML = "No qualified techs";
        techSel.appendChild(option);
        var option = document.createElement("option");
    }
    console.log(document.getElementById('device').value.toString().split(',')[1]);
    getDeviceTypes();
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
    var deviceid = document.getElementById('device').value.toString().split(',')[0].toString();
    console.log(deviceid);
    var technician = document.getElementById('technician').value;
    console.log(document.getElementById('device').value.toString().split(',')[0]);

    newTaskTemp.querySelector('.widget-user-username').innerText = title;
    newTaskTemp.querySelector('#device-name').innerText = device;
    newTaskTemp.querySelector('#customerRequest').innerText = customer;
    newTaskTemp.querySelector('#due-dateTask').innerText = dueDate;
    newTaskTemp.querySelector('#technician-hidden-name').innerText = technician;

    var url = RESTaddr + "webresources/Devices/CreateAssignment/" + title + "/" + deviceid + "/" + dueDate + "/" + prio + "/" + customer + "/" + technician + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    ///CreateAssignment/{title}/{deviceid}/{deadline}/{prio}/{customer}/{technician}/{sessionId}
    req.open("POST", url, true);
    req.onreadystatechange = taskaddcallback;
    req.send(null);

    taskList.appendChild(newTaskTemp);
   
     document.getElementById('addNewTaskForm').reset();
     
}

function taskaddcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
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
    var devSel = document.getElementById('dev-types');
    $('#dev-types').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var option = document.createElement("option");
            var user = XML.childNodes[0].childNodes[loop];
            option.value = user.getElementsByTagName("name")[0].childNodes[0].nodeValue.toString();
            option.innerHTML = "" + user.getElementsByTagName("name")[0].childNodes[0].nodeValue;
            devSel.appendChild(option);
        }
    }
}

function addDevice() {
    ///AddDevice/{type}/{name}/{owner}/{manufacturer}/{model}/{sessionId}
    var devtype = document.getElementById('dev-types').value;
    var name = document.getElementById('dev-name').value;
    var owner = document.getElementById('dev-owner').value;
    var manu = document.getElementById('dev-manufacturer').value;
    var url = RESTaddr + "webresources/Devices/AddDevice/" + devtype + "/" + name + "/" + owner + "/" + manu + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = techselcallback;
    req.send(null);
    document.getElementById('addDeviceForm').reset();
    getCustomers();
}

function adddevicecallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
}

function addType() {
    //  /AddType/{name}/{data}/{sessionId}
    var name = document.getElementById('type-name').value;
    var data = document.getElementById('type-data').value;
    var url = RESTaddr + "webresources/Devices/AddType/" + name + "/" + data + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = addtypecallback;
    req.send(null);
    document.getElementById('addTypeForm').reset();
    getCustomers();
}

function addtypecallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
}

function addCustomer() {
    // /AddCust/{fname}/{lname}/{uname}/{psw}/{email}/{phone}/{address}/{city}/{state}/{zipcode}/{sessionId}
    var devtype = document.getElementById('dev-types').value;
    var fname = document.getElementById('fname').value.toString();
    var lname = document.getElementById('lname').value.toString();
    var uname = document.getElementById('username').value.toString();
    var psw = document.getElementById('password').value.toString();
    var email = document.getElementById('email').value.toString();
    var phone = document.getElementById('phone').value.toString();
    var address = document.getElementById('address').value.toString();
    var city = document.getElementById('city').value.toString();
    var state = document.getElementById('state').value.toString();
    var zipcode = document.getElementById('zipcode').value.toString();
    var url = RESTaddr + "webresources/Users/AddCust/" + fname + "/" + lname + "/" + uname + "/" + psw + "/" + email + "/" + phone + "/" + address + "/" + city + "/" + state + "/" + zipcode + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = addcustcallback;
    req.send(null);
    document.getElementById('addCustForm').reset();
    getCustomers();
}

function addcustcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
}

function getAssignments() {
    url = RESTaddr + "webresources/Devices/Assignments/Active/" + localStorage.getItem("sessionId");
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = getassignmentcallback;
    req3.send(null);
}

function getassignmentcallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            fileAssignments(req3.responseXML);
        }
    }
}

function fileAssignments(XML) {
    var taskList = document.getElementById('new-tasks');
    $('#new-tasks').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var newTaskTemp = document.getElementById('newTask-template').content.cloneNode(true);
            var taskdata = XML.childNodes[0].childNodes[loop];
            newTaskTemp.querySelector('.widget-user-username').innerText = taskdata.getElementsByTagName("title")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#device-name').innerText = taskdata.getElementsByTagName("device")[0].getElementsByTagName("name")[0].innerHTML.toString();
            newTaskTemp.querySelector('#customerRequest').innerText = taskdata.getElementsByTagName("customer")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#due-dateTask').innerText = taskdata.getElementsByTagName("deadline")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#technician-hidden-name').innerText = taskdata.getElementsByTagName("technician")[0].childNodes[0].nodeValue;
            taskList.appendChild(newTaskTemp);
        }
    }
    getRepaired();
}

function getRepaired() {
    url = RESTaddr + "webresources/Devices/Assignments/Repaired/" + localStorage.getItem("sessionId");
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = repairedcallback;
    req3.send(null);
}

function repairedcallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            fileRepaired(req3.responseXML);
        }
    }
}

function fileRepaired(XML) {
    var taskList = document.getElementById('returned-tasks');
    $('#returned-tasks').empty();
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var newTaskTemp = document.getElementById('repaired-template').content.cloneNode(true);
            var taskdata = XML.childNodes[0].childNodes[loop];
            var id=taskdata.getElementsByTagName("id")[1].innerHTML.toString();
            newTaskTemp.querySelector('#archrep').onclick = function(){archiveTask(id)};
            newTaskTemp.querySelector('#repairtitle').innerText = taskdata.childNodes[10].innerHTML
            newTaskTemp.querySelector('#repairname').innerText = taskdata.getElementsByTagName("device")[0].getElementsByTagName("name")[0].innerHTML.toString();
            newTaskTemp.querySelector('#repaircust').innerText = taskdata.getElementsByTagName("customer")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#repairdate').innerText = taskdata.getElementsByTagName("deadline")[0].childNodes[0].nodeValue;
            taskList.appendChild(newTaskTemp);
        }
    }
    getCanceled();
}

function getCanceled() {
    url = RESTaddr + "webresources/Devices/Assignments/Canceled/" + localStorage.getItem("sessionId");
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = canceledcallback;
    req3.send(null);
}

function canceledcallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            fileCanceled(req3.responseXML);
        }
    }
}

function fileCanceled(XML) {
    var taskList = document.getElementById('returned-tasks');
    console.log(XML);
    console.log(XML.childNodes[0].childNodes.length);
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var newTaskTemp = document.getElementById('canceled-template').content.cloneNode(true);
            var taskdata = XML.childNodes[0].childNodes[loop];
            var id=taskdata.getElementsByTagName("id")[1].innerHTML.toString();
            newTaskTemp.querySelector('#archcancel').onclick = function(){archiveTask(id)};
            newTaskTemp.querySelector('#canceltitle').innerText = taskdata.getElementsByTagName("title")[0].innerHTML;
            newTaskTemp.querySelector('#canceldate').innerText = taskdata.getElementsByTagName("deadline")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#cancelcust').innerText = taskdata.getElementsByTagName("customer")[0].childNodes[0].nodeValue
            newTaskTemp.querySelector('#cancelname').innerText = taskdata.getElementsByTagName("device")[0].getElementsByTagName("name")[0].innerHTML.toString();
            taskList.appendChild(newTaskTemp);
        }
    }

}

function archiveTask(taskid){
    //"/Assignment/{assignmentid}/{newstatus}/{sessionId}"
    url = RESTaddr + "webresources/Devices/Assignment/"+taskid+"/2/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = archivecallback;
    req.send(null);
}

function archivecallback(){
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            getAssignments();
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

function printProfile() {
    console.log("hei-profile");
    var url = RESTaddr + "webresources/Users/View/Myself/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = printProfileCallBack;
    req.send(null);
}

function printProfileCallBack() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            crntUser = req.responseXML.childNodes;
            console.log(crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue);
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            var uname = crntUser[0].getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            var email = crntUser[0].getElementsByTagName("email")[0].childNodes[0].nodeValue;
            var phone = crntUser[0].getElementsByTagName("phone")[0].childNodes[0].nodeValue;
            
            document.getElementById("fullName-profile").innerHTML = fname + " " + lname;
            document.getElementById("user-name").innerHTML = fname + " " + lname;
            document.getElementById("uName-profile").innerHTML = uname;
            document.getElementById("email-profile").innerHTML = email;
            document.getElementById("phone-profile").innerHTML = phone;
            
                        
            if(crntUser[0].getElementsByTagName("role")[0].childNodes[0].nodeValue == "customer") {
                var address = crntUser[0].getElementsByTagName("address")[0].childNodes[0].nodeValue;
                var city = crntUser[0].getElementsByTagName("city")[0].childNodes[0].nodeValue;
                var state = crntUser[0].getElementsByTagName("state")[0].childNodes[0].nodeValue;
                var zipcode = crntUser[0].getElementsByTagName("zipcode")[0].childNodes[0].nodeValue;
                
                document.getElementById("address-profile").innerHTML = address;
                document.getElementById("city-profile").innerHTML = city;
                document.getElementById("state-profile").innerHTML = state;
                document.getElementById("zipCode-profile").innerHTML = zipcode;
            }
            console.log(crntUser[0].getElementsByTagName("access")[0].childNodes[0].nodeValue);//           
        }
    }
}

function showTechInfo(element) {
    var techName = element.querySelector('#technician-hidden-name').innerText;
    console.log("tech " + techName);
    var url = RESTaddr + "webresources/Users/View/" + techName + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = showTechInfoCallBack;
    req.send(null);
}
function showTechInfoCallBack() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            crntUser = req.responseXML.childNodes;
            console.log("hello");
            console.log(crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue);
            
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            var uname = crntUser[0].getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            var email = crntUser[0].getElementsByTagName("email")[0].childNodes[0].nodeValue;
            var phone = crntUser[0].getElementsByTagName("phone")[0].childNodes[0].nodeValue;            
            
            document.getElementById("fullName-techInfo").innerHTML = fname + " " + lname;
            document.getElementById("firstName-techInfo").innerHTML = fname;
            document.getElementById("uName-techInfo").innerHTML = uname;
            document.getElementById("email-techInfo").innerHTML = email;
            document.getElementById("phone-techInfo").innerHTML = phone;
        }
    }
}

function showCusInfo(element) {
    var cusName = element.querySelector('#customerRequest').innerHTML;
    console.log("cus " + cusName);
    var url = RESTaddr + "webresources/Users/View/" + cusName + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = showCusInfoCallBack;
    req.send(null);
}
function showCusInfoCallBack() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            crntUser = req.responseXML.childNodes;
            
            console.log(crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue);
            
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            var uname = crntUser[0].getElementsByTagName("userName")[0].childNodes[0].nodeValue;
            var email = crntUser[0].getElementsByTagName("email")[0].childNodes[0].nodeValue;
            var phone = crntUser[0].getElementsByTagName("phone")[0].childNodes[0].nodeValue;
            var address = crntUser[0].getElementsByTagName("address")[0].childNodes[0].nodeValue;
            var city = crntUser[0].getElementsByTagName("city")[0].childNodes[0].nodeValue;
            var state = crntUser[0].getElementsByTagName("state")[0].childNodes[0].nodeValue;
            var zipcode = crntUser[0].getElementsByTagName("zipcode")[0].childNodes[0].nodeValue;
            
            document.getElementById("fullName-cusInfo").innerHTML = fname + " " + lname;            
            document.getElementById("uName-cusInfo").innerHTML = uname;
            document.getElementById("email-cusInfo").innerHTML = email;
            document.getElementById("phone-cusInfo").innerHTML = phone;
            document.getElementById("address-cusInfo").innerHTML = email;
            document.getElementById("city-cusInfo").innerHTML = city;
            document.getElementById("state-cusInfo").innerHTML = state;
            document.getElementById("zipcode-cusInfo").innerHTML = zipcode;
        }
    }
}
