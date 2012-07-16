/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function cambiaColorFila(ObjetoTR, color){
    ObjetoTR.bgColor=color;
}
function cambiaColorFila(ObjetoTR, color){
    ObjetoTR.bgColor=color;
}
function seleccionColumna(Columna){
    seleccionar(Columna.parentNode);
}
function seleccionaTodo(checkBox){
    var tabla = document.getElementById('tablaRegistros');
    var cantidadDeFilas = tabla.rows.length;
    if(checkBox.checked){
        for(i=0; i<cantidadDeFilas;i++){
            tabla.rows[i].cells[0].childNodes[0].checked = true;
        }
    }else{
        for(i=0; i<cantidadDeFilas;i++){
            tabla.rows[i].cells[0].childNodes[0].checked = false;
        }
    }
}
function enviarChequeados(){
    var tabla = document.getElementById('tablaRegistros');
    var cantidadDeFilas = tabla.rows.length;
    for(i=0; i<cantidadDeFilas;i++){
        if(tabla.rows[i].cells[0].childNodes[0].checked){
            seleccionar(tabla.rows[i]);
        }
    }
}

