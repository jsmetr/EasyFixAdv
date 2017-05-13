/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;
var req2;
var req3;
var RESTaddr;
var myrole;
var revid;
var targetcmntid;

function init() {
    grabAddr();
    whoAmI();
}

function clerkInit() {
    init();
    getCustomers();
    getAssignments();
}

function initForProfile() {
    grabAddr();
    printProfile();
}

function initEmpManager() {
    init();
    getEmployees();
}

function initReview() {
    if (localStorage.getItem("sessionId") == "") {
        myrole = "index";
        grabAddr()
    } else {
        init();
    }
    getReviews();
}

function grabAddr() {
    var addr = document.location.href.toString().split("/");
    var REST = "";
    for (var i = 1; i < addr.length; i++) {
        if (!addr[i].includes("html") && addr[i].toString().length > 0 && (typeof addr[i] !== 'undefined')) {
            REST = REST + addr[i] + "/";
        }
    }
    RESTaddr = addr[1].toString() + "//" + REST;
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
    if (!localStorage.getItem("sessionId").length > 0) {
        window.location.replace("index.html");
    } else {
        var url = RESTaddr + "webresources/Users/Logout/" + localStorage.getItem("sessionId");
        req = initRequest();
        req.open("PUT", url, true);
        req.onreadystatechange = logoutcallback;
        req.send(null);
    }
}

function logoutcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            localStorage.setItem("sessionId", "");
            window.location.replace("index.html");
        }
    }
}

function toMyPortal() {
    window.location.replace(myrole + ".html");
}

function toMyProfile() {
    if (myrole == "index") {
        window.location.replace(myrole + ".html");

    } else {
        window.location.replace(myrole + "_profile.html");
    }
}

function whoAmI() {
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
            var fname = crntUser[0].getElementsByTagName("firstName")[0].childNodes[0].nodeValue;
            var lname = crntUser[0].getElementsByTagName("lastName")[0].childNodes[0].nodeValue;
            myrole = crntUser[0].getElementsByTagName("role")[0].childNodes[0].nodeValue;
            document.getElementById("user-name").innerHTML = fname + " " + lname;
            myJobs = [];
            for (loop = 0; loop < crntUser[0].getElementsByTagName("jobs").length; loop++) {
                myJobs.push(String(crntUser[0].getElementsByTagName("jobs")[loop].childNodes[0].nodeValue));
            }
        }
    }
}

function getReviews() {
    var url = RESTaddr + "webresources/Devices/getReviews/Latest/10";
    req2 = initRequest();
    req2.open("GET", url, true);
    req2.onreadystatechange = getrevscallback;
    req2.send(null);
}

function getrevscallback() {
    if (req2.readyState == 4) {
        if (req2.status == 200) {
            var XML = req2.responseXML;
            var reviews = XML.getElementsByTagName("review");
            var revdiv = document.getElementById('reviews');
            for (var i = 0; i < reviews.length; i++) {
                var reviewshell = document.getElementById('review-template').content.cloneNode(true);
                reviewshell.querySelector('#rev-title').innerText = reviews[i].getElementsByTagName('title')[0].innerHTML;
                if (localStorage.getItem("sessionId").length > 0) {
                    reviewshell.querySelector('#rev-title').innerHTML = reviews[i].getElementsByTagName('title')[0].innerHTML + ' | <a data-target="#modal-reply" data-toggle="modal" id="replybtn" onclick="setTarget(' + reviews[i].getElementsByTagName('id')[0].innerHTML + ')"> Reply</a>';
                } else {
                    reviewshell.querySelector('#rev-title').innerText = reviews[i].getElementsByTagName('title')[0].innerHTML;
                }
                reviewshell.querySelector('#rev-uname').innerText = reviews[i].getElementsByTagName('signed')[0].innerHTML;
                reviewshell.querySelector('#commentsection').id = "commentsection" + reviews[i].getElementsByTagName('id')[0].innerHTML;
                var rating = parseInt(reviews[i].getElementsByTagName('rating')[0].innerHTML);
                var ii = 0;
                for (ii; ii < rating; ii++) {
                    var a = document.getElementById('star-template').content.cloneNode(true);
                    reviewshell.querySelector('#stars').appendChild(a);
                }
                for (ii; ii < 5; ii++) {
                    var a = document.getElementById('nostar-template').content.cloneNode(true);
                    reviewshell.querySelector('#stars').appendChild(a);
                }
                reviewshell.querySelector('#rev-body').innerText = reviews[i].getElementsByTagName('body')[0].innerHTML;
                var cmntlink = reviewshell.querySelector('#comment-toggle');
                cmntlink.innerHTML = '<a data-toggle="collapse" data-target="#' + reviews[i].getElementsByTagName('id')[0].innerHTML + '" class="btn btn-default stat-item" onclick = "getComments( ' + reviews[i].getElementsByTagName('id')[0].innerHTML + ')" ><i class="fa fa-share icon" ></i>View comments</a>';
                reviewshell.querySelector('#collapse-comments').id = reviews[i].getElementsByTagName('id')[0].innerHTML;
                revdiv.appendChild(reviewshell);
            }
        }
    }

}

