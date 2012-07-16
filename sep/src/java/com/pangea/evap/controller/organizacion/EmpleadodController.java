package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Ing. Gerson Ramirez
 */
// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "empleadodBean")
@RequestScoped
public class EmpleadodController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    List<Dependencia> dependencias;
    private List<Empleado> empleados;
    private Empleado eseleccionado;
    private TreeNode root;
    private String filtron, filtroa, filtroc;
    private Dependencia seleccionada;

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

    public List<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<Dependencia> dependencias) {
        this.dependencias = dependencias;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Empleado getEseleccionado() {
        return eseleccionado;
    }

    public void setEseleccionado(Empleado eseleccionado) {
        this.eseleccionado = eseleccionado;
    }

    public Dependencia getSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(Dependencia seleccionada) {
        this.seleccionada = seleccionada;
    }

    public void cargarArbol() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        dependencias = this.retornaDependencias();
        root = new DefaultTreeNode("root", null);
        TreeNode def = new DefaultTreeNode(dependencias.get(0), root);
        hijos(def);
        sesion.setAttribute("dependencia2", root);


    }

    public void hijos(TreeNode root) {

        Dependencia dep = (Dependencia) root.getData();

        Iterator it = dependencias.iterator();
        while (it.hasNext()) {
            Dependencia dit = (Dependencia) it.next();


            if (dit.getNivel().compareTo(BigInteger.ONE) != 0) {
                if (dit.getIdDependenciaPadre().getId().equals(dep.getId())) {

                    TreeNode def = new DefaultTreeNode(dit, root);
                    hijos(def);

                }
            }

        }



    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("pendiente")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
    }

    public void procesarCambio() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        eseleccionado = (Empleado) sesion.getAttribute("empleadocambio");
        this.cambiarDependencia(seleccionada, eseleccionado);
        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");

        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleados = (List<Empleado>) sesion.getAttribute("empleadosd");
        mostrarMensaje(0, "Exito", "Empleado Reubicado");


    }

    public void filtrar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), filtron, filtroa, filtroc);
        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleados = (List<Empleado>) sesion.getAttribute("empleadosd");

    }

    public void prepareDependencia() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");

        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleados = (List<Empleado>) sesion.getAttribute("empleadosd");

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/listaDependencias.xhtml");

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

    public void prepareEmpleadoseleccionado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadosdseleccionado", eseleccionado);
        Empleado sup = this.retornaSupervisor(eseleccionado.getId().toString());

        if (sup == null) {
            sesion.setAttribute("nosupervisor", "false");
        } else {
            sesion.setAttribute("nosupervisor", "true");
        }
        sesion.setAttribute("supervisordseleccionado", sup);
        sesion.setAttribute("supervisor2", null);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/verEmpleadod.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public void prepareEliminarempleadoseleccionado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadosdseleccionado", eseleccionado);
        Empleado sup = this.retornaSupervisor(eseleccionado.getId().toString());

        if (sup == null) {
            sesion.setAttribute("nosupervisor", "false");
        } else {
            sesion.setAttribute("nosupervisor", "true");
        }
        sesion.setAttribute("supervisordseleccionado", sup);
        sesion.setAttribute("supervisor2", null);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/eliminarEmpleadod.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public void prepareCambiard() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("empleadocambio", eseleccionado);
    }

    public void prepareAgregarempleadoseleccionado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("fechanac", new Date());
        sesion.setAttribute("fechaing", new Date());
        List<Cargo> cargos = this.retornaCargos("");
        sesion.setAttribute("cargos", cargos);


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/agregarEmpleadod.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public void prepareEliminarEvaluacion() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadoevaluacions", eseleccionado);

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/eliminarEvaluacion.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }




    }

    public void prepareEditarempleadoseleccionado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadosdseleccionado", eseleccionado);
        Empleado sup = this.retornaSupervisor(eseleccionado.getId().toString());
        List<Cargo> cargos = this.retornaCargos("");


        if (sup == null) {
            sesion.setAttribute("nosupervisor", "false");
        } else {
            sesion.setAttribute("nosupervisor", "true");
        }
        sesion.setAttribute("supervisordseleccionado", sup);
        sesion.setAttribute("supervisor2", null);
        sesion.setAttribute("cargos", cargos);

        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados;

        listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), "", "", "");


        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleados = (List<Empleado>) sesion.getAttribute("empleadosd");


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/editarEmpleadosd.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public EmpleadodController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        empleados = (List<Empleado>) sesion.getAttribute("empleadosd");


        if (sesion.getAttribute("dependencia2").toString().compareTo("nada") != 0) {
            root = (TreeNode) sesion.getAttribute("dependencia2");
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

    private java.util.List<com.pangea.evap.services.Dependencia> retornaDependencias() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaDependencias();
    }

    private void cambiarDependencia(com.pangea.evap.services.Dependencia dependencia, com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.cambiarDependencia(dependencia, empleado);
    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtron, filtroan, filtroc);
    }

    private java.util.List<com.pangea.evap.services.Cargo> retornaCargos(java.lang.String filtro) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaCargos(filtro);
    }
}
