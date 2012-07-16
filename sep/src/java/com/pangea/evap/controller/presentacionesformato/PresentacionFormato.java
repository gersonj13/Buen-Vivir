package com.pangea.evap.controller.presentacionesformato;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@ManagedBean(name = "presentacionformatoController")
@SessionScoped
public class PresentacionFormato {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private FormatoEvaluacionFactor current, selected;
    private List<PresentacionFactor> formatospresentacionesg;
    private List<FormatoEvaluacionFactor> formatospresentacionese;
    private FormatoEvaluacion formatosel;
    private PresentacionFactor seleccionado;
    private BigInteger puntajeasignado;

    public BigInteger getPuntajeasignado() {
        return puntajeasignado;
    }

    public void setPuntajeasignado(BigInteger puntajeasignado) {
        this.puntajeasignado = puntajeasignado;
    }

    public PresentacionFactor getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(PresentacionFactor seleccionado) {
        this.seleccionado = seleccionado;
    }

    public List<PresentacionFactor> getFormatospresentacionesg() {
        return formatospresentacionesg;
    }

    public void setFormatospresentacionesg(List<PresentacionFactor> formatospresentacionesg) {
        this.formatospresentacionesg = formatospresentacionesg;
    }

    public List<FormatoEvaluacionFactor> getFormatospresentacionese() {
        return formatospresentacionese;
    }

    public void setFormatospresentacionese(List<FormatoEvaluacionFactor> formatospresentacionese) {
        this.formatospresentacionese = formatospresentacionese;
    }

    public FormatoEvaluacion getFormatosel() {
        return formatosel;
    }

    public void setFormatosel(FormatoEvaluacion formatosel) {
        this.formatosel = formatosel;
    }

    public FormatoEvaluacionFactor getCurrent() {
        return current;
    }

    public void setCurrent(FormatoEvaluacionFactor current) {
        this.current = current;
    }

    public FormatoEvaluacionFactor getSelected() {
        return selected;
    }

    public void setSelected(FormatoEvaluacionFactor selected) {
        this.selected = selected;
    }

    public PresentacionFormato() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
    }

    public void prepareFormatos() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/formatoEvaluacion/List.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        formatospresentacionesg = (List<PresentacionFactor>) sesion.getAttribute("formatospresentaciong");
        formatospresentacionese = (List<FormatoEvaluacionFactor>) sesion.getAttribute("formatospresentacione");
        formatosel = (FormatoEvaluacion) sesion.getAttribute("formatoseleccionado");
        selected = new FormatoEvaluacionFactor();
        puntajeasignado = new BigInteger("0");
        for (int i = 0; i < formatospresentacionese.size(); i++) {
            String x = puntajeasignado.toString();
            String s = formatospresentacionese.get(i).getPesoFactorEscala().toString();
            int num = Integer.parseInt(x) + Integer.parseInt(s);
            puntajeasignado = new BigInteger(String.valueOf(num));
        }



    }

    public String prepareList() {
        current = new FormatoEvaluacionFactor();

        return "List";
    }

    public void inicializar() {
        current = new FormatoEvaluacionFactor();
    }

    public void create() {

        BigDecimal form;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.crearformatofactor(formatosel, seleccionado);

        this.crearformatoevalplanteamiento(formatosel, seleccionado);

        List<PresentacionFactor> pref;
        pref = this.retornaPresentacionesformatos(formatosel.getId());
        sesion.setAttribute("formatospresentaciong", pref);
        List<FormatoEvaluacionFactor> prefe;
        prefe = this.retornaformatofactores(formatosel);
        sesion.setAttribute("formatospresentacione", prefe);
        formatospresentacionesg = (List<PresentacionFactor>) sesion.getAttribute("formatospresentaciong");
        formatospresentacionese = (List<FormatoEvaluacionFactor>) sesion.getAttribute("formatospresentacione");
        puntajeasignado = new BigInteger("0");
        for (int i = 0; i < formatospresentacionese.size(); i++) {
            String x = puntajeasignado.toString();
            String s = formatospresentacionese.get(i).getPesoFactorEscala().toString();
            int num = Integer.parseInt(x) + Integer.parseInt(s);
            puntajeasignado = new BigInteger(String.valueOf(num));
        }
    }

    public void eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarformatofactor(current);
        mostrarMensaje(0, "Exito", "Factor Eliminado del Formato");
        List<PresentacionFactor> pref;
        pref = this.retornaPresentacionesformatos(formatosel.getId());
        sesion.setAttribute("formatospresentaciong", pref);
        List<FormatoEvaluacionFactor> prefe;
        prefe = this.retornaformatofactores(formatosel);
        sesion.setAttribute("formatospresentacione", prefe);
        formatospresentacionesg = (List<PresentacionFactor>) sesion.getAttribute("formatospresentaciong");
        formatospresentacionese = (List<FormatoEvaluacionFactor>) sesion.getAttribute("formatospresentacione");
        puntajeasignado = new BigInteger("0");
        for (int i = 0; i < formatospresentacionese.size(); i++) {
            String x = puntajeasignado.toString();
            String s = formatospresentacionese.get(i).getPesoFactorEscala().toString();
            int num = Integer.parseInt(x) + Integer.parseInt(s);
            puntajeasignado = new BigInteger(String.valueOf(num));
        }


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
            current = (FormatoEvaluacionFactor) event.getObject();
            this.editarformatofactor(current);
            List<PresentacionFactor> pref;
            pref = this.retornaPresentacionesformatos(formatosel.getId());
            sesion.setAttribute("formatospresentaciong", pref);
            List<FormatoEvaluacionFactor> prefe;
            prefe = this.retornaformatofactores(formatosel);
            sesion.setAttribute("formatospresentacione", prefe);

            mostrarMensaje(0, "Exito", "Formato Evaluacion Editado");
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

    private java.util.List<com.pangea.evap.services.PresentacionFactor> retornaPresentacionesformatos(java.math.BigDecimal formato) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPresentacionesformatos(formato);
    }

    private java.util.List<com.pangea.evap.services.FormatoEvaluacionFactor> retornaformatofactores(com.pangea.evap.services.FormatoEvaluacion formato) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaformatofactores(formato);
    }

    private void crearformatofactor(com.pangea.evap.services.FormatoEvaluacion formato, com.pangea.evap.services.PresentacionFactor presentacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearformatofactor(formato, presentacion);
    }

    private void crearformatoevalplanteamiento(com.pangea.evap.services.FormatoEvaluacion formato, com.pangea.evap.services.PresentacionFactor presentacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearformatoevalplanteamiento(formato, presentacion);
    }

    private void editarformatofactor(com.pangea.evap.services.FormatoEvaluacionFactor formatoevaluacionfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarformatofactor(formatoevaluacionfactor);
    }

    private void eliminarformatofactor(com.pangea.evap.services.FormatoEvaluacionFactor formatoevaluacionfactor) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarformatofactor(formatoevaluacionfactor);
    }
}
