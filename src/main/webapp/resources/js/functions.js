/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function openNav() {
    document.getElementById("mySidenav").style.width = "350px";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

$.blockUI.defaults.overlayCSS.opacity = 0;
$.blockUI.defaults.message='<h1><img src="javax.faces.resource/images/waitcursor.gif.jsf?ln=bsf" /></h1>';




function unblock() {
    $.unblockUI();
}