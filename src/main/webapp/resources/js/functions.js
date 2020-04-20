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



function openModal(id) {
    $('#' + id).modal()
}
function closeModal(id) {
    $('#' + id).modal('hide');
}


function showTooltip() {
    alertify.set('notifier', 'position', 'bottom-center');
    alertify.success('<span style="color: white;"><i class="fa fa-plus"></i> Item Adicionado</span>');

}
function connectionError() {
    alertify.set('notifier', 'position', 'bottom-center');
    alertify.error('<span style="color: white;"><i class="fa fa-plus"></i> Erro de Conexão com o servidor</span>');

}
function connectionErrorMsg(msg) {
    alertify.set('notifier', 'position', 'bottom-center');
    alertify.error('<span style="color: white;"><i class="fa fa-plus"></i>' + msg + ' </span>');

}

function effect(iditemimg) {
    var cart = $('.cart');
    var cartIcon = $('.cart a svg');
    var imgtodrag = $("." + iditemimg).find("img");

    if (imgtodrag) {
        var imgclone = imgtodrag.clone()
                .offset({
                    top: imgtodrag.offset().top,
                    left: imgtodrag.offset().left
                })
                .css({
                    'opacity': '0.5',
                    'position': 'absolute',
                    'height': '50px',
                    'width': '50px',
                    'z-index': '100'
                })
                .appendTo($('body'))
                .animate({
                    'top': cart.offset().top + 10,
                    'left': cart.offset().left + 10,
                    'width': 55,
                    'height': 55
                }, 1000, 'easeInOutExpo');

        setTimeout(function () {
            cartIcon.effect("bounce", {
                times: 2
            }, 200);
        }, 1500);

        imgclone.animate({
            'width': 0,
            'height': 0
        }, function () {
            $(this).detach()
        });
    }
}


function imgError(path) {
    $('img').each(function () {
        if ((typeof this.naturalWidth != "undefined" &&
                this.naturalWidth == 0)
                || this.readyState == 'uninitialized') {
            $(this).attr('src', path);
        }
    });
}




