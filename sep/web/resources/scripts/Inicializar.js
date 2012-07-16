/*
Para los efectos de input y textarea
 */


// jQuery Social Dropdown Menu
// (c) Skyrocket Labs
// http://www.skyrocketlabs.com
// skyrocketlabs@gmail.com
// Created: 02.08.2010
// Last updated: 02.18.2010

//This script has been slightly modified but is essentially David Walsh's jQuery Sliders tutorial
// Download the original script or see a demo at http://davidwalsh.name/jquery-sliders/

function addLoadEvent(func) {
   var oldonload = window.onload;
   if (typeof window.onload != 'function') {
      window.onload = func;
   } else {
      window.onload = function() {
         oldonload();
         func();
      }
   }
}


function limitText(limitField, limitNum,proximofocus) {
   if (limitField.value.length > limitNum) {
      //         alert(document.getElementById(proximofocus))
      limitField.value = limitField.value.substring(0, limitNum);
      document.getElementById(proximofocus).focus();
   //        proximofocus.focus(true);
   }
}
function darFocus(proximofocus){
   document.getElementById(proximofocus).focus();
}

/****************/
function markCalled(id) {
   $(id).innerHTML = "Called";
   $(id).className = "message-success";
}
function init() {
   shortcut.add("Shift+interrogacion",function() {
      dlg2.show();

   },{
      'type':'keydown',
      'propagate':false,
      'disable_in_input':true,
      'target':document,
      'keycode':65
   }
   );
   shortcut.add("Shift+interrogacion2",function() {
      dlg2.show();
        
   },{
      'type':'keydown',
      'propagate':false,
      'disable_in_input':true,
      'target':document,
      'keycode':65
   }
   );
   shortcut.add("Shift+Ctrl+f",function() {
      if(top.document.getElementById('framep').cols=='252,*')
      {
         top.document.getElementById('framep').cols='0%,*';
      }else
      {
         top.document.getElementById('framep').cols='252,*';
      }
   });
   shortcut.add("Shift+Alt+n",function() {
      location.href="Paginas/mvcli001m/Edit2.xhtml";
   });
   shortcut.add("f4",function() {
      location.href="Paginas/mvcli001m/Edit1.xhtml";
   });
   shortcut.add("Shift+f1",function() {
      location.href="Proyecto/qaesc001m/Listar.xhtml";
   });
   shortcut.add("Shift+f10",function() {
      widget_pnl.toggle();
   });
  
}

///Procesando


function prepareInputreadysForHints() {
  
   var inputs = document.getElementsByTagName("input");
   for (var i=0; i<inputs.length; i++){
      // test to see if the hint span exists first

      if (inputs[i].parentNode.getElementsByTagName("span")[0]) {
         // the span exists!  on focus, show the hint
            
         inputs[i].onfocus = function () {
            this.parentNode.getElementsByTagName("span")[0].style.display = "inline";
         }
         // when the cursor moves away from the field, hide the hint
         inputs[i].onblur = function () {
            this.parentNode.getElementsByTagName("span")[0].style.display = "none";
         }
      }
   }
   // repeat the same tests as above for selects
   var selects = document.getElementsByTagName("select");
   for (var k=0; k<selects.length; k++){
      if (selects[k].parentNode.getElementsByTagName("span")[0]) {
         selects[k].onfocus = function () {
            this.parentNode.getElementsByTagName("span")[0].style.display = "inline";
         }
         selects[k].onblur = function () {
            this.parentNode.getElementsByTagName("span")[0].style.display = "none";
         }
      }
   }
}
function corregir_tabla() {
   x=document.getElementsByTagName('table');
   for(i=x.length-1;i>=0;)
   {
      if(x[i].parentNode.className!='a_tabla' && x[i].parentNode.className!='tabla_estandar')
      {
//      alert(x[i].parentNode.className);
         padre=x[i].parentNode;
         tab = document.createElement('div');
         tab.className='a_tabla';
         y=x[i].cloneNode(true);
         padre.appendChild(tab);
         padre.insertBefore(tab,x[i]);
         tab.appendChild(x[i]);
      }
      i--;
   }
    
}


addLoadEvent(init);
addLoadEvent(prepareInputreadysForHints);
addLoadEvent(corregir_tabla);
//ver();
//init();
//prepareInputreadysForHints();

function entradaNumerica(e) {

   tecla = (document.all) ? e.keyCode : e.which;
   //    alert(tecla)
   if (tecla==8||tecla==0||tecla==9)
      return true; //Tecla de retroceso (para poder borrar)
   patron = /\d/ //solo numeros; //ver nota
   te = String.fromCharCode(tecla);
   return patron.test(te);
}
