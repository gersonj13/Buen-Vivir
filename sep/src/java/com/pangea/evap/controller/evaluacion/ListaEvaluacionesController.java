
package com.pangea.evap.controller.evaluacion;

import com.pangea.evap.services.*;
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

/**
 *
 * @author Ing. Gerson Ramirez
 */
@ManagedBean(name = "listaevaluacionesBean")
@RequestScoped
public class ListaEvaluacionesController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private List<Evaluacion> listaEvaluaciones;
    private List<EmpleadoTO> listaEmpleados;
    private Evaluacion current;

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

    public List<Evaluacion> getListaEvaluaciones() {
        return listaEvaluaciones;
    }

    public void setListaEvaluaciones(List<Evaluacion> listaEvaluaciones) {
        this.listaEvaluaciones = listaEvaluaciones;
    }

    public void prepareEmpleados() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("formatoEvaluacion", current);
        listaEmpleados = getEmpleadosEvaluacion(sesion.getAttribute("codSupervisor").toString(), String.valueOf(current.getId()));
        sesion.removeAttribute("empleados");
        sesion.setAttribute("empleados", listaEmpleados);




        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEmpleados.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("1")) {
            return "background-color: aliceblue;";
        }
        return " background-color: mistyrose;";
    }

    public ListaEvaluacionesController() {
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        listaEvaluaciones = (List<Evaluacion>) sesion.getAttribute("Evaluaciones");

    }

    public void consultarEvaluaciones() {
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);

        listaEvaluaciones = new ArrayList<Evaluacion>();
        listaEvaluaciones = getEvaluacionesSuper(sesion.getAttribute("codSupervisor").toString());
        sesion.setAttribute("Evaluaciones", listaEvaluaciones);





    }

    public void preparePrincipal() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/inicio2.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    private java.util.List<com.pangea.evap.services.Evaluacion> getEvaluacionesSuper(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getEvaluacionesSuper(idEmpleado);
    }

    private java.util.List<com.pangea.evap.services.EmpleadoTO> getEmpleadosEvaluacion(java.lang.String idEmpleado, java.lang.String idEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getEmpleadosEvaluacion(idEmpleado, idEvaluacion);
    }

   
}
