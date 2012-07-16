package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.io.IOException;
import java.math.BigInteger;
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
@ManagedBean(name = "dependenciasBean")
@RequestScoped
public class DependenciasController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    TreeNode encontrado;
    List<Empleado> listaEmpleados;
    List<Dependencia> dependencias;
    private static Dependencia selected;
    private Dependencia currentagregar = new Dependencia();
    private TreeNode root;

    public Dependencia getCurrentagregar() {
        return currentagregar;
    }

    public void setCurrentagregar(Dependencia currentagregar) {
        this.currentagregar = currentagregar;
    }

    public Dependencia getSelected() {
        return selected;
    }

    public void setSelected(Dependencia selected) {
        this.selected = selected;
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

    public boolean selisnull() {
        if (selected == null) {
            return false;
        }
        return true;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public DependenciasController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("dependencia").toString().compareTo("nada") != 0) {
            root = (TreeNode) sesion.getAttribute("dependencia");
        }


    }

    public void prepareEmpleados() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("dependenciaSeleccionada", selected);

        listaEmpleados = this.retornaEmpleadosd(selected.getId().toString(), "", "", "");
        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);





        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/listaEmpleadosd.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }




    }

    public void cargarArbol() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        dependencias = this.retornaDependencias();
        root = new DefaultTreeNode("root", null);
        TreeNode def = new DefaultTreeNode(dependencias.get(0), root);
        hijos(def);
        sesion.setAttribute("dependencia", root);


    }

    public void prepareCreate() {
        currentagregar = new Dependencia();

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("padred", selected);
    }

    public void prepareDelete() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("padred", selected);
    }

    public void prepareEdit() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        sesion.setAttribute("padree", selected);

    }

    public void Crear() {
        boolean exito;

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        selected = (Dependencia) sesion.getAttribute("padred");

        currentagregar.setIdDependenciaPadre(selected);
        BigInteger niv = selected.getNivel().add(new BigInteger("1"));
        currentagregar.setNivel(niv);

        exito = this.agregarDependencia(currentagregar);

        if (!exito) {
            mostrarMensaje(2, "Error", "Dependencia no Agregada");

        } else {
            mostrarMensaje(0, "Exito", "Dependencia Agregada");
            cargarArbol();

        }
    }

    public void Delete() {
        boolean exito;

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        selected = (Dependencia) sesion.getAttribute("padred");



        this.eliminarDependencia(selected);


        mostrarMensaje(0, "Exito", "Dependencia Eliminada");
        cargarArbol();


    }

    public void Edit() {
        boolean exito;





        this.editarDependencia(selected);


        mostrarMensaje(0, "Exito", "Dependencia Editada");
        cargarArbol();


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

    private java.util.List<com.pangea.evap.services.Dependencia> retornaDependencias() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaDependencias();
    }

    private boolean agregarDependencia(com.pangea.evap.services.Dependencia dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.agregarDependencia(dependencia);
    }

    private void eliminarDependencia(com.pangea.evap.services.Dependencia dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarDependencia(dependencia);
    }

    private void editarDependencia(com.pangea.evap.services.Dependencia dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarDependencia(dependencia);
    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtron, filtroan, filtroc);
    }
}
