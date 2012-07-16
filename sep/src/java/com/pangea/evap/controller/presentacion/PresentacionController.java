package com.pangea.evap.controller.presentacion;

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
@ManagedBean(name = "presentacionesController")
@SessionScoped
public class PresentacionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private PresentacionFactor current, selected;
    private List<PresentacionFactor> presentaciones;
    private Factor factorsel;

    public PresentacionFactor getCurrent() {
        return current;
    }

    public void setCurrent(PresentacionFactor current) {
        this.current = current;
    }

    public PresentacionFactor getSelected() {
        return selected;
    }

    public void setSelected(PresentacionFactor selected) {
        this.selected = selected;
    }

    public List<PresentacionFactor> getPresentaciones() {
        return presentaciones;
    }

    public void setPresentaciones(List<PresentacionFactor> presentaciones) {
        this.presentaciones = presentaciones;
    }

    public Factor getFactorsel() {
        return factorsel;
    }

    public void setFactorsel(Factor factorsel) {
        this.factorsel = factorsel;
    }

    public PresentacionController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        presentaciones = (List<PresentacionFactor>) sesion.getAttribute("presentaciones");
        factorsel = (Factor) sesion.getAttribute("factorseleccionado");
        }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        factorsel = (Factor) sesion.getAttribute("factorseleccionado");
        presentaciones = this.retornaPresentaciones(factorsel);
        sesion.setAttribute("presentaciones", presentaciones);

        selected = new PresentacionFactor();


    }

    public String prepareList() {
        current = new PresentacionFactor();
        
        return "List";
    }

    public void inicializar() {
        current = new PresentacionFactor();
    }

    public String create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        this.crearPresentacion(factorsel, selected);
        mostrarMensaje(0, "Exito", "Presentacion Creada");
        presentaciones = this.retornaPresentaciones(factorsel);
        sesion.setAttribute("presentaciones", presentaciones);
        selected = new PresentacionFactor();


        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarPresentacion(current);
        mostrarMensaje(0, "Exito", "Presentacion Eliminada");
        presentaciones = this.retornaPresentaciones(factorsel);
        sesion.setAttribute("presentaciones", presentaciones);

        return "";
    }

    public void preparePlanteamientos() {
        
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.removeAttribute("presentacionseleccionada");
        sesion.setAttribute("presentacionseleccionada", current);
        List<Planteamiento> plan=this.retornaPlanteamientos(current);
        sesion.setAttribute("planteamientos", plan);

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/planteamientos/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


        
    
    }

 
    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (PresentacionFactor) event.getObject();
            this.editarPresentacion(current);
            mostrarMensaje(0, "Exito", "Presentacion Editada");
            presentaciones = this.retornaPresentaciones(factorsel);
            sesion.setAttribute("presentaciones", presentaciones);

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

    public void prepareFactor() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/factores/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    private java.util.List<com.pangea.evap.services.PresentacionFactor> retornaPresentaciones(com.pangea.evap.services.Factor factor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPresentaciones(factor);
    }

    private void eliminarPresentacion(com.pangea.evap.services.PresentacionFactor presentacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarPresentacion(presentacion);
    }

    private void editarPresentacion(com.pangea.evap.services.PresentacionFactor presentacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarPresentacion(presentacion);
    }

    private void crearPresentacion(com.pangea.evap.services.Factor factor, com.pangea.evap.services.PresentacionFactor presentacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearPresentacion(factor, presentacion);
    }

    private java.util.List<com.pangea.evap.services.Planteamiento> retornaPlanteamientos(com.pangea.evap.services.PresentacionFactor presfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPlanteamientos(presfactor);
    }
}