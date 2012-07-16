package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

 
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "empleadoseliminadosController")
@SessionScoped
public class EmpleadosEliminadosController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Empleado current, selected;

    public Empleado getSelected() {
        return selected;
    }
    private List<Empleado> empleados;

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public void setSelected(Empleado selected) {
        this.selected = selected;
    }
  
    public EmpleadosEliminadosController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("empleadoseliminados").toString().compareTo("nada") != 0) {
            empleados = (List<Empleado>) sesion.getAttribute("empleadoseliminados");
        }





    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        empleados = this.empleadosEliminados();
        sesion.setAttribute("empleadoseliminados", empleados);
        selected = new Empleado();


    }

    public Empleado getCurrent() {
        return current;
    }

    public void setCurrent(Empleado current) {
        this.current = current;
    }

    public String prepareList() {
        current = new Empleado();
        
        return "List";
    }

    public void inicializar() {
        current = new Empleado();
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

    public void habilitarEmpleado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);


        this.reingresarEmpleado(current);

        empleados = this.empleadosEliminados();
        sesion.setAttribute("empleadoseliminados", empleados);
        mostrarMensaje(1,"Exito","Empleado Reingresado");
        current = new Empleado();

    }

    private java.util.List<com.pangea.evap.services.Empleado> empleadosEliminados() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.empleadosEliminados();
    }

    private void reingresarEmpleado(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.reingresarEmpleado(empleado);
    }
}