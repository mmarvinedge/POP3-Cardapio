/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    setarCompany();
});
function callBack() {
    PF('loading').show();
    backGrupos();
    PF('loading').hide();
}

//<![CDATA[
function finalizarPedido() {
    Swal.fire({
        title: 'Muito Bom',
        text: "Seu pedido foi enviado!",
        icon: 'success',
        allowOutsideClick: false,
        closeOnClickOutside: false,
        showCancelButton: false,
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Clique Para Sair!'
    }).then((result) => {
        if (result.value) {
            backGrupos();
            redirecionar();
        }
    });
}

//]]>
function alerta(msg) {
    Swal.fire({
        title: 'Atenção',
        text: msg, icon: 'warning',
        showCancelButton: false,
        confirmButtonColor: '#3085d6',
        confirmButtonText:
                'OK!'
    });
}


//Get the button:
mybutton = document.getElementById("myBtn");
$('img').attr('loading', 'lazy');
// When the user s crolls down 20px from the top of t he document, show the button


// When the user c licks on the button, scroll to the top of the document
function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

//<![CDATA[
(function (global) {

    if (typeof (global) === "undefined") {
        throw new Error("window is undefined");
    }

    var _hash = "!";
    var noBackPlease = function () {
        global.location.href += "#";
        // making sure we have the fruit available for juice (^__^)
        global.setTimeout(function () {
            global.location.href += "!";
        }, 50);
    };
    global.onhashchange = function () {

        if (global.location.hash !== _hash) {
            global.location.hash = _hash;
            if ($('.modal.in, .modal.show').length > 0) {
                closeModal('mdlPedir');
            } else {
                // backGrupos();
            }


        }
    };
    global.onload = function () {
        noBackPlease();
        // disables backspace on page except on input fields and textarea..
        document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm !== 'textarea')) {
                e.preventDefault();
            }
            // stopping event bubbling up the DOM tree..
            e.stopPropagation();
        };
    }

})(window);
window.addEventListener("load", function () {
    setTimeout(function () {
        // This hides the address bar:
        window.scrollTo(0, 1);
    }, 0);
});
(function () {
    window.__insp = window.__insp || [];
    __insp.push(['wid', 1278828457]);
    var ldinsp = function () {
        if (typeof window.__inspld != "undefined")
            return;
        window.__inspld = 1;
        var insp = document.createElement('script');
        insp.type = 'text/javascript';
        insp.async = true;
        insp.id = "inspsync";
        insp.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://cdn.inspectlet.com/inspectlet.js?wid=1278828457&r=' + Math.floor(new Date().getTime() / 3600000);
        var x = document.getElementsByTagName('script')[0];
        x.parentNode.insertBefore(insp, x);
    };
    setTimeout(ldinsp, 0);
})();




$(window).scroll(function () {
    console.log($(window).scrollTop());
    if ($(window).scrollTop() >= 120) {
        $('.stickyrow').addClass('fixed-header');
        $('.stickyrow div').addClass('visible-title');
    } else {
        $('.stickyrow').removeClass('fixed-header');
        $('.stickyrow div').removeClass('visible-title');
    }
});


$(document).ready(function () {
    var buttonRight = document.getElementById('slideRight');
    var buttonLeft = document.getElementById('slideLeft');
    buttonLeft.style.display = 'none';
    if (!check('contant')) {
        buttonRight.style.display = 'none';
    }
});



function changeNext() {
    $('#contant').animate({scrollLeft: '+=75'}, 300);
//document.getElementById('contant').scrollLeft += 75;
}
;

function changeback() {
    $('#contant').animate({scrollLeft: '-=75'}, 300);
// document.getElementById('contant').scrollLeft -= 75;
}
;
function scrolled(o)
{
    var buttonRight = document.getElementById('slideRight');
    var buttonLeft = document.getElementById('slideLeft');
    //visible height + pixel scrolled = total height

    if (o.scrollLeft === 0) {
        //buttonLeft.style.display = 'none';
        $("#slideLeft").hide(1000);
    } else {
        //buttonLeft.style.display = '';
        $("#slideLeft").show(1000);
    }

    if (o.offsetWidth + o.scrollLeft == o.scrollWidth)
    {
        $("#slideRight").hide(1000);
        //buttonRight.style.display = 'none';
    } else {
        $("#slideRight").show(1000);
        // buttonRight.style.display = '';
    }
}

function isScrollable(el) {

    /*The scrollTop() method sets or returns the  
     vertical scrollbar position for the selected elements*/
    var y1 = el.scrollTop;
    el.scrollTop += 1;
    var y2 = el.scrollTop;
    el.scrollTop -= 1;
    var y3 = el.scrollTop;
    el.scrollTop = y1;
    /*The scrollLeft() method returns the horizontal  
     scrollbar position for the selected elements.*/
    var x1 = el.scrollLeft;
    el.scrollLeft += 1;
    var x2 = el.scrollLeft;
    el.scrollLeft -= 1;
    var x3 = el.scrollLeft;
    el.scrollLeft = x1;
    //returns true or false accordingly 
    return {
        horizontallyScrollable: x1 !== x2 || x2 !== x3,
        verticallyScrollable: y1 !== y2 || y2 !== y3
    }
}

function check(id) {
    /*the data is JSON type, convert it to string and then 
     check the element with given id for scrollbar*/
    var jsonObj = JSON.stringify(isScrollable(document.getElementById(id)));
    var jsonObj2 = JSON.parse(jsonObj);
    return jsonObj2['horizontallyScrollable'];
}

//]]>



