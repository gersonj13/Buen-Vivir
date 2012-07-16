package com.pangea.evap.controller.plantemiento;

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
@ManagedBean(name = "planteamientosController")
@SessionScoped
public class PlanteamientosController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Planteamiento current, selected;
    private List<Planteamiento> planteamientos;
    private PresentacionFactor prefactorsel;

    public PresentacionFactor getPrefactorsel() {
        return prefactorsel;
    }

    public void setPrefactorsel(PresentacionFactor prefactorsel) {
        this.prefactorsel = prefactorsel;
    }

    public List<Planteamiento> getPlanteamientos() {
        return planteamientos;
    }

    public void setPlanteamientos(List<Planteamiento> planteamientos) {
        this.planteamientos = planteamientos;
    }

    public Planteamiento getCurrent() {
        return current;
    }

    public void setCurrent(Planteamiento current) {
        this.current = current;
    }

    public Planteamiento getSelected() {
        return selected;
    }

    public void setSelected(Planteamiento selected) {
        this.selected = selected;
    }

    public PlanteamientosController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        planteamientos = (List<Planteamiento>) sesion.getAttribute("planteamientos");
        prefactorsel = (PresentacionFactor) sesion.getAttribute("presentacionseleccionada");

  }

    public void preparePresentaciones() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/presentaciones/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        prefactorsel = (PresentacionFactor) sesion.getAttribute("presentacionseleccionada");
        planteamientos = this.retornaPlanteamientos(prefactorsel);
        sesion.setAttribute("planteamientos", planteamientos);
        selected = new Planteamiento();


    }

    public String prepareList() {
        current = new Planteamiento();
        
        return "List";
    }

    public void inicializar() {
        current = new Planteamiento();
    }

    public String create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.crearPlanteamiento(selected, prefactorsel);
        this.actualizarPlanteamientoEval(prefactorsel);
        mostrarMensaje(0, "Exito", "Planteamiento Creado");
        planteamientos = this.retornaPlanteamientos(prefactorsel);
        sesion.setAttribute("planteamientos", planteamientos);
        selected = new Planteamiento();
        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarPlanteamiento(current);
        mostrarMensaje(0, "Exito", "Planteamiento Eliminado");
        planteamientos = this.retornaPlanteamientos(prefactorsel);
        sesion.setAttribute("planteamientos", planteamientos);

        return "";
    }

  
    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (Planteamiento) event.getObject();
            this.editarPlanteamiento(current);
            mostrarMensaje(0, "Exito", "Planteamiento Editado");
            planteamientos = this.retornaPlanteamientos(prefactorsel);
            sesion.setAttribute("planteamientos", planteamientos);

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

    private java.util.List<com.pangea.evap.services.Planteamiento> retornaPlanteamientos(com.pangea.evap.services.PresentacionFactor presfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPlanteamientos(presfactor);
    }

    private void crearPlanteamiento(com.pangea.evap.services.Planteamiento planteamiento, com.pangea.evap.services.PresentacionFactor presfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearPlanteamiento(planteamiento, presfactor);
    }

    private void editarPlanteamiento(com.pangea.evap.services.Planteamiento planteamiento) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarPlanteamiento(planteamiento);
    }

    private void eliminarPlanteamiento(com.pangea.evap.services.Planteamiento planteamiento) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarPlanteamiento(planteamiento);
    }

    private void actualizarPlanteamientoEval(com.pangea.evap.services.PresentacionFactor presfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.actualizarPlanteamientoEval(presfactor);
    }
}