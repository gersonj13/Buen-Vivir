package com.pangea.evap.controller.reportes;

import com.fenix.util.jbVarios;
import com.pangea.evap.controller.util.ExcelUtil;
import com.pangea.evap.controller.util.ExcelUtilPromedio;
import com.pangea.evap.services.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

 
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "reporteEvaluacionesController")
@SessionScoped
public class ReporteEvaluacionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Evaluacion current;
    private List<Evaluacion> evaluaciones;
    private List<Dependencia> dependencias;
    private Date Fechai, Fechaf;
    private Date Fechaie, Fechafe;

    public Date getFechafe() {
        return Fechafe;
    }

    public void setFechafe(Date Fechafe) {
        this.Fechafe = Fechafe;
    }

    public Date getFechaie() {
        return Fechaie;
    }

    public void setFechaie(Date Fechaie) {
        this.Fechaie = Fechaie;
    }

    public Date getFechaf() {
        return Fechaf;
    }

    public void setFechaf(Date Fechaf) {
        this.Fechaf = Fechaf;
    }

    public Date getFechai() {
        return Fechai;
    }

    public void setFechai(Date Fechai) {
        this.Fechai = Fechai;
    }

    public List<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }

    public Evaluacion getCurrent() {
        return current;
    }

    public void setCurrent(Evaluacion current) {
        this.current = current;
    }

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public ReporteEvaluacionController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("reportevaluaciones").toString().compareTo("nada") != 0) {
            evaluaciones = (List<Evaluacion>) sesion.getAttribute("reportevaluaciones");


        }


    }

    public void ProcesarTotales() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletResponse res = (HttpServletResponse) ec.getResponse();


        res.setContentType("application/vnd.ms-excel");
        jbVarios jvar = new jbVarios();
        String fecha = jvar.Hoy("dd-MM-yyyy_hh-mm");
        res.setHeader("Content-disposition", "attachment; filename=TotalEva_" + current.getNombreEvaluacion() + fecha + ".xlsx");
        ExcelUtil excel = new ExcelUtil("Totales Evaluacion");
        excel.anadeInfo("", "");
        excel.anadeInfo("Nombre Evaluacion:", current.getNombreEvaluacion());
        excel.anadeInfo("Periodo de Aplicacion", String.valueOf(current.getFechaInicio().getDay()) + "-" + String.valueOf(current.getFechaInicio().getMonth()) + "-" + String.valueOf(current.getFechaInicio().getYear()));
        excel.anadeInfo("", String.valueOf(current.getFechaFin().getDay()) + "-" + String.valueOf(current.getFechaFin().getMonth()) + "-" + String.valueOf(current.getFechaFin().getYear()));
        excel.anadeInfo("Formato Utilizado:", current.getIdFormatoEvaluacion().getNombre());
        excel.anadeInfo("Peso Evaluacion:", current.getPeso().toString());
        excel.anadeInfo("", "");


        List<StringArray> datos;
        datos = this.reporteInformacionEvaluacion(current.getId().toString());




        excel.datosEvaluacion(datos);

        excel.generaDocumento2(res);

        fc.responseComplete();

    }

    public void mostrarMensaje(int _opcMensaje, String _cabeceraMensaje, String _cuerpomensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        switch (_opcMensaje) {
            case 0: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 1: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 2: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
            case 3: {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, _cabeceraMensaje, _cuerpomensaje));
                break;
            }
        }
    }

    public void ProcesarGeneral() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletResponse res = (HttpServletResponse) ec.getResponse();

        List<EmpleadoArray> empleados = this.reporteEmpleadosno(current.getId().toString());

        if (!empleados.isEmpty()) {

            res.setContentType("application/vnd.ms-excel");
            jbVarios jvar = new jbVarios();
            String fecha = jvar.Hoy("dd-MM-yyyy_hh-mm");
            res.setHeader("Content-disposition", "attachment; filename=" + current.getNombreEvaluacion() + fecha + ".xlsx");
            ExcelUtil excel = new ExcelUtil();


            for (int i = 0; i < empleados.size(); i++) {
                EmpleadoArray item = empleados.get(i);

                List<Empleado> empsup = item.getItem();

                excel.anadeEmpleado(empsup.get(0), empsup.get(1));

            }
            excel.actualizarTotalDependencia();
            excel.actualizarTotalSupervisor();
            excel.actualizarTotalGeneral();
            excel.actualizarTotalGerencia();
            excel.generaDocumento(res);

            fc.responseComplete();

        }
        else{
            mostrarMensaje(1,"Aviso","Todos los empleados han sido evaluados");
        }
    }

    public void ProcesarPromedios() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpServletResponse res = (HttpServletResponse) ec.getResponse();
        res.setContentType("application/vnd.ms-excel");


        jbVarios jvar = new jbVarios();
        String fecha = jvar.Hoy("dd-MM-yyyy_hh-mm");
        res.setHeader("Content-disposition", "attachment; filename=" + "Puntajes Empleados" + fecha + ".xlsx");

        List<StringArray> historico = this.reporteHistoricoEmpleados();
        ExcelUtilPromedio excel = new ExcelUtilPromedio(historico.get(0).getItem().size() - 5);


        for (int i = 0; i < historico.size(); i++) {
            StringArray item = historico.get(i);

            List<String> emp = item.getItem();

            excel.anadeEmpleado(emp);

        }

        excel.generaDocumento(res);

        fc.responseComplete();


    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        evaluaciones = this.retornaEvaluacionesReporte();


        sesion.setAttribute("reportevaluaciones", evaluaciones);

        Fechai = new Date();
        Fechaf = new Date();


    }

    public String prepareList() {
        current = new Evaluacion();
        
        return "List";
    }

    public void inicializar() {
        current = new Evaluacion();
    }

    public void TransformarFechas1(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechaie = xcal.toGregorianCalendar().getTime();

    }

    public void TransformarFechas2(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechafe = xcal.toGregorianCalendar().getTime();

    }

    private java.util.List<com.pangea.evap.services.EmpleadoArray> reporteEmpleadosno(java.lang.String identificadorEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.reporteEmpleadosno(identificadorEvaluacion);
    }

    private java.util.List<com.pangea.evap.services.StringArray> reporteInformacionEvaluacion(java.lang.String identificadorEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.reporteInformacionEvaluacion(identificadorEvaluacion);
    }

    private java.util.List<com.pangea.evap.services.Evaluacion> retornaEvaluacionesReporte() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEvaluacionesReporte();
    }

    private java.util.List<com.pangea.evap.services.StringArray> reporteHistoricoEmpleados() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.reporteHistoricoEmpleados();
    }
}
