/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var req4;
var req5;
var RESTaddr;
var allOrders;
var canceledOrders;


function reportInit() {
    init();
    getAllAssignments();
    RESTaddr = getRESTAddr();
}

function getAllAssignments() {
    console.log("getting");
    var url = RESTaddr + "webresources/Devices/Graph/Assignments/All/" + localStorage.getItem("sessionId");
    console.log(url);
    req5 = initRequest();
    req5.open("GET", url, true);
    req5.onreadystatechange = getassigncallback;
    req5.send(null);
}

function getassigncallback() {
    if (req5.readyState == 4) {
        if (req5.status == 200) {
            if (req5.responseText !== "FAILURE") {
                var XML = req5.responseXML;
                allOrders = XML;
                var dData = [];
                var colors = ["#4ED18F", "#15BA67", "#5BAABF", "#94D7E9", "#BBE0E9"];
                var colorpick = 0;
                console.log(XML);

                if (XML.childNodes[0].childNodes.length > 0) {
                    for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
                        var item = XML.childNodes[0].childNodes[loop];
                        console.log(item.getElementsByTagName("name")[0].childNodes[0].nodeValue);
                        dData.push({
                            value: parseInt(item.getElementsByTagName("count")[0].childNodes[0].nodeValue)
                            , color: colors[colorpick]
                            , highlight: "#15BA67"
                            , label: item.getElementsByTagName("name")[0].childNodes[0].nodeValue
                        });
                        colorpick++;
                        if (colorpick >= colors.length) {
                            colorpick = 0;
                        }
                    }
                }
                var ctx = $(".doughnut-chart")[0].getContext("2d");
                window.myDoughnut = new Chart(ctx).Pie(dData, {
                    responsive: true
                    , showTooltips: true
                });
                getCanceledAssignments();
            }
        }
    }
}

function getCanceledAssignments() {
    var url = RESTaddr + "webresources/Devices/Graph/Assignments/Canceled/" + localStorage.getItem("sessionId");
    req5 = initRequest();
    req5.open("GET", url, true);
    req5.onreadystatechange = getcancelcallback;
    req5.send(null);
}

function getcancelcallback() {
    if (req5.readyState == 4) {
        if (req5.status == 200) {
            if (req5.responseText !== "FAILURE") {
                var XML = req5.responseXML;
                canceledOrders = XML;
                var pData = [];

                if (XML.childNodes[0].childNodes.length > 0) {
                    for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
                        var item = XML.childNodes[0].childNodes[loop];
                        console.log(item.getElementsByTagName("name")[0].childNodes[0].nodeValue);
                        pData.push({
                            value: parseInt(item.getElementsByTagName("count")[0].childNodes[0].nodeValue)
                            , color: "#BBE0E9"
                            , highlight: "#15BA67"
                            , label: item.getElementsByTagName("name")[0].childNodes[0].nodeValue
                        });
                    }
                }
                console.log(pData);
                var ctx2 = $(".pie-chart")[0].getContext("2d");
                window.myPie = new Chart(ctx2).Pie(pData, {
                    responsive: true
                    , showTooltips: true
                });
                getSkills()
            }
        }
    }
}

function getSkills() {
    var url = RESTaddr + "webresources/Devices/Graph/Skills/" + localStorage.getItem("sessionId");
    console.log(url);
    req5 = initRequest();
    req5.open("GET", url, true);
    req5.onreadystatechange = skillscallback;
    req5.send(null);
}

