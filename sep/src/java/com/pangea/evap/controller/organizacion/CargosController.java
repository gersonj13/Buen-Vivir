package com.pangea.evap.controller.organizacion;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;

 
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "cargosController")
@SessionScoped
public class CargosController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Cargo current, selected;
    private List<Cargo> cargos;
    private String filtro;

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Cargo getCurrent() {
        return current;
    }

    public void setCurrent(Cargo current) {
        this.current = current;
    }

    public Cargo getSelected() {
        return selected;
    }

    public void setSelected(Cargo selected) {
        this.selected = selected;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public CargosController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("cargosn").toString().compareTo("nada") != 0) {
            cargos = (List<Cargo>) sesion.getAttribute("cargosn");
        }

    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        cargos = this.retornaCargos("");
        sesion.setAttribute("cargosn", cargos);
        selected = new Cargo();


    }

    public String prepareList() {
        current = new Cargo();
        
        return "List";
    }

    public void inicializar() {
        current = new Cargo();
    }

    public String create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        Empleado emp = new Empleado();
        emp.setIdCargo(selected);
        this.agregarCargo(emp);
        mostrarMensaje(0, "Exito", "Cargo Creado");
        cargos = this.retornaCargos("");
        sesion.setAttribute("cargosn", cargos);
        selected = new Cargo();


        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (this.eliminarCargo(current)) {
            mostrarMensaje(0, "Exito", "Presentacion Eliminada");
        } else {
            mostrarMensaje(2, "Error", "Hay empleados con ese cargo");

        }

        cargos = this.retornaCargos("");
        sesion.setAttribute("cargosn", cargos);

        return "";
    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (Cargo) event.getObject();
            this.editarCargo(current);
            mostrarMensaje(0, "Exito", "Cargo Editado");
            cargos = this.retornaCargos("");
            sesion.setAttribute("cargosn", cargos);

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

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

    public void filtrar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        List<Cargo> listacargos = this.retornaCargos(filtro);
        sesion.removeAttribute("cargosn");
        sesion.setAttribute("cargosn", listacargos);
        cargos = (List<Cargo>) sesion.getAttribute("cargosn");

    }
  
  
    private void editarCargo(com.pangea.evap.services.Cargo cargo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarCargo(cargo);
    }

    private void agregarCargo(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.agregarCargo(empleado);
    }

    private boolean eliminarCargo(com.pangea.evap.services.Cargo cargo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.eliminarCargo(cargo);
    }

    private java.util.List<com.pangea.evap.services.Cargo> retornaCargos(java.lang.String filtro) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaCargos(filtro);
    }

   
}