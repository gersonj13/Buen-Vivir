package com.pangea.evap.controller.gerentes;

import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "empleadogBean")
@RequestScoped
public class EmpleadogController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    List<EmpleadoTO> empleados;
    private boolean visibilidad;
    private List<StringArray> lista;
    private EmpleadoTO current;

    public List<StringArray> getLista() {
        return lista;
    }

    public void setLista(List<StringArray> lista) {
        this.lista = lista;
    }

    public boolean isVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(boolean visibilidad) {
        this.visibilidad = visibilidad;
    }
    private ArrayList<String> evalua;

    public ArrayList<String> getEvalua() {
        return evalua;
    }

    public void setEvalua(ArrayList<String> evalua) {
        this.evalua = evalua;
    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("pendiente")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
    }

    public String comprobarImagen(String cedula) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String ruta1;

        ruta1 = ec.getRealPath("/resources/images/usuarios/" + cedula + ".JPG");
        File f = new File(ruta1);
        boolean existe = f.exists();


        if (!existe) {

            return "/faces/resources/images/usuarios/noimage.jpg";
        }
        return "/faces/resources/images/usuarios/" + cedula + ".JPG";
    }

    public EmpleadoTO getCurrent() {
        return current;
    }

    public void setCurrent(EmpleadoTO current) {
        this.current = current;
    }

    public List<EmpleadoTO> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(ArrayList<EmpleadoTO> empleados) {
        this.empleados = empleados;
    }

    public EmpleadogController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);



        evalua = new ArrayList<String>();

        empleados = (List<EmpleadoTO>) sesion.getAttribute("empleadosg");

        visibilidad = false;


    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);



        evalua = new ArrayList<String>();

        empleados = (List<EmpleadoTO>) sesion.getAttribute("empleadosg");

        visibilidad = false;
    }

    public void prepareEvaluaciones() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {

            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/gerentes/evaluaciong.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public void setempleadoResultado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionadog");

        lista = (List<StringArray>) sesion.getAttribute("listaResultado");

    }

    public void prepareSeleccion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadoSeleccionadog", current);

        Evaluacion ev = (Evaluacion) sesion.getAttribute("formatoEvaluaciong");
        lista = this.planteamientosS(ev, current);

        sesion.setAttribute("listaResultado", lista);

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/gerentes/resultadosg.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void cancelar() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();




        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEmpleados.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }

    public void continuar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/gerentes/empleadosg.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }

    public void prepareEvaluacion() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionadog");


        Evaluacion ev = (Evaluacion) sesion.getAttribute("formatoEvaluacion");



        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/formatoEvaluacion.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }




    }

    public void setearEmpleado() {

        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionadog");



    }

    private java.util.List<com.pangea.evap.services.StringArray> planteamientosS(com.pangea.evap.services.Evaluacion evaluacion, com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.planteamientosS(evaluacion, empleado);
    }
}
