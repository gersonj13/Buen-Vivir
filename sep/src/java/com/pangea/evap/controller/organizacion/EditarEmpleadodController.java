package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
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
@ManagedBean(name = "editarEmpleadodBean")
@RequestScoped
public class EditarEmpleadodController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    List<Cargo> cargos;
    private Empleado empleado;
    private Empleado supervisor;
    private List<Empleado> empleadosd;
    private String filtron, filtroa, filtroc;
    private Cargo cargoseleccionado;
    private boolean mostrar;
    private Empleado superv = null;

    public String getFiltroa() {
        return filtroa;
    }

    public void setFiltroa(String filtroa) {
        this.filtroa = filtroa;
    }

    public String getFiltroc() {
        return filtroc;
    }

    public void setFiltroc(String filtroc) {
        this.filtroc = filtroc;
    }

    public String getFiltron() {
        return filtron;
    }

    public void setFiltron(String filtron) {
        this.filtron = filtron;
    }

    public List<Empleado> getEmpleadosd() {
        return empleadosd;
    }

    public void setEmpleadosd(List<Empleado> empleadosd) {
        this.empleadosd = empleadosd;
    }

    public Empleado getSuperv() {
        return superv;
    }

    public void setSuperv(Empleado supe) {
        superv = supe;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public Cargo getCargoseleccionado() {
        return cargoseleccionado;
    }

    public void setCargoseleccionado(Cargo cargoseleccionado) {
        this.cargoseleccionado = cargoseleccionado;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public Empleado getEmpleado() {
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

    public EditarEmpleadodController() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        superv = (Empleado) sesion.getAttribute("supervisor2");


        if (sesion.getAttribute("nosupervisor").toString().compareTo("true") == 0 && superv == null) {

            supervisor = (Empleado) sesion.getAttribute("supervisordseleccionado");

        }
        if (superv != null) {
            supervisor = (Empleado) sesion.getAttribute("supervisor2");

        }


        empleadosd = (List<Empleado>) sesion.getAttribute("empleadosd");

        empleado = (Empleado) sesion.getAttribute("empleadosdseleccionado");
        cargos = (List<Cargo>) sesion.getAttribute("cargos");

    }

    public void filtrar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), filtron, filtroa, filtroc);
        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleadosd = (List<Empleado>) sesion.getAttribute("empleadosd");

    }

    public void prepareEdit() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        supervisor = superv;

        sesion.setAttribute("supervisor2", superv);
    }

    public void eliminarSupervisore() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);


        this.eliminarSupervisor(empleado);
        sesion.setAttribute("empleadosdseleccionado", empleado);
        Empleado sup = this.retornaSupervisor(empleado.getId().toString());
        if (sup == null) {
            sesion.setAttribute("nosupervisor", "false");
        } else {
            sesion.setAttribute("nosupervisor", "true");
        }
        sesion.setAttribute("supervisordseleccionado", sup);
        sesion.setAttribute("supervisor2", null);

        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");

        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/verEmpleadod.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public void Edit() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        supervisor = (Empleado) sesion.getAttribute("supervisordseleccionado");
        superv = (Empleado) sesion.getAttribute("supervisor2");
        if (!empleado.getApellidos().equals("") && !empleado.getNombres().equals("") && !empleado.getCorreo().equals("")
                && !empleado.getCodigo().equals("") && empleado.getCorreo().toLowerCase().endsWith("@seguroshorizonte.com")) {

            if (this.comprobarCorreo(empleado.getCorreo().trim().toLowerCase()).equals("aceptado")) {



                if (superv == null) {
                    this.editarEmpleado(supervisor, empleado);
                } else {
                    this.editarEmpleado(superv, empleado);
                }
                if (superv != null || supervisor != null) {
                    this.creaestadoSupervisor(empleado);
                }
                mostrarMensaje(0, "Exito", "Empleado Actualizado");
                sesion.setAttribute("empleadosdseleccionado", empleado);
                Empleado sup = this.retornaSupervisor(empleado.getId().toString());
                if (sup == null) {
                    sesion.setAttribute("nosupervisor", "false");
                } else {
                    sesion.setAttribute("nosupervisor", "true");
                }
                sesion.setAttribute("supervisordseleccionado", sup);
                sesion.setAttribute("supervisor2", null);

                Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
                List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");

                sesion.removeAttribute("empleadosd");
                sesion.setAttribute("empleadosd", listaEmpleados);


                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/verEmpleadod.xhtml");

                } catch (IOException e) {
                    //  LOG.error("Redirect failed");
                    throw new FacesException(e);
                }
            } else {
                mostrarMensaje(2, "Error", "Empleado no existe en LDAP");

            }
        } else {
            mostrarMensaje(1, "Error", "Debe llenar todos los campos");

        }


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

    private Empleado retornaSupervisor(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaSupervisor(idEmpleado);
    }

    private void editarEmpleado(com.pangea.evap.services.Empleado empleado, com.pangea.evap.services.Empleado supervisor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarEmpleado(empleado, supervisor);
    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtron, filtroan, filtroc);
    }

    private void creaestadoSupervisor(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.creaestadoSupervisor(empleado);
    }

    private String comprobarCorreo(java.lang.String correo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.comprobarCorreo(correo);
    }

    private void eliminarSupervisor(com.pangea.evap.services.Empleado empleadox) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();

        port.eliminarSupervisor(empleadox);
    }
}
