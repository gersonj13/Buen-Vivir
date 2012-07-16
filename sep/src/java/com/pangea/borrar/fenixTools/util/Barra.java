/**
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Pasteurizadora Tachira CA</p>
 * @author Desconocido
 * @version
 */
package com.pangea.borrar.fenixTools.util;


//import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class Barra{
    
    String tSql = "";
    String tSql2 = "";

    public boolean[] metodo(String padre, String idusu, Connection _con) {
        boolean barra[]={false,false,false,false};        
        
        Statement stm = null;
        ResultSet tb = null;
        Statement stmPermi = null;
        ResultSet tbPermi = null;

        try {
            stm = _con.createStatement();            
            stmPermi = _con.createStatement();

            tSql = new StringBuffer(
                    "select nnodo, tipo from sgapl002v where padre = '")
                    .append(padre)
                    .append("' and tipo in (4,6,8,10) order by tipo")
                    .toString();
            tb = stm.executeQuery(tSql);
            /**
             * tipo = 4 se refiere a Insertar y busco si esta habilitado para el perfil
             * tipo = 5 no existe la opcion insertar en el menu
             * tipo = 6 se refiere a Consultar y busco si esta habilitado para el perfil
             * tipo = 7 no existe la opcion Consultar en el menu
             * tipo = 8 se refiere a Modificar y busco si esta habilitado para el perfil
             * tipo = 9 no existe la opcion Modificar en el menu
             * tipo = 10 se refiere a Eliminar y busco si esta habilitado para el perfil
             * tipo = 11 no existe la opcion Eliminar en el menu
             */
            try {
                while (tb.next()) {
                    //Esta habilitado para q le muestren la ventana?
                    tSql2 = new StringBuffer(
                            "select habil from sgper002v, sgusu001v ").append(
                            "where sgusu001v.idgrp = sgper002v.idgrp AND ").append("sgusu001v.idusu = '")
                            .append(idusu).append("' AND nnodo = '").append(tb.getString("nnodo"))
                            .append("'").toString();
                    tbPermi = stmPermi.executeQuery(tSql2);
                    if (tbPermi.next()) {
                        if(tbPermi.getString("habil").equals("1")){
                            switch (tb.getInt("tipo")) {
                                case 4:
                                    barra[0] = true; //Insertar
                                    break;
                                case 6:
                                    barra[1] = true; //Consultar
                                    break;
                                case 8:
                                    barra[2] = true; //Modificar
                                    break;
                                case 10:
                                    barra[3] = true; //Eliminar
                                    break;
                             }//fin switch
                        }
                    }
                }
            } catch (Exception ex1) {
            }
        }
        catch (Exception e2) {
        }
        return barra;
    }
}

    