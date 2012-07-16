package com.pangea.evap.controller.canje;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.GestionEvaluacion_Service;
import com.pangea.evap.services.Periodo;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;

 
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "periodocanjeController")
@SessionScoped
public class PeriodoCanjeController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Periodo current, selected;
    private List<Periodo> periodos;
    private Date Fechai, Fechaf;
    private Date Fechaie, Fechafe;

    public Date getFechaf() {
        return Fechaf;
    }

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setFechaf(Date Fechaf) {
        this.Fechaf = Fechaf;
    }

    public Date getFechafe() {
        return Fechafe;
    }

    public void setFechafe(Date Fechafe) {
        this.Fechafe = Fechafe;
    }

    public Date getFechai() {
        return Fechai;
    }

    public void setFechai(Date Fechai) {
        this.Fechai = Fechai;
    }

    public Date getFechaie() {
        return Fechaie;
    }

    public void setFechaie(Date Fechaie) {
        this.Fechaie = Fechaie;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    public Periodo getCurrent() {
        return current;
    }

    public void setCurrent(Periodo current) {
        this.current = current;
    }

    public Periodo getSelected() {
        return selected;
    }

    public void setSelected(Periodo selected) {
        this.selected = selected;
    }

    public void TransformarFechas1(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechaie = xcal.toGregorianCalendar().getTime();

    }

    public void TransformarFechas2(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechafe = xcal.toGregorianCalendar().getTime();

    }

    public PeriodoCanjeController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("periodos").toString().compareTo("nada") != 0) {
            periodos = (List<Periodo>) sesion.getAttribute("periodos");
        }

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
        periodos = this.retornaPeriodos();
        sesion.setAttribute("periodos", periodos);
        selected = new Periodo();
        Fechai = new Date();
        Fechaf = new Date();

    }

    public void inicializar() {
        current = new Periodo();
    }

    public String create() throws DatatypeConfigurationException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        if (Fechai.before(Fechaf) && !selected.getDescripcion().equals("")) {

            GregorianCalendar c = new GregorianCalendar();
            c.setTime(Fechai);
            XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            selected.setFechaInicioEvaluacion(date1);

            GregorianCalendar c2 = new GregorianCalendar();
            c2.setTime(Fechaf);
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
            selected.setFechaFinEvaluacion(date2);


            this.crearPeriodo(selected);
            mostrarMensaje(0, "Exito", "Periodo Creado");
            periodos = this.retornaPeriodos();
            sesion.setAttribute("periodos", periodos);
            selected = new Periodo();
            Fechai = new Date();
            Fechaf = new Date();

        } else {
            mostrarMensaje(1, "Error", "Valores Incorrectos");

        }
        return "";
    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarPeriodo(current);
        mostrarMensaje(0, "Exito", "Periodo Eliminado");
        periodos = this.retornaPeriodos();
        sesion.setAttribute("periodos", periodos);

        return "";
    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (Periodo) event.getObject();

            GregorianCalendar c = new GregorianCalendar();
            c.setTime(Fechaie);
            XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            current.setFechaInicioEvaluacion(date1);

            GregorianCalendar c2 = new GregorianCalendar();
            c2.setTime(Fechafe);
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
            current.setFechaFinEvaluacion(date2);

            if (!Fechaie.after(Fechafe)) {


                this.editarPeriodo(current);
                mostrarMensaje(0, "Exito", "Periodo Editado");
                periodos = this.retornaPeriodos();
                sesion.setAttribute("periodos", periodos);
                Fechaie = new Date();
                Fechafe = new Date();
            } else {
                mostrarMensaje(0, "Error", "Fechas Mal distribuidas");

            }
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

    private java.util.List<com.pangea.evap.services.Periodo> retornaPeriodos() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaPeriodos();
    }

    private void editarPeriodo(com.pangea.evap.services.Periodo periodo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarPeriodo(periodo);
    }

    private void eliminarPeriodo(com.pangea.evap.services.Periodo periodo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarPeriodo(periodo);
    }

    private void crearPeriodo(com.pangea.evap.services.Periodo periodo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearPeriodo(periodo);
    }
}