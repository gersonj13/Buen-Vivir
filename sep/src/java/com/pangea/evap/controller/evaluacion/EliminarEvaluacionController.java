package com.pangea.evap.controller.evaluacion;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;

// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "eliminarevaluacionController")
@SessionScoped
public class EliminarEvaluacionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private EvaluacionEmpleado current, selected;
    private List<EvaluacionEmpleado> evaluacionese;
    private Empleado empleadosel;

    public Empleado getEmpleadosel() {
        return empleadosel;
    }

    public void setEmpleadosel(Empleado empleadosel) {
        this.empleadosel = empleadosel;
    }

    public EvaluacionEmpleado getCurrent() {
        return current;
    }

    public void setCurrent(EvaluacionEmpleado current) {
        this.current = current;
    }

    public EvaluacionEmpleado getSelected() {
        return selected;
    }

    public void setSelected(EvaluacionEmpleado selected) {
        this.selected = selected;
    }

    public List<EvaluacionEmpleado> getEvaluacionese() {
        return evaluacionese;
    }

    public void setEvaluacionese(List<EvaluacionEmpleado> evaluacionese) {
        this.evaluacionese = evaluacionese;
    }

    public EliminarEvaluacionController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        empleadosel = (Empleado) sesion.getAttribute("empleadoevaluacions");

        if (sesion.getAttribute("evaluacionesempleado").toString().compareTo("nada") != 0) {
            evaluacionese = (List<EvaluacionEmpleado>) sesion.getAttribute("evaluacionesempleado");
        }




    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        empleadosel = (Empleado) sesion.getAttribute("empleadoevaluacions");

        evaluacionese = this.retornaEvaluacionesEmpleado(empleadosel);

        sesion.setAttribute("evaluacionesempleado", evaluacionese);



    }

    public void prepareEmpleados() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");

        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/listaEmpleadosd.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminaEvaluacionEmpleado(current);
        mostrarMensaje(0, "Exito", "Evaluacion Eliminada");
        evaluacionese = this.retornaEvaluacionesEmpleado(empleadosel);

        sesion.setAttribute("evaluacionesempleado", evaluacionese);

        return "";
    }

    public String prepareEdit() {
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
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

    private java.util.List<com.pangea.evap.services.EvaluacionEmpleado> retornaEvaluacionesEmpleado(com.pangea.evap.services.Empleado empleadox) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEvaluacionesEmpleado(empleadox);
    }

    private void eliminaEvaluacionEmpleado(com.pangea.evap.services.EvaluacionEmpleado evaluacionx) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminaEvaluacionEmpleado(evaluacionx);
    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtronx, java.lang.String filtroanx, java.lang.String filtrocx) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtronx, filtroanx, filtrocx);
    }
}