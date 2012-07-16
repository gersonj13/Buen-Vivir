package com.pangea.evap.controller.canje;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.RowEditEvent;

// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "corteController")
@SessionScoped
public class CorteController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Corte current, selected;
    private List<Corte> cortes;
    private Date Fechai, Fechaf;
    private Date Fechaie, Fechafe;
    private List<Periodo> periodos;
    private String[] caracteristicas;
    private BigDecimal monto;

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String[] getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String[] caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public List<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Periodo> periodos) {
        this.periodos = periodos;
    }

    public List<Corte> getCortes() {
        return cortes;
    }

    public void setCortes(List<Corte> cortes) {
        this.cortes = cortes;
    }

    public Corte getCurrent() {
        return current;
    }

    public void setCurrent(Corte current) {
        this.current = current;
    }

    public Corte getSelected() {
        return selected;
    }

    public void setSelected(Corte selected) {
        this.selected = selected;
    }

    public Date getFechaf() {
        return Fechaf;
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

    public void TransformarFechas1(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechaie = xcal.toGregorianCalendar().getTime();

    }

    public void TransformarFechas2(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechafe = xcal.toGregorianCalendar().getTime();

    }

    public CorteController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("cortes").toString().compareTo("nada") != 0) {
            cortes = (List<Corte>) sesion.getAttribute("cortes");
        }

    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        cortes = this.retornaCorte();
        periodos = this.retornaPeriodos();
        sesion.setAttribute("cortes", cortes);
        selected = new Corte();
        Fechai = new Date();
        Fechaf = new Date();
        caracteristicas = new String[4];
        for (int i = 0; i < caracteristicas.length; i++) {
            caracteristicas[i] = "0";

        }

    }

    public void inicializar() {
        current = new Corte();
    }

    public void Caracteristicas() throws DatatypeConfigurationException {

        List<String> lista;
        caracteristicas = new String[4];
        if (selected.getIdPeriodo() != null && selected.getMinimoPromedioPuntos().toString().compareTo("") != 0) {
            lista = this.datosEvaluaciones(selected.getIdPeriodo(), selected.getMinimoPromedioPuntos());

            for (int i = 0; i < lista.size(); i++) {
                caracteristicas[i] = lista.get(i);

            }


        } else {
            mostrarMensaje(1, "Error", "Debe seleccionar Periodo y Minimo de puntos");
        }

    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            current = (Corte) event.getObject();
            this.editarCorte(current);
            mostrarMensaje(0, "Exito", "Corte Editado");
            cortes = this.retornaCorte();
            sesion.setAttribute("cortes", cortes);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

        }
    }

    public String create() throws DatatypeConfigurationException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        if (selected.getIdPeriodo() != null && selected.getMinimoPromedioPuntos().toString().compareTo("") != 0
                && monto.toString().compareTo("") != 0) {



            selected.setId(BigDecimal.ZERO);
            this.crearCorte(selected.getIdPeriodo(), selected, monto);
            selected = this.corteMaximo();
            this.seleccionarEmpleados(selected);
            mostrarMensaje(0, "Exito", "Corte Creado");
            cortes = this.retornaCorte();
            sesion.setAttribute("cortes", cortes);
            selected = new Corte();
            Fechai = new Date();
            Fechaf = new Date();

        } else {
            mostrarMensaje(1, "Error", "Valores Incorrectos");

        }
        return "";
    }

    public void prepareEmpleadosSeleccionados() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("corteSeleccionado", current);
        List<Seleccionado> selec = this.retornaEmpleadosSeleccionados(current.getId().toString(), "", "", "");
        sesion.setAttribute("listaSeleccionados", selec);

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/canje/empleadosSeleccionados.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
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

    private java.util.List<com.pangea.evap.services.Corte> retornaCorte() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaCorte();
    }

    private void seleccionarEmpleados(com.pangea.evap.services.Corte corte) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.seleccionarEmpleados(corte);
    }

    private Corte corteMaximo() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.corteMaximo();
    }

    private java.util.List<java.lang.String> datosEvaluaciones(com.pangea.evap.services.Periodo peroiodo, java.math.BigDecimal arg1) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.datosEvaluaciones(peroiodo, arg1);
    }

    private void crearCorte(com.pangea.evap.services.Periodo periodo, com.pangea.evap.services.Corte corte, java.math.BigDecimal monto) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearCorte(periodo, corte, monto);
    }

    private java.util.List<com.pangea.evap.services.Seleccionado> retornaEmpleadosSeleccionados(java.lang.String corte, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosSeleccionados(corte, filtron, filtroan, filtroc);
    }

    private void editarCorte(com.pangea.evap.services.Corte cortex) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarCorte(cortex);
    }
}