function skillscallback() {
    if (req5.readyState == 4) {
        if (req5.status == 200) {
            if (req5.responseText !== "FAILURE") {
                var XML = req5.responseXML;
                var Data = [];
                var colors = ["#4ED18F", "#15BA67", "#5BAABF", "#94D7E9", "#BBE0E9"];
                var colorpick = 0;
                console.log(XML);

                if (XML.childNodes[0].childNodes.length > 0) {
                    for (loop = 0; loop < XML.childNodes[0].childNodes.length; loop++) {
                        var item = XML.childNodes[0].childNodes[loop];
                        console.log(item.getElementsByTagName("name")[0].childNodes[0].nodeValue);
                        Data.push({
                            value: parseInt(item.getElementsByTagName("count")[0].childNodes[0].nodeValue)
                            , color: colors[colorpick]
                            , highlight: "#15BA67"
                            , label: item.getElementsByTagName("name")[0].childNodes[0].nodeValue
                        });
                        colorpick++;
                        if (colorpick >= colors.length) {
                            colorpick = 0;
                        }
                    }
                }
                var ctx6 = $(".polar-chart")[0].getContext("2d");
                window.myPolar = new Chart(ctx6).PolarArea(Data, {
                    responsive: true
                    , showTooltips: true
                });
                radarData();
            }
        }
    }
}

function radarData() {
    var labels = [];
    var orders = [];
    var cancels = [];
    var size = 0;
    if (allOrders.childNodes[0].childNodes.length > canceledOrders.childNodes[0].childNodes.length) {
        size = allOrders.childNodes[0].childNodes.length;
        var main = orders;
        var second = cancels;
        var mainD = allOrders;
        var secondD = canceledOrders;
        var cancelfin = second;
        var ordersfin = main;
        console.log("order-main");
    } else {
        size = canceledOrders.childNodes[0].childNodes.length;
        var main = cancels;
        var second = orders;
        var mainD = canceledOrders;
        var secondD = allOrders;
        var cancelfin = main;
        var ordersfin = second;
        console.log("cancel-main");
    }
    for (var i = 0; i < size; i++) {

        var item = mainD.childNodes[0].childNodes[i];
        labels.push(item.getElementsByTagName("name")[0].childNodes[0].nodeValue);
        main.push(parseInt(item.getElementsByTagName("count")[0].childNodes[0].nodeValue));
        second.push(0);
    }
    console.log(secondD.childNodes);
    console.log(secondD.childNodes.length);
    for (var ii = 0; ii < secondD.childNodes.length; ii++) {
        {
            console.log(secondD.childNodes[ii].getElementsByTagName("name")[0].childNodes[0].nodeValue);
            var index = labels.indexOf(secondD.childNodes[ii].getElementsByTagName("name")[0].childNodes[0].nodeValue);
            if(index>-1){
                second[index]=parseInt(secondD.childNodes[ii].getElementsByTagName("count")[0].childNodes[0].nodeValue);
            } else{
                labels.push(secondD.childNodes[ii].getElementsByTagName("name")[0].childNodes[0].nodeValue)
                second.push(parseInt(secondD.childNodes[ii].getElementsByTagName("count")[0].childNodes[0].nodeValue));
                main.push(0);
            }
        }
    }
    var radarData = {
        labels: labels
        , datasets: [
            {
                label: "All Orders"
                , fillColor: "rgba(21,186,103,0.5)"
                , strokeColor: "rgba(220,220,220,1)"
                , pointColor: "rgba(220,220,220,1)"
                , pointStrokeColor: "#fff"
                , pointHighlightFill: "#fff"
                , pointHighlightStroke: "rgba(220,220,220,1)"
                , data: ordersfin
            }
            , {
                label: "Canceled Orders"
                , fillColor: "rgba(21,113,186,0.5)"
                , strokeColor: "rgba(151,187,205,1)"
                , pointColor: "rgba(151,187,205,1)"
                , pointStrokeColor: "#fff"
                , pointHighlightFill: "#fff"
                , pointHighlightStroke: "rgba(151,187,205,1)"
                , data: cancelfin
            }
        ]
    };
    var ctx5 = $(".radar-chart")[0].getContext("2d");
    window.myRadar = new Chart(ctx5).Radar(radarData, {
        responsive: true
        , showTooltips: true
    });
}
