/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.evap.controller.util;

import com.pangea.evap.services.Dependencia;
import com.pangea.evap.services.Empleado;
import com.pangea.evap.services.Evaluacion;
import com.pangea.evap.services.StringArray;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author Ing. Gerson Ramirez
 */
public class ExcelUtilPromedio {

    private final XSSFWorkbook libro;
    // la hoja de calculo
    private final XSSFSheet hoja;
    // estilo de las celdas del encabezado (con el nombre de las columnas)
    private final XSSFCellStyle estiloTitulo;
    // estilo de las celdas con fórmula
    private final XSSFCellStyle estiloCeldacuadro;
    private String actual;
    private String actualPadre;
    private int totalgeneral;

    public int getTotalgeneral() {
        return totalgeneral;
    }

    public void setTotalgeneral(int totalgeneral) {
        this.totalgeneral = totalgeneral;
    }

    public int getCantidadPadre() {
        return cantidadPadre;
    }

    public void setCantidadPadre(int cantidadPadre) {
        this.cantidadPadre = cantidadPadre;
    }

    public int getPosicionDependenciaPadre() {
        return posicionDependenciaPadre;
    }

    public void setPosicionDependenciaPadre(int posicionDependenciaPadre) {
        this.posicionDependenciaPadre = posicionDependenciaPadre;
    }
    private int cantidad;
    private int cantidadPadre;
    private int cantidadSupervisor;
    private int posicionSupervisor;
    private int posicionDependencia;
    private int posicionDependenciaPadre;
    private Empleado supervisorActual;
    private int cantidadEvaluaciones;

    public int getCantidadEvaluaciones() {
        return cantidadEvaluaciones;
    }

    public void setCantidadEvaluaciones(int cantidadEvaluaciones) {
        this.cantidadEvaluaciones = cantidadEvaluaciones;
    }

    public int getCantidadSupervisor() {
        return cantidadSupervisor;
    }

    public void setCantidadSupervisor(int cantidadSupervisor) {
        this.cantidadSupervisor = cantidadSupervisor;
    }

    public int getPosicionSupervisor() {
        return posicionSupervisor;
    }

    public void setPosicionSupervisor(int posicionSupervisor) {
        this.posicionSupervisor = posicionSupervisor;
    }

    public Empleado getSupervisorActual() {
        return supervisorActual;
    }

    public void setSupervisorActual(Empleado supervisorActual) {
        this.supervisorActual = supervisorActual;
    }

    public int getPosicionDependencia() {
        return posicionDependencia;
    }

    public void setPosicionDependencia(int posicionDependencia) {
        this.posicionDependencia = posicionDependencia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public ExcelUtilPromedio() {
        this.libro = new XSSFWorkbook();
        this.hoja = this.libro.createSheet("Puntaje Empleados");
        this.estiloTitulo = getEstiloTitulo();
        this.estiloCeldacuadro = getEstiloCeldaCuadro();
        anadeFilaEncabezado(4);
        actual = null;
        supervisorActual = null;
        actualPadre = null;
        cantidad = 0;
        cantidadPadre = 0;
        posicionDependencia = 1;
        posicionDependenciaPadre = 2;
        posicionSupervisor = 2;
        cantidadSupervisor = 0;
        totalgeneral = 0;


    }

    public ExcelUtilPromedio(int n) {

        this.libro = new XSSFWorkbook();
        this.hoja = this.libro.createSheet("Puntaje Empleados");
        this.estiloTitulo = getEstiloTitulo();
        this.estiloCeldacuadro = getEstiloCeldaCuadro();
        anadeFilaEncabezado(n);
        actual = null;
        supervisorActual = null;
        actualPadre = null;
        cantidad = 0;
        cantidadPadre = 0;
        posicionDependencia = 1;
        posicionDependenciaPadre = 2;
        posicionSupervisor = 2;
        cantidadSupervisor = 0;
        totalgeneral = 0;
        cantidadEvaluaciones = n;


    }

    public void anadeInfo(String a, String b) {

        final XSSFRow filasevaluacion = getNuevaFila();
        XSSFCell celdax = filasevaluacion.createCell(0);
        celdax.setCellValue(a);
        XSSFCellStyle estilo;
        estilo = estiloCeldacuadro;
        estilo.setAlignment(HorizontalAlignment.LEFT);
        celdax.setCellStyle(estilo);
        celdax = filasevaluacion.createCell(1);
        celdax.setCellValue(b);
        celdax.setCellStyle(estiloCeldacuadro);



    }

    public void datosEvaluacion(List<StringArray> datos) {
        final XSSFRow filasevaluacionx = hoja.getRow(3);
        hoja.addMergedRegion(new CellRangeAddress(3, 3, 8, 10));
        creaCeldaEncabezado(filasevaluacionx, 8, "Tipos de Evaluacion");

        XSSFCell celdaEncabezado = filasevaluacionx.createCell(9);
        celdaEncabezado.setCellValue("");
        XSSFCellStyle estilo = this.getEstiloTitulo();
        estilo.setBorderLeft(BorderStyle.NONE);
        estilo.setBorderRight(BorderStyle.NONE);
        celdaEncabezado.setCellStyle(estilo);

        celdaEncabezado = filasevaluacionx.createCell(10);
        celdaEncabezado.setCellValue("");
        estilo = this.getEstiloTitulo();
        estilo.setBorderLeft(BorderStyle.NONE);
        celdaEncabezado.setCellStyle(estilo);





        final XSSFRow filasevaluacion = hoja.getRow(4);
        int numeroCelda = 3;
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Maximo");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Minimo");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Promedio");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Total Puntos");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Cantidad Evaluaciones");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Evaluaciones");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Vacaciones");
        creaCeldaEncabezado(filasevaluacion, numeroCelda++, "Reposo");

        final XSSFRow filasevaluaciondatos = hoja.getRow(5);

        for (int i = 0; i < 5; i++) {

            if (datos.get(i).getItem() != null) {
                final XSSFCell celdax = filasevaluaciondatos.createCell(i + 3);
                celdax.setCellValue(datos.get(i).getItem().get(0));
                celdax.setCellStyle(estiloCeldacuadro);
            }
        }

        if (datos.size() > 4 && datos.get(5) != null) {
            XSSFCell celdax = filasevaluaciondatos.createCell(8);
            celdax.setCellValue(datos.get(5).getItem().get(0));
            celdax.setCellStyle(estiloCeldacuadro);

            if (datos.size() > 5 && datos.get(6) != null) {

                if (datos.get(6).getItem().get(1).compareTo("Vacaciones") == 0) {
                    celdax = filasevaluaciondatos.createCell(9);
                    celdax.setCellValue(datos.get(6).getItem().get(0));
                    celdax.setCellStyle(estiloCeldacuadro);

                } else {
                    celdax = filasevaluaciondatos.createCell(9);
                    celdax.setCellValue("0");
                    celdax.setCellStyle(estiloCeldacuadro);
                    celdax = filasevaluaciondatos.createCell(10);
                    celdax.setCellValue(datos.get(6).getItem().get(0));
                    celdax.setCellStyle(estiloCeldacuadro);

                }

            }
            if (datos.size() > 6 && datos.get(7) != null) {
                celdax = filasevaluaciondatos.createCell(10);
                celdax.setCellValue(datos.get(7).getItem().get(0));
                celdax.setCellStyle(estiloCeldacuadro);
            }
        }

    }

    private void creaCeldaEncabezado(XSSFRow filaEncabezado, int numeroCelda, String valor) {
        final XSSFCell celdaEncabezado = filaEncabezado.createCell(numeroCelda);
        celdaEncabezado.setCellValue(valor);
        celdaEncabezado.setCellStyle(estiloTitulo);
    }

    private XSSFRow getNuevaFila() {
        return hoja.createRow(hoja.getPhysicalNumberOfRows());
    }

    private XSSFCellStyle getEstiloCeldaCuadro() {
        final XSSFCellStyle cellStyle = libro.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }

    private XSSFCellStyle getEstiloTitulo() {
        final XSSFCellStyle cellStyle = libro.createCellStyle();
        final XSSFFont cellFont = libro.createFont();
        cellFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(cellFont);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    private void anadeFilaEncabezado(int x) {
        final XSSFRow filaEncabezado = getNuevaFila();
        int numeroCelda = 0;
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Gerencia");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Departamento");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Nombres");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Apellidos");
        for (int i = 0; i < x; i++) {
            creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Evaluacion " + (i + 1));
        }

        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Promedio");


    }

    public void generaDocumento(HttpServletResponse res) throws IOException {
        ajustaColumnas();
        ServletOutputStream out = res.getOutputStream();
        libro.write(out);
        out.flush();
        out.close();

    }

    public void generaDocumento2(HttpServletResponse res) throws IOException {
        ajustaColumnas2();
        ServletOutputStream out = res.getOutputStream();
        libro.write(out);
        out.flush();
        out.close();

    }

    public void actualizarTotalDependencia() {
        XSSFRow temp = hoja.getRow(posicionDependencia);
        temp.createCell(2).setCellValue("Total Departamento: " + (cantidad + 1));
        posicionDependencia = hoja.getPhysicalNumberOfRows();
        cantidad = 0;
    }

    public void actualizarTotalSupervisor() {
        XSSFRow temp = hoja.getRow(posicionSupervisor);
        temp.createCell(6).setCellValue("Total Supervisor: " + (cantidadSupervisor + 1));
        posicionSupervisor = hoja.getPhysicalNumberOfRows();
        cantidadSupervisor = 0;
    }

    public void actualizarTotalGerencia() {
        XSSFRow temp = hoja.getRow(posicionDependenciaPadre);
        temp.createCell(1).setCellValue("Total Gerencia: " + (cantidadPadre + 1));
        posicionDependenciaPadre = hoja.getPhysicalNumberOfRows();
        cantidadPadre = 0;
    }

    public void actualizarTotalGeneral() {
        XSSFRow temp = hoja.getRow(1);
        temp.createCell(1).setCellValue("Total General: " + (totalgeneral));
    }

    public void anadeEmpleado(List<String> emp) {

        totalgeneral++;
        boolean cambioDependencia = false;
        if (actualPadre == null) {

            if (emp.get(cantidadEvaluaciones + 1).compareTo("") != 0) {

                actualPadre = emp.get(cantidadEvaluaciones + 1);
                posicionDependenciaPadre = posicionDependencia - 1;

            }

        } else {

            if (emp.get(cantidadEvaluaciones + 1).compareTo("") != 0) {

                if (actualPadre.compareTo(emp.get(cantidadEvaluaciones + 1)) != 0) {

                    final XSSFRow filaDependenciap = getNuevaFila();
                    filaDependenciap.createCell(0).setCellValue(emp.get(cantidadEvaluaciones + 1));
                    actualPadre = emp.get(cantidadEvaluaciones + 1);


                } else {
                    cantidadPadre++;
                }
            }

        }

        if (actual == null) {
            final XSSFRow filaDependencia = getNuevaFila();
            filaDependencia.createCell(1).setCellValue(emp.get(cantidadEvaluaciones + 2));
            actual = emp.get(cantidadEvaluaciones + 2);



        } else {
            if (actual.compareTo(emp.get(cantidadEvaluaciones + 2)) != 0) {
                final XSSFRow filaDependencia = getNuevaFila();
                filaDependencia.createCell(1).setCellValue(emp.get(cantidadEvaluaciones + 2));

                actual = emp.get(cantidadEvaluaciones + 2);
                cambioDependencia = true;
            } else {
                cantidad++;
            }
        }


        final XSSFRow filaEmpleado = getNuevaFila();
        filaEmpleado.createCell(2).setCellValue(emp.get(cantidadEvaluaciones + 3));
        filaEmpleado.createCell(3).setCellValue(emp.get(cantidadEvaluaciones + 4));

        for (int i = 0; i < cantidadEvaluaciones; i++) {

            filaEmpleado.createCell(4 + i).setCellValue(emp.get(i));

        }
        filaEmpleado.createCell(cantidadEvaluaciones + 4).setCellValue(emp.get(cantidadEvaluaciones));





    }

    private void ajustaColumnas() {
        final short numeroColumnas = hoja.getRow(0).getLastCellNum();
        for (int i = 0; i < numeroColumnas; i++) {
            hoja.autoSizeColumn(i);
        }
    }

    private void ajustaColumnas2() {
        final short numeroColumnas = hoja.getRow(4).getLastCellNum();
        for (int i = 0; i < numeroColumnas; i++) {
            hoja.autoSizeColumn(i);
        }
    }
}
