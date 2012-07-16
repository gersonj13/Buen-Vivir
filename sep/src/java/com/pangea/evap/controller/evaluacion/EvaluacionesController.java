package com.pangea.evap.controller.evaluacion;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "evaluacionesController")
@SessionScoped
public class EvaluacionesController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Evaluacion current, selected;
    private List<Evaluacion> evaluaciones;
    private List<FormatoEvaluacion> formatos;
    private List<Dependencia> dependencias;
    private FormatoEvaluacion formatoseleccionado;
    private TreeNode root;
    private Date Fechai, Fechaf;
    private Date Fechaie, Fechafe;

    public Date getFechafe() {
        return Fechafe;
    }

    public void setFechafe(Date Fechafe) {
        this.Fechafe = Fechafe;
    }

    public Date getFechaie() {
        return Fechaie;
    }

    public void setFechaie(Date Fechaie) {
        this.Fechaie = Fechaie;
    }

    public Date getFechaf() {
        return Fechaf;
    }

    public void setFechaf(Date Fechaf) {
        this.Fechaf = Fechaf;
    }

    public Date getFechai() {
        return Fechai;
    }

    public void setFechai(Date Fechai) {
        this.Fechai = Fechai;
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

    public FormatoEvaluacion getFormatoseleccionado() {
        return formatoseleccionado;
    }

    public void setFormatoseleccionado(FormatoEvaluacion formatoseleccionado) {
        this.formatoseleccionado = formatoseleccionado;
    }

    public List<FormatoEvaluacion> getFormatos() {
        return formatos;
    }

    public void setFormatos(List<FormatoEvaluacion> formatos) {
        this.formatos = formatos;
    }

    public Evaluacion getCurrent() {
        return current;
    }

    public void setCurrent(Evaluacion current) {
        this.current = current;
    }

    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public Evaluacion getSelected() {
        return selected;
    }

    public void setSelected(Evaluacion selected) {
        this.selected = selected;
    }

    public EvaluacionesController() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("evaluaciones").toString().compareTo("nada") != 0) {
            evaluaciones = (List<Evaluacion>) sesion.getAttribute("evaluaciones");
        }





    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        evaluaciones = this.retornaEvaluaciones();
        sesion.setAttribute("evaluaciones", evaluaciones);
        formatos = this.retornaFormatos();
        selected = new Evaluacion();
        formatoseleccionado = new FormatoEvaluacion();
        selected.setIdFormatoEvaluacion(new FormatoEvaluacion());
        selected.setIdDependencia(new Dependencia());
        Fechai = new Date();
        Fechaf = new Date();


    }

    public String prepareList() {
        current = new Evaluacion();

        return "List";
    }

    public void inicializar() {
        current = new Evaluacion();
    }

    public String create() throws DatatypeConfigurationException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        if (Fechai.before(Fechaf) && !selected.getIdDependencia().getNombre().equals("") && !selected.getIdFormatoEvaluacion().getNombre().equals("") && !selected.getPeso().toString().equals("")) {

            String codigo = (String) sesion.getAttribute("codSupervisor");
            Usuario us = new Usuario();

            us.setId(new BigDecimal(codigo.toString()));
            selected.setIdCreador(us);


            GregorianCalendar c = new GregorianCalendar();
            c.setTime(Fechai);
            XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            selected.setFechaInicio(date1);

            GregorianCalendar c2 = new GregorianCalendar();
            c2.setTime(Fechaf);
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
            selected.setFechaFin(date2);

            this.crearEvaluacion(selected);

            mostrarMensaje(0, "Exito", "Evaluacion Creada");



        } else {
            mostrarMensaje(1, "Error", "Valores Incorrectos");

        }
        evaluaciones = this.retornaEvaluaciones();
        sesion.setAttribute("evaluaciones", evaluaciones);
        formatos = this.retornaFormatos();
        selected = new Evaluacion();
        formatoseleccionado = new FormatoEvaluacion();
        selected.setIdFormatoEvaluacion(new FormatoEvaluacion());
        selected.setIdDependencia(new Dependencia());
        Fechai = new Date();
        Fechaf = new Date();
        return "";
    }

    public void TransformarFechas1(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechaie = xcal.toGregorianCalendar().getTime();

    }

    public void TransformarFechas2(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechafe = xcal.toGregorianCalendar().getTime();

    }

    public String eliminar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        this.eliminarEvaluacion(current);
        mostrarMensaje(0, "Exito", "Evaluacion Eliminada");

        evaluaciones = this.retornaEvaluaciones();
        sesion.setAttribute("evaluaciones", evaluaciones);
        formatos = this.retornaFormatos();
        selected = new Evaluacion();
        formatoseleccionado = new FormatoEvaluacion();
        selected.setIdFormatoEvaluacion(new FormatoEvaluacion());
        selected.setIdDependencia(new Dependencia());
        Fechai = new Date();
        Fechaf = new Date();

        return "";
    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);

            current = (Evaluacion) event.getObject();
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(Fechaie);
            XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            current.setFechaInicio(date1);

            GregorianCalendar c2 = new GregorianCalendar();
            c2.setTime(Fechafe);
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
            current.setFechaFin(date2);

            if (!Fechaie.after(Fechafe)) {

                String codigo = (String) sesion.getAttribute("codSupervisor");
                Usuario us = new Usuario();
                us.setId(new BigDecimal(codigo.toString()));
                current.setIdCreador(us);



                this.editarEvaluacion(current);
                evaluaciones = this.retornaEvaluaciones();
                sesion.setAttribute("evaluaciones", evaluaciones);
                formatos = this.retornaFormatos();
                selected = new Evaluacion();
                formatoseleccionado = new FormatoEvaluacion();
                selected.setIdFormatoEvaluacion(new FormatoEvaluacion());
                selected.setIdDependencia(new Dependencia());
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

    private java.util.List<com.pangea.evap.services.Evaluacion> retornaEvaluaciones() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEvaluaciones();
    }

    private java.util.List<com.pangea.evap.services.FormatoEvaluacion> retornaFormatos() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaFormatos();
    }

    private java.util.List<com.pangea.evap.services.Dependencia> retornaDependencias() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaDependencias();
    }

    private void editarEvaluacion(com.pangea.evap.services.Evaluacion evaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarEvaluacion(evaluacion);
    }

    private void eliminarEvaluacion(com.pangea.evap.services.Evaluacion evaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.eliminarEvaluacion(evaluacion);
    }

    private void crearEvaluacion(com.pangea.evap.services.Evaluacion evaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.crearEvaluacion(evaluacion);
    }
}
