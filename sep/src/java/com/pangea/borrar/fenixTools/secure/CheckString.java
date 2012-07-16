/**
 * <p>Title: Examinar cadenas segun expresiones regulares </p>
 * <p>Description: Clase creada en la capa controlador del proyecto, con el fin de examinar
 * la existencia de palabras o condiciones especificas en una cadena</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Pasteurizadora Tachira CA</p>
 * @author icardenas
 * @version 0.1v 01-12-2010
 */
package com.pangea.borrar.fenixTools.secure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckString {

    public static final int VERIFICAR_MAYUSCULAS = 0;
    public static final int VERIFICAR_NUMEROS = 1;
    public static final int VERIFICAR_ESPECIALES = 2;

    public CheckString() {
    }

    /**
     * Metodo que permite verificar que una cadena este entre un rango de longitud.
     * @param _rango valor minimo y maximo requeridos para la cadena.
     * @param _cadenaEvaluar Cadena que se le va evaluar la cantidad de caracteres.
     * @return boolean, Retorna verdadero si la cantidad de caracteres de _cadenaEvaluar se encuentra entre el rango.
     */
    public static boolean examinarLongitud(int _rango[], String _cadenaEvaluar) {
        Pattern regla = Pattern.compile(
                new StringBuffer(".{").append(_rango[0]).append(",").append(_rango[1]).append("}").toString());

        Matcher m = regla.matcher(_cadenaEvaluar);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * Metodo que permite verificar subcadenas en una cadena, en la cadena o al inicio de la misma
     * distinguiendo entre mayusculas y minusculas.
     * @param _cadena que contiene la subcadena (tambien podria ser una expresion regular).
     * @param  _cadenaEvaluar Cadena que se le va evaluar.
     * @param _minCad decide si debo considerar la mayusula para la comparacion, true para comparar sin importar mayusculas.
     * @param _comience (true) indica si tengo que buscar la palabra solo al inicio de la cadena, false no importa el lugar.
     * @return boolean, Retorna verdadero si se ha encontrado la palabra en una cadena.
     */
    public static  boolean examinarCadena(String _cadena, String _cadenaEvaluar, boolean _minCad, boolean _comience) {
        if (_minCad) {
            _cadena = _cadena.toLowerCase();
            _cadenaEvaluar = _cadenaEvaluar.toLowerCase();
        }
        if (_comience) {
            _cadena = "^" + _cadena;
        }


        Pattern regla = Pattern.compile(_cadena);
        Matcher m = regla.matcher(_cadenaEvaluar);
        if (m.find()) {
            return true;
        }

        return false;
    }

    /**
     * Metodo que permite examinar que una cadena cumpla con reglas numerica,mayuscula,
     * caracteres especiales. , : + - * / @ _.
     * @param _cadenaEvaluar Cadena que se le va evaluar.
     * @param _can Cantidad minima de caracteres que debe tener la cadena por tipo de opcion.
     * @param _opc (opcion) me indica si la verificacion sera de mayuscula=0, numerica=1, especiales=2
     * @return boolean, Retorna verdadero si la cantidad de caracteres indicados se cumple segun la opcion.
     */
    public static  boolean examinarRegla(String _cadenaEvaluar, int _can, int _opc) {

        switch (_opc) {
            case VERIFICAR_MAYUSCULAS: {
                Pattern regla = Pattern.compile(
                        new StringBuffer("([A-Z].*?){").append(_can).append("}").toString());
                Matcher m = regla.matcher(_cadenaEvaluar);

                if (m.find()) {
                    return true;
                }
                break;
            }
            case VERIFICAR_NUMEROS: {
                Pattern regla = Pattern.compile(
                        new StringBuffer("([0-9].*?){").append(_can).append("}").toString());
                Matcher m = regla.matcher(_cadenaEvaluar);

                if (m.find()) {
                    return true;
                }
                break;
            }
            case VERIFICAR_ESPECIALES: {
                Pattern regla = Pattern.compile(
                        new StringBuffer("((\\,|\\:|\\.|\\@|\\+|\\-|\\*|\\/|\\_).*?){").append(_can).append("}").toString());
                Matcher m = regla.matcher(_cadenaEvaluar);

                if (m.find()) {
                    return true;
                }
                break;
            }
            default:
                break;

        }
        return false;
    }

    /**
     * Metodo que desconcatena una cadena separadas por comas y se encarga de verificar mediante el metodo examinarCadenas
     * @param _array que contiene la subcadenas a verificar la existencia.
     * @param _cadenaEvaluar Cadena que se le va evaluar.
     * @param _minCad decide si debo considerar la mayusula para la comparacion (true) palabras iguales sin importar mayusculas.
     * @param _comience (true) indica si tengo que buscar la palabra solo al inicio de la cadena, false no importa el lugar.
     * @return boolean, Retorna verdadero si se ha encontrado la subcadena en una cadena.
     */
    public static boolean examinarArray(String _array, String _cadenaEvaluar, boolean _minCad, boolean _comience) {
        String[] arr = _array.split(",");
        for (int i = 0; i < arr.length; i++) {
            if (examinarCadena(arr[i], _cadenaEvaluar, _minCad, _comience)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que se encarga de examinar que una cadena tenga caracteristicas como largo mayuscula, minuscula, numeros y caracteres especiales
     * @param _cadenaEvaluar Cadena que se le va evaluar.
     * @param _cant array de enteros donde {Cantidad Minima Caracteres,Cantidad Minima Mayusculas,Cantidad Minima Minusculas,
     * Cantidad Minima numeros, Cantidad Minima de especiales (. , : + - * / @ _.)}
     * @return boolean, Retorna verdadero si se cumplen todas las condiciones
     */
    public static  boolean examinarExpresion(String _cadenaEvaluar, int[] _cant) {

        Pattern regla = Pattern.compile(
                new StringBuffer("(?=.{").append(_cant[0]).append(",})((?=(.*\\d){")
                .append(_cant[3]).append("})(?=(.*(\\,|\\.|\\:|\\@|\\+|\\-|\\*|\\/|\\_)){").append(_cant[4])
                .append("}))(?![.\\n])(?=(.*[A-Z]){")
                .append(_cant[1])
                .append("})(?=(.*[a-z]){").append(_cant[2])
                .append("}).*$").toString());

        Matcher m = regla.matcher(_cadenaEvaluar);
        if (m.find()) {
            return true;
        }
        
        return false;
    }
}
