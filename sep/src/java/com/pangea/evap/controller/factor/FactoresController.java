package com.pangea.evap.controller.factor;

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
@ManagedBean(name = "factoresController")
@SessionScoped
public class FactoresController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Factor current, selected;

    public Factor getSelected() {
        return selected;
    }
    private List<Factor> factores;

    public List<Factor> getFactores() {
        return factores;
    }

    public void setFactores(List<Factor> factores) {
        this.factores = factores;
    }

    public void setSelected(Factor selected) {
        this.selected = selected;
    }
  
    public FactoresController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("factores").toString().compareTo("nada") != 0) {
            factores = (List<Factor>) sesion.getAttribute("factores");
        }





    }

    

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        factores = this.retornaFactores();
        sesion.setAttribute("factores", factores);
        selected = new Factor();


    }

    public Factor getCurrent() {
        return current;
    }

    public void setCurrent(Factor current) {
        this.current = current;
    }

    public String prepareList() {
        current = new Factor();
        
        return "List";
    }

    public void inicializar() {
        current = new Factor();
    }

    public String create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        this.crearFactor(selected);
        mostrarMensaje(0, "Exito", "Factor Creado");
        factores = this.retornaFactores();
        sesion.setAttribute("factores", factores);
        selected = new Factor();


        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarFactor(current);
        mostrarMensaje(0, "Exito", "Factor Eliminado");
        factores = this.retornaFactores();
        sesion.setAttribute("factores", factores);

        return "";
    }

    public String prepareEdit() {
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (Factor) event.getObject();
            this.editarFactor(current);
            mostrarMensaje(0, "Exito", "Factor Editado");
            factores = this.retornaFactores();
            sesion.setAttribute("factores", factores);
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

    public void preparePresentacion() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.removeAttribute("factorseleccionado");
        sesion.setAttribute("factorseleccionado", current);
        Factor prueba;
        prueba = (Factor) sesion.getAttribute("factorseleccionado");
        List<PresentacionFactor> pres = this.retornaPresentaciones(current);

        sesion.setAttribute("presentaciones", pres);





        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/presentaciones/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    private java.util.List<com.pangea.evap.services.Factor> retornaFactores() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaFactores();
    }

    private void crearFactor(com.pangea.evap.services.Factor factor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearFactor(factor);
    }

    private void editarFactor(com.pangea.evap.services.Factor factor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarFactor(factor);
    }

    private void eliminarFactor(com.pangea.evap.services.Factor factor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarFactor(factor);
    }

   
    private java.util.List<com.pangea.evap.services.PresentacionFactor> retornaPresentaciones(com.pangea.evap.services.Factor factor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPresentaciones(factor);
    }

   
}