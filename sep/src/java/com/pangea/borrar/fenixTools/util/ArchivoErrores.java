package com.pangea.borrar.fenixTools.util;

/*
import java.io.*;
import java.text.*;
import java.util.*;

public class ArchivoErrores {
    public boolean Grabar(String usuario, String ruta, String descrip) {
        try {

            Date fecha = new Date(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            File file = new File("msg/errores/"+ df.format(fecha) + ".jsp");

System.out.println(file.getParentFile().toString());

            if (!file.getParentFile().isDirectory()) {
                file.getParentFile().mkdirs();
            }

            df = new SimpleDateFormat("dd-MM-yyyy");

            if (ruta.length() > 50) {
                ruta = ruta.substring(0, 50) + "<BR>" +
                       ruta.substring(51, ruta.length());
            }

            int size = (int) file.length();
            if (size == 0) {
                FileWriter out = new FileWriter(file, true);
                out.write(
                        "<%@taglib uri='/WEB-INF/struts-html.tld' prefix='html'%>\n" +
                        "<link href='<html:rewrite page='/estilo.css' />' rel='stylesheet' type='text/css'>\n" +
                        "<html:html>\n" +
                        "<head>\n" +
                        "<title>Errores</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<p class='titulo' align='center'>Errores " +
                        df.format(fecha) + "</p>\n" +
                        "<table width='100%' border='1' id='gris' bordercolor='#EFEFEF'>\n" +
                        "<tr class='fuentenegrita' >\n" +
                        "<td width='20%' height='18'>Fecha</td>\n" +
                        "<td width='10%'>Usuario</td>\n" +
                        "<td width='20%'>Ruta</td>\n" +
                        "<td width='50%'>Descripción</td>\n" +
                        "</tr>\n");

                df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                out.write("<tr onmouseover = \"this.bgColor = '#C5D8EB';\" onmouseout = \"this.bgColor= '#FFFFFF';\">\n" +
                          "<td>" + df.format(fecha) + "</td>\n" +
                          "<td>" + usuario + "</td>\n" +
                          "<td>" + ruta + "</td>\n" +
                          "<td>" + descrip + "</td>\n" +
                          "</tr>\n" +
                          "</table>\n" +
                          "</body>\n" +
                          "</html:html>\n");
                out.close();
            } else {

                RandomAccessFile out = new RandomAccessFile(file, "rw");

                // Seek to end of file
                out.seek(file.length() - 30);

                df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                out.writeBytes("<tr onmouseover = \"this.bgColor = '#C5D8EB';\" onmouseout = \"this.bgColor= '#FFFFFF';\">\n" +
                               "<td>" + df.format(fecha) + "</td>\n" +
                               "<td>" + usuario + "</td>\n" +
                               "<td>" + ruta + "</td>\n" +
                               "<td>" + descrip + "</td>\n" +
                               "</tr>\n" +
                               "</table>\n" +
                               "</body>\n" +
                               "</html:html>\n");

                out.close();
            }
            System.out.println("[" + df.format(fecha) + "][" + usuario + "][" +
                               ruta + "]: " + descrip);
            return true;

        } catch (Exception ex) {
            System.out.println(
                    "com.fenix.util.ArchivosErrores [Grabar] " +
                    ex.toString());
            return false;
        }

    }

    public boolean GrabarSql(String usuario, String ruta, String descrip) {
        try {

            Date fecha = new Date(System.currentTimeMillis());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            File file = new File("../wmconasi/sql/sqllog_" +
                                 df.format(fecha) + ".jsp");

            df = new SimpleDateFormat("dd-MM-yyyy");

            if (ruta.length() > 50) {
                ruta = ruta.substring(0, 50) + "<BR>" +
                       ruta.substring(51, ruta.length());
            }

            int size = (int) file.length();
            if (size == 0) {
                FileWriter out = new FileWriter(file, true);
                out.write(
                        "<%@taglib uri='/WEB-INF/struts-html.tld' prefix='html'%>\n" +
                        "<link href='<html:rewrite page='/estilo.css' />' rel='stylesheet' type='text/css'>\n" +
                        "<html:html>\n" +
                        "<head>\n" +
                        "<title>Errores</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<p class='titulo' align='center'>Errores " +
                        df.format(fecha) + "</p>\n" +
                        "<table width='100%' border='1' id='gris' bordercolor='#EFEFEF'>\n" +
                        "<tr class='fuentenegrita' >\n" +
                        "<td width='20%' height='18'>Fecha</td>\n" +
                        "<td width='10%'>Usuario</td>\n" +
                        "<td width='20%'>Ruta</td>\n" +
                        "<td width='50%'>Descripción</td>\n" +
                        "</tr>\n");

                df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                out.write("<tr onmouseover = \"this.bgColor = '#C5D8EB';\" onmouseout = \"this.bgColor= '#FFFFFF';\">\n" +
                          "<td>" + df.format(fecha) + "</td>\n" +
                          "<td>" + usuario + "</td>\n" +
                          "<td>" + ruta + "</td>\n" +
                          "<td>" + descrip + "</td>\n" +
                          "</tr>\n" +
                          "</table>\n" +
                          "</body>\n" +
                          "</html:html>\n");
                out.close();
            } else {

                RandomAccessFile out = new RandomAccessFile(file, "rw");

                // Seek to end of file
                out.seek(file.length() - 30);

                df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                out.writeBytes("<tr onmouseover = \"this.bgColor = '#C5D8EB';\" onmouseout = \"this.bgColor= '#FFFFFF';\">\n" +
                               "<td>" + df.format(fecha) + "</td>\n" +
                               "<td>" + usuario + "</td>\n" +
                               "<td>" + ruta + "</td>\n" +
                               "<td>" + descrip + "</td>\n" +
                               "</tr>\n" +
                               "</table>\n" +
                               "</body>\n" +
                               "</html:html>\n");

                out.close();
            }
            System.out.println("[" + df.format(fecha) + "][" + usuario + "][" +
                               ruta + "]: " + descrip);
            return true;

        } catch (Exception ex) {
            System.out.println(
                    "com.fenix.util.ArchivosErrores [GrabarSql] " +
                    ex.toString());
            return false;
        }

    }
}*/