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
public class ExcelUtil {

    private final XSSFWorkbook libro;
    // la hoja de calculo
    private final XSSFSheet hoja;
    // estilo de las celdas del encabezado (con el nombre de las columnas)
    private final XSSFCellStyle estiloTitulo;
    // estilo de las celdas con fórmula
    private final XSSFCellStyle estiloCeldacuadro;
    private Dependencia actual;
    private Dependencia actualPadre;
    private int totalgeneral;

    public int getTotalgeneral() {
        return totalgeneral;
    }

    public void setTotalgeneral(int totalgeneral) {
        this.totalgeneral = totalgeneral;
    }

    public Dependencia getActualPadre() {
        return actualPadre;
    }

    public void setActualPadre(Dependencia actualPadre) {
        this.actualPadre = actualPadre;
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

    public Dependencia getActual() {
        return actual;
    }

    public void setActual(Dependencia actual) {
        this.actual = actual;
    }

    public ExcelUtil() {
        this.libro = new XSSFWorkbook();
        this.hoja = this.libro.createSheet("Detalle No Evaluados");
        this.estiloTitulo = getEstiloTitulo();
        this.estiloCeldacuadro = getEstiloCeldaCuadro();
        anadeFilaEncabezado();
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

    public ExcelUtil(String nombre) {
        this.libro = new XSSFWorkbook();
        this.hoja = this.libro.createSheet(nombre);
        this.estiloTitulo = getEstiloTitulo();
        this.estiloCeldacuadro = getEstiloCeldaCuadro();
        final XSSFRow filasevaluacion = getNuevaFila();
        hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        XSSFCell celdax = filasevaluacion.createCell(0);
        celdax.setCellValue("Datos Globales Evaluacion");
        celdax.setCellStyle(estiloTitulo);
        XSSFCell celdaEncabezado = filasevaluacion.createCell(1);
        celdaEncabezado.setCellValue("");
        XSSFCellStyle estilo = this.getEstiloTitulo();
        estilo.setBorderRight(BorderStyle.NONE);
        celdaEncabezado.setCellStyle(estilo);




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

        if (datos.size() > 4 && datos.get(5)!=null) {
            XSSFCell celdax = filasevaluaciondatos.createCell(8);
            celdax.setCellValue(datos.get(5).getItem().get(0));
            celdax.setCellStyle(estiloCeldacuadro);

            if (datos.size() > 5 && datos.get(6)!=null) {

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
            if (datos.size() > 6 && datos.get(7)!=null) {
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

    private void anadeFilaEncabezado() {
        final XSSFRow filaEncabezado = getNuevaFila();
        int numeroCelda = 0;
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Gerencia");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Departamento");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Supervisor");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Codigo");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Cedula");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Correo Supervisor");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Empleado");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Codigo");
        creaCeldaEncabezado(filaEncabezado, numeroCelda++, "Cedula");

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

    public void anadeEmpleado(Empleado emp, Empleado sup) {

        totalgeneral++;
        boolean cambioDependencia = false;
        if (actualPadre == null) {

            if (emp.getIdDependencia().getIdDependenciaPadre() != null) {
                if (emp.getIdDependencia().getIdDependenciaPadre().getIdDependenciaPadre() != null) {
                    if (!emp.getIdDependencia().getIdDependenciaPadre().getIdDependenciaPadre().equals(actual)) {
                        posicionDependenciaPadre = hoja.getPhysicalNumberOfRows();
                        final XSSFRow filaDependenciap = getNuevaFila();
                        filaDependenciap.createCell(0).setCellValue(emp.getIdDependencia().getIdDependenciaPadre().getNombre());
                        actualPadre = emp.getIdDependencia().getIdDependenciaPadre();
                    } else {
                        actualPadre = emp.getIdDependencia().getIdDependenciaPadre();
                        cantidadPadre += cantidad;
                        posicionDependenciaPadre = posicionDependencia - 1;

                    }
                }
            }

        } else {

            if (emp.getIdDependencia().getIdDependenciaPadre() != null) {

                if (actualPadre.getNombre().compareTo(emp.getIdDependencia().getIdDependenciaPadre().getNombre()) != 0) {

                    if (emp.getIdDependencia().getIdDependenciaPadre().getIdDependenciaPadre() != null) {


                        actualizarTotalGerencia();
                        final XSSFRow filaDependenciap = getNuevaFila();
                        filaDependenciap.createCell(0).setCellValue(emp.getIdDependencia().getIdDependenciaPadre().getNombre());
                        actualPadre = emp.getIdDependencia().getIdDependenciaPadre();

                    }
                } else {
                    cantidadPadre++;
                }
            }

        }

        if (actual == null) {
            final XSSFRow filaDependencia = getNuevaFila();
            filaDependencia.createCell(0).setCellValue(emp.getIdDependencia().getNombre());
            actual = emp.getIdDependencia();



        } else {
            if (actual.getNombre().compareTo(emp.getIdDependencia().getNombre()) != 0) {

                actualizarTotalDependencia();
                final XSSFRow filaDependencia = getNuevaFila();
                filaDependencia.createCell(0).setCellValue(emp.getIdDependencia().getNombre());

                actual = emp.getIdDependencia();
                cambioDependencia = true;
            } else {
                cantidad++;
            }
        }

        if (supervisorActual == null) {

            final XSSFRow filasupervisor = getNuevaFila();

            filasupervisor.createCell(2).setCellValue(sup.getNombres() + " " + sup.getApellidos());
            filasupervisor.createCell(3).setCellValue(sup.getCodigo());
            filasupervisor.createCell(4).setCellValue(sup.getNroIdentificacion().toString());
            filasupervisor.createCell(5).setCellValue(sup.getCorreo());

            supervisorActual = sup;

        } else {
            if (supervisorActual.getNombres().compareTo(sup.getNombres()) != 0 || cambioDependencia) {

                actualizarTotalSupervisor();
                final XSSFRow filasupervisor = getNuevaFila();
                filasupervisor.createCell(2).setCellValue(sup.getNombres() + " " + sup.getApellidos());
                filasupervisor.createCell(3).setCellValue(sup.getCodigo());
                filasupervisor.createCell(4).setCellValue(sup.getNroIdentificacion().toString());
                filasupervisor.createCell(5).setCellValue(sup.getCorreo());

                supervisorActual = sup;

            } else {
                cantidadSupervisor++;
            }
        }



        final XSSFRow filaEmpleado = getNuevaFila();
        filaEmpleado.createCell(6).setCellValue(emp.getNombres() + " " + emp.getApellidos());
        filaEmpleado.createCell(7).setCellValue(emp.getCodigo());
        filaEmpleado.createCell(8).setCellValue(emp.getNroIdentificacion().toString());





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