function getComments(id) {
    revid = id;
    var url = RESTaddr + "webresources/Devices/getComments/" + id;
    req2 = initRequest();
    req2.open("GET", url, true);
    req2.onreadystatechange = getcommentscallback;
    req2.send(null);
}

function getcommentscallback() {
    if (req2.readyState == 4) {
        if (req2.status == 200) {
            var XML = req2.responseXML;
            var cmntsect = document.getElementById('commentsection' + revid);
            var comments = XML.getElementsByTagName("comment");
            var id = '#commentsection' + revid;
            console.log(XML);
            console.log(comments);
            $(id).empty();
            for (var i = 0; i < comments.length; i++) {
                fileComment(cmntsect, comments[i]);
            }
        }
    }
}

function fileComment(parent, comment) {
    var comments = [];
    var childs = comment.childNodes;
    for(var i=0;i<childs.length;i++){
        if(childs[i].nodeName=="comments"){
            comments.push(childs[i]);
        }
    }
    var cmnt = document.getElementById('comment-template').content.cloneNode(true);
    var temp = comment.getElementsByTagName('body');
    cmnt.querySelector('#comment-body').innerText = temp[0].innerHTML;
    temp = comment.getElementsByTagName('signed');
    console.log(temp[temp.length - 1].innerHTML);
    cmnt.querySelector('#signed').innerText = temp[temp.length - 1].innerHTML;
    temp = comment.getElementsByTagName('creationtime');
    if (localStorage.getItem("sessionId").length > 0) {
        cmnt.querySelector('#timestamp').innerHTML = temp[temp.length - 1].innerHTML + ' | <a data-target="#modal-reply" data-toggle="modal" id="replybtn" onclick="setCmntTarget(' + revid + ',' + comment.getElementsByTagName('id')[0].innerHTML + ')"> Reply</a>';
    } else {
        cmnt.querySelector('#timestamp').innerHTML = temp[temp.length - 1].innerHTML;
    }
    var nxtparent = cmnt.querySelector('#resp');
    parent.appendChild(cmnt);
    console.log(comments.length);
    if(comments.length == 0){
    } else {
        for (var i = 0; i < comments.length; i++) {
            fileComment(nxtparent, comments[i]);
        }
    }
}

function setTarget(reviewid) {
    revid = reviewid;
    console.log(revid);
    document.getElementById('formbtn').onclick = function () {
        replyToReview();
    };
}

function setCmntTarget(reviewid, cmntid) {
    setTarget(reviewid);
    targetcmntid = cmntid;
    console.log(targetcmntid);
    document.getElementById('formbtn').onclick = function () {
        replyToComment();
    };
}

function replyToComment() {
    var cbody = document.getElementById("cmntbody").value.toString();
    console.log(cbody);
    var url = RESTaddr + "webresources/Devices/respondToComment/" + cbody + "/" + revid + "/" + targetcmntid + "/" + localStorage.getItem("sessionId");
    req2 = initRequest();
    req2.open("POST", url, true);
    req2.onreadystatechange = replycallback;
    req2.send(null);
}

function replyToReview() {
    var rbody = document.getElementById("cmntbody").value.toString();
    console.log(rbody);
    var url = RESTaddr + "webresources/Devices/respondToReview/" + rbody + "/" + revid + "/" + localStorage.getItem("sessionId");
    req2 = initRequest();
    req2.open("POST", url, true);
    req2.onreadystatechange = replycallback;
    req2.send(null);
}

function replycallback() {
    if (req2.readyState == 4) {
        if (req2.status == 200) {
            console.log(req2.responseText);
            getReviews();
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
    getDeviceTypes();
}

function addNewTask() {
    var title = document.getElementById('title').value;
    var desc = document.getElementsByName('description').value;
    var customer = document.getElementById('customer').value;
    var device = document.getElementById('device').value;
    var dueDate = document.getElementById('due-date').value;
    var prio = document.getElementsByClassName("btn active").item(0).id;
    var deviceid = document.getElementById('device').value.toString().split(',')[0].toString();
    var technician = document.getElementById('technician').value;

    var url = RESTaddr + "webresources/Devices/CreateAssignment/" + title + "/" + desc + "/" + deviceid + "/" + dueDate + "/" + prio + "/" + customer + "/" + technician + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = taskaddcallback;
    req.send(null);
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

function addEmployee() {
    var fname = document.getElementById('fname').value.toString();
    var lname = document.getElementById('lname').value.toString();
    var uname = document.getElementById('username').value.toString();
    var psw = document.getElementById('password').value.toString();
    var email = document.getElementById('email').value.toString();
    var phone = document.getElementById('phone').value.toString();
    var role = document.getElementById('role').value.toString();
    var skills = document.getElementById('skills').value.toString();
    if (!skills.length > 0) {
        skills = "mu:0";
    }
    var url = RESTaddr + "webresources/Users/AddEmpl/" + fname + "/" + lname + "/" + uname + "/" + psw + "/" + email + "/" + phone + "/" + role + "/" + skills + "/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = addcustcallback;
    req.send(null);
    document.getElementById('addEmplForm').reset();
}

function addcustcallback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            console.log(req.responseText);
        }
    }
}

function getEmployees() {
    url = RESTaddr + "webresources/Users/View/AllEmployees/" + localStorage.getItem("sessionId");
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = getemplcallback;
    req3.send(null);
}

function getemplcallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            var XML = req3.responseXML;
            var carddiv = document.getElementById('empcards');
            $('#empcards').empty();
            if (XML.childNodes[0].childNodes.length > 0) {
                for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
                    var empcard = document.getElementById('cardtemplate').content.cloneNode(true);
                    var taskdata = XML.childNodes[0].childNodes[loop];
                    console.log(taskdata)
                    console.log(taskdata.getElementsByTagName("firstName")[0].innerHTML);

                    empcard.querySelector('#empname').innerText = taskdata.getElementsByTagName("firstName")[0].innerHTML + " " + taskdata.getElementsByTagName("lastName")[0].innerHTML;
                    empcard.querySelector('#emprole').innerText = taskdata.getElementsByTagName("role")[0].childNodes[0].nodeValue;
                    var uname = (taskdata.getElementsByTagName("userName")[0].childNodes[0].nodeValue);
                    var element = empcard.getElementById('detailslink');
                    console.log(uname);
                    addempldetail(uname, element);
                    carddiv.appendChild(empcard);
                }
            }
        }
    }
}

function addempldetail(param, elem) {
    elem.onclick = function () {
        console.log(param);
        getEmpDetail(param);
    };
}

function getEmpDetail(uname) {
    console.log(uname);
    url = RESTaddr + "webresources/Users/View/" + uname + "/" + localStorage.getItem("sessionId");
    console.log(url);
    req3 = initRequest();
    req3.open("GET", url, true);
    req3.onreadystatechange = getdetailscallback;
    req3.send(null);
}

function getdetailscallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            var XML = req3.responseXML;
            var detcard = document.getElementById('detailscard');
            console.log(XML);
            var skills = XML.getElementsByTagName("skills");
            $('#skillspan').empty();
            if (skills.length > 0) {
                document.getElementById('skilltitle').innerHTML = "Skills:";
                var skillspan = document.getElementById('skillspan')
                for (var i = 0; i < skills.length; i++) {
                    console.log(document.getElementById('skilltemplate'));
                    var skillelem = document.getElementById('skilltemplate').content.cloneNode(true);
                    skillelem.querySelector('#skillname').innerHTML = skills[i].getElementsByTagName("devicetype")[0].innerHTML + '<span class="padded-left">' + skills[i].getElementsByTagName("level")[0].innerHTML + '</span>';
                    skillspan.appendChild(skillelem);
                }
            }
            document.getElementById('skilltemplate'); //detailname detailusername detailrole detailemail detailphone 
            console.log(XML.getElementsByTagName("firstName")[0].innerHTML);
            document.getElementById('myModalLabel').innerHTML = "More About " + XML.getElementsByTagName("firstName")[0].innerHTML;
            document.getElementById('detailname').innerHTML = XML.getElementsByTagName("firstName")[0].innerHTML + " " + XML.getElementsByTagName("lastName")[0].innerHTML;
            document.getElementById('detailusername').innerHTML = XML.getElementsByTagName("userName")[0].innerHTML.toString();
            document.getElementById('detailrole').innerHTML = XML.getElementsByTagName("role")[0].innerHTML;
            document.getElementById('detailphone').innerHTML = XML.getElementsByTagName("phone")[0].innerHTML;
            document.getElementById('detailemail').innerHTML = XML.getElementsByTagName("email")[0].innerHTML;
            document.getElementById('detailemail').href = "mailto:" + XML.getElementsByTagName("email")[0].innerHTML;
            document.getElementById('reinstate').onclick = function () {
                reinstate(XML.getElementsByTagName("role")[0].innerHTML.toString(), XML.getElementsByTagName("userName")[0].innerHTML.toString())
            };
            document.getElementById('retire').onclick = function () {
                setStatus(-1, XML.getElementsByTagName("userName")[0].innerHTML.toString())
            };
            if (parseInt(XML.getElementsByTagName("access")[0].innerHTML) > -1) {
                $('#reinstate').hide();
                $('#retire').show();
            } else {
                $('#retire').hide();
                $('#reinstate').show();

            }
        }
    }
}

