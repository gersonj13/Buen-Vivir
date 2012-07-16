package com.pangea.evap.controller.formatoevaluacion;

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
@ManagedBean(name = "formatosevaluacionController")
@SessionScoped
public class FormatosEvaluacionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private FormatoEvaluacion current, selected;

    public FormatoEvaluacion getCurrent() {
        return current;
    }

    public void setCurrent(FormatoEvaluacion current) {
        this.current = current;
    }

    public List<FormatoEvaluacion> getFormatos() {
        return formatos;
    }

    public void setFormatos(List<FormatoEvaluacion> formatos) {
        this.formatos = formatos;
    }

    public FormatoEvaluacion getSelected() {
        return selected;
    }

    public void setSelected(FormatoEvaluacion selected) {
        this.selected = selected;
    }
    private List<FormatoEvaluacion> formatos;

     
    public FormatosEvaluacionController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("formatos").toString().compareTo("nada") != 0) {
            formatos = (List<FormatoEvaluacion>) sesion.getAttribute("formatos");
        }


    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        formatos = this.retornaFormatos();
        sesion.setAttribute("formatos", formatos);
        selected = new FormatoEvaluacion();


    }

    public void prepareFactor() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);


        sesion.setAttribute("formatoseleccionado", current);
        
        
        
        List<PresentacionFactor> pref;
        pref= this.retornaPresentacionesformatos(current.getId());
        
        
        sesion.setAttribute("formatospresentaciong", pref);
        List<FormatoEvaluacionFactor> prefe;
        prefe= this.retornaformatofactores(current);
        sesion.setAttribute("formatospresentacione", prefe);
       
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/presentacionesformatos/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }




    }

    public String prepareList() {
        current = new FormatoEvaluacion();
        
        return "List";
    }

    public void inicializar() {
        current = new FormatoEvaluacion();
    }

    public String create() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        this.crearformatoEvaluacion(selected);
        mostrarMensaje(0, "Exito", "Formato Creado");
        formatos = this.retornaFormatos();
        sesion.setAttribute("formatos", formatos);
        selected = new FormatoEvaluacion();


        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarformatoEvaluacion(current);
        mostrarMensaje(0, "Exito", "Formato Eliminado");
        formatos = this.retornaFormatos();
        sesion.setAttribute("formatos", formatos);

        return "";
    }

  

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (FormatoEvaluacion) event.getObject();
            this.editarformatoEvaluacion(current);
            mostrarMensaje(0, "Exito", "Formato Editado");
            formatos = this.retornaFormatos();
            sesion.setAttribute("formatos", formatos);
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

    private java.util.List<com.pangea.evap.services.FormatoEvaluacion> retornaFormatos() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaFormatos();
    }

    private void editarformatoEvaluacion(com.pangea.evap.services.FormatoEvaluacion formatoEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarformatoEvaluacion(formatoEvaluacion);
    }

    private void crearformatoEvaluacion(com.pangea.evap.services.FormatoEvaluacion formatoEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearformatoEvaluacion(formatoEvaluacion);
    }

    private void eliminarformatoEvaluacion(com.pangea.evap.services.FormatoEvaluacion formatoEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarformatoEvaluacion(formatoEvaluacion);
    }

   

    private java.util.List<com.pangea.evap.services.FormatoEvaluacionFactor> retornaformatofactores(com.pangea.evap.services.FormatoEvaluacion formato) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaformatofactores(formato);
    }

    private java.util.List<com.pangea.evap.services.PresentacionFactor> retornaPresentacionesformatos(java.math.BigDecimal formato) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPresentacionesformatos(formato);
    }

  

    
}
