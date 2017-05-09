/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var req2;
var RESTaddr;


function reportInit() {
    testrest();
    console.log(localStorage.getItem("sessionId"));
    RESTaddr = getRESTAddr();
}

function testrest() {
    var url = RESTaddr + "webresources/Testing/TickTock";
    req2 = initRequest();
    req2.open("GET", url, true);
    req2.onreadystatechange = resttestcallback;
    req2.send(null);
}

function resttestcallback() {
    if (req2.readyState == 4) {
        if (req2.status == 200) {
            if (req2.responseText !== "FAILURE") {
                console.log(req2.responseText);
                getAllAssignments();
            }
        }
    }
}

function getAllAssignments() {
    var url = RESTaddr + "webresources/Devices/Assignments"
    var req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = getAllAssignmentCallback(req);
    req.send(null);
}

function getAllAssignmentCallback(req) {
    if (req.readyState == 4) {
        if (req.status == 200) {
            if (req.responseText !== "FAILURE") {
                console.log(req.responseText);
            }
        }
    }
}

function setDoughnut() {
    getAllAssignments();
    var doughnutData = [
        {
            value: 100
            , color: "#4ED18F"
            , highlight: "#15BA67"
            , label: "Device type 01"
        }
        , {
            value: 250
            , color: "#15BA67"
            , highlight: "#15BA67"
            , label: "Device type 02"
        }
        , {
            value: 100
            , color: "#5BAABF"
            , highlight: "#15BA67"
            , label: "Device type 03"
        }
        , {
            value: 40
            , color: "#94D7E9"
            , highlight: "#15BA67"
            , label: "Device type 04"
        }
        , {
            value: 120
            , color: "#BBE0E9"
            , highlight: "#15BA67"
            , label: "Device type 05"
        }

    ];
    doughnutData.push({
        value: 120
        , color: "#BBE0E9"
        , highlight: "#15BA67"
        , label: "Device type 05"
    });
    return doughnutData;
}