function reinstate(role, uname) {
    if (role == ("manager")) {
        console.log("is manager");
        setStatus(2, uname);
    } else {
        setStatus(1, uname);
    }
}

function setStatus(newstatus, uname) {
    url = RESTaddr + "webresources/Users/ChangeStatus/" + newstatus + "/" + uname + "/" + localStorage.getItem("sessionId");
    req3 = initRequest();
    req3.open("PUT", url, true);
    1
    req3.onreadystatechange = setstatuscallback;
    req3.send(null);
}

function setstatuscallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            console.log(req3.responseText);
            getEmployees();
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
            var id = taskdata.getElementsByTagName("id")[1].innerHTML.toString();
            newTaskTemp.querySelector('#archrep').onclick = function () {
                archiveTask(id)
            };
            console.log(taskdata);
            var titles = taskdata.getElementsByTagName("title");
            console.log(titles);
            newTaskTemp.querySelector('#repairtitle').innerText = taskdata.getElementsByTagName("title")[titles.length - 1].childNodes[0].nodeValue;
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
    if (XML.childNodes[0].childNodes.length > 0) {
        for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
            var newTaskTemp = document.getElementById('canceled-template').content.cloneNode(true);
            var taskdata = XML.childNodes[0].childNodes[loop];
            var id = taskdata.getElementsByTagName("id")[1].innerHTML.toString();
            newTaskTemp.querySelector('#archcancel').onclick = function () {
                archiveTask(id)
            };
            newTaskTemp.querySelector('#canceltitle').innerText = taskdata.getElementsByTagName("title")[0].innerHTML;
            newTaskTemp.querySelector('#canceldate').innerText = taskdata.getElementsByTagName("deadline")[0].childNodes[0].nodeValue;
            newTaskTemp.querySelector('#cancelcust').innerText = taskdata.getElementsByTagName("customer")[0].childNodes[0].nodeValue
            newTaskTemp.querySelector('#cancelname').innerText = taskdata.getElementsByTagName("device")[0].getElementsByTagName("name")[0].innerHTML.toString();
            taskList.appendChild(newTaskTemp);
        }
    }

}

function archiveTask(taskid) {
    //"/Assignment/{assignmentid}/{newstatus}/{sessionId}"
    url = RESTaddr + "webresources/Devices/Assignment/" + taskid + "/2/" + localStorage.getItem("sessionId");
    req = initRequest();
    req.open("PUT", url, true);
    req.onreadystatechange = archivecallback;
    req.send(null);
}

function archivecallback() {
    if (req3.readyState == 4) {
        if (req3.status == 200) {
            getAssignments();
        }
    }
}

function printProfile() {
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


            if (crntUser[0].getElementsByTagName("role")[0].childNodes[0].nodeValue == "customer") {
                var address = crntUser[0].getElementsByTagName("address")[0].childNodes[0].nodeValue;
                var city = crntUser[0].getElementsByTagName("city")[0].childNodes[0].nodeValue;
                var state = crntUser[0].getElementsByTagName("state")[0].childNodes[0].nodeValue;
                var zipcode = crntUser[0].getElementsByTagName("zipcode")[0].childNodes[0].nodeValue;

                document.getElementById("address-profile").innerHTML = address;
                document.getElementById("city-profile").innerHTML = city;
                document.getElementById("state-profile").innerHTML = state;
                document.getElementById("zipCode-profile").innerHTML = zipcode;
            }
        }
    }
}

function showTechInfo(element) {
    var techName = element.querySelector('#technician-hidden-name').innerText;
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
            document.getElementById("address-cusInfo").innerHTML = address;
            document.getElementById("city-cusInfo").innerHTML = city;
            document.getElementById("state-cusInfo").innerHTML = state;
            document.getElementById("zipcode-cusInfo").innerHTML = zipcode;
        }
    }
}
