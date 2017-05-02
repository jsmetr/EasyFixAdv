/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var req;

//simple testing script run on landing page load (body onload='testinit()') to ensure REST can be reached from front-end

function testinit(){
    testrest();
}

function initRequest() {
    if(window.XMLHttpRequest){
        if(navigator.userAgent.indexOf('MSIE') !== -1){
            isIE=true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject){
        isIE=true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}
function testrest(){
    var url= "http://localhost:8080/EasyFix/webresources/Testing/Response";
    req=initRequest();
    req.open("GET",url,true);
    req.onreadystatechange = resttestcallback;
    req.send(null);
}

function resttestcallback(){
    if (req.readyState == 4){
        if(req.status == 200){
            if(req.responseText!=="FAILURE"){
                console.log(req.responseText);
            }else {
                alert("Your username and password are invalid.")
            }
        }
    }
}