
package com.pangea.evap.controller.gerentes;

import com.pangea.evap.services.*;
import java.io.IOException;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

 
// @Author Ing. Gerson Ramirez
@ManagedBean(name = "evaluaciongController")
@SessionScoped
public class EvaluaciongController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Evaluacion current, selected;
    private List<Evaluacion> evaluaciones;
    private List<EmpleadoTO> listaEmpleados;

    public List<EmpleadoTO> getListaEmpleados() {
        return listaEmpleados;
    }

    public void setListaEmpleados(List<EmpleadoTO> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
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

    public Evaluacion getSelected() {
        return selected;
    }

    public void setSelected(Evaluacion selected) {
        this.selected = selected;
    }

   
    public EvaluaciongController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("evaluacionesg").toString().compareTo("nada") != 0) {
            evaluaciones = (List<Evaluacion>) sesion.getAttribute("evaluacionesg");
        }

    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        evaluaciones = this.retornaEvaluaciones();
        sesion.setAttribute("evaluaciones", evaluaciones);
        selected = new Evaluacion();
        selected.setIdFormatoEvaluacion(new FormatoEvaluacion());
        selected.setIdDependencia(new Dependencia());


    }

    public void prepareEmpleados() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("formatoEvaluaciong", current);
        listaEmpleados = getEmpleadosEvaluaciong(sesion.getAttribute("codSupervisor").toString(), String.valueOf(current.getId()));
        sesion.removeAttribute("empleadosg");
        sesion.setAttribute("empleadosg", listaEmpleados);




        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/gerentes/empleadosg.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public String prepareList() {
        current = new Evaluacion();
        
        return "List";
    }

    public void inicializar() {
        current = new Evaluacion();
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

    private java.util.List<com.pangea.evap.services.Evaluacion> retornaEvaluaciones() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEvaluaciones();
    }

    private java.util.List<com.pangea.evap.services.EmpleadoTO> getEmpleadosEvaluaciong(java.lang.String idEmpleado, java.lang.String idEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getEmpleadosEvaluaciong(idEmpleado, idEvaluacion);
    }

   
}


