package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
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
// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "verEmpleadodBean")
@RequestScoped
public class VerEmpleadodController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Empleado empleado;
    private Empleado supervisor;
    private boolean exists;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public Empleado getEmpleados() {
        return empleado;
    }

    public Empleado getSupervisor() {
        return supervisor;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setSupervisor(Empleado supervisor) {
        this.supervisor = supervisor;
    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("pendiente")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
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

    public VerEmpleadodController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        exists = false;
        if (sesion.getAttribute("nosupervisor").toString().compareTo("true") == 0) {
            exists = true;
            supervisor = (Empleado) sesion.getAttribute("supervisordseleccionado");

        }
        empleado = (Empleado) sesion.getAttribute("empleadosdseleccionado");

    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtron, filtroan, filtroc);
    }
}
