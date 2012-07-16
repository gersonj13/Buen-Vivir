package com.pangea.evap.controller.organizacion;

import com.pangea.evap.services.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Ing. Gerson Ramirez
 */
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "agregarEmpleadodBean")
@RequestScoped
public class AgregarEmpleadodController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Empleado empleado;
    private Empleado supervisor;
    private List<Empleado> empleadosd;
    private String filtron, filtroa, filtroc;
    private Date fechanac, fechaing;
    private BigDecimal id;
    private String ncargo;
    private List<Cargo> cargos;
    private Cargo cargoseleccionado;
    private boolean mostrar;
    private Empleado superv = null;

    public String getNcargo() {
        return ncargo;
    }

    public void setNcargo(String ncargo) {
        this.ncargo = ncargo;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getFechaing() {
        return fechaing;
    }

    public void setFechaing(Date fechaing) {
        this.fechaing = fechaing;
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }

    public String getFiltroa() {
        return filtroa;
    }

    public void setFiltroa(String filtroa) {
        this.filtroa = filtroa;
    }

    public String getFiltroc() {
        return filtroc;
    }

    public void setFiltroc(String filtroc) {
        this.filtroc = filtroc;
    }

    public String getFiltron() {
        return filtron;
    }

    public void setFiltron(String filtron) {
        this.filtron = filtron;
    }

    public List<Empleado> getEmpleadosd() {
        return empleadosd;
    }

    public void setEmpleadosd(List<Empleado> empleadosd) {
        this.empleadosd = empleadosd;
    }

    public Empleado getSuperv() {
        return superv;
    }

    public void setSuperv(Empleado supe) {
        superv = supe;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void setCargos(List<Cargo> cargos) {
        this.cargos = cargos;
    }

    public Cargo getCargoseleccionado() {
        return cargoseleccionado;
    }

    public void setCargoseleccionado(Cargo cargoseleccionado) {
        this.cargoseleccionado = cargoseleccionado;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public Empleado getEmpleado() {
        return empleado;
    }

    public Empleado getSupervisor() {
        return supervisor;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setSupervisor(Empleado supervisor) {
        this.supervisor = supervisor;
    }

    public AgregarEmpleadodController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);



        empleado = new Empleado();
        Cargo c = new Cargo();
        c.setNombre("");
        empleado.setIdCargo(c);
        cargos = (List<Cargo>) sesion.getAttribute("cargos");


    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("pendiente")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
    }

    public List<Cargo> completar(String text) {



        List<Cargo> disponibles = new ArrayList<Cargo>();

        Iterator i = cargos.iterator();
        while (i.hasNext()) {

            Cargo c = (Cargo) i.next();
            if (c.getNombre().toLowerCase().startsWith(text.toLowerCase())) {
                disponibles.add(c);
            }

        }
        return disponibles;
    }

    public void prepareEmpleados() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/listaEmpleadosd.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        fechaing = (Date) sesion.getAttribute("fechaing");
        fechanac = (Date) sesion.getAttribute("fechanac");
    }

    public void filtrar() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
        List<Empleado> listaEmpleados = this.retornaEmpleadosd(de.getId().toString(), filtron, filtroa, filtroc);
        sesion.removeAttribute("empleadosd");
        sesion.setAttribute("empleadosd", listaEmpleados);
        empleadosd = (List<Empleado>) sesion.getAttribute("empleadosd");

    }

    public void prepareAgrega() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        supervisor = superv;

        sesion.setAttribute("supervisor2", superv);
    }

    public void Agregar() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpSession sesion = (HttpSession) ec.getSession(true);

            if (!empleado.getApellidos().equals("") && !empleado.getNombres().equals("") && !empleado.getCorreo().equals("")
                    && !empleado.getIdCargo().getNombre().equals("") && !empleado.getNroIdentificacion().toString().equals("")
                    && !empleado.getCodigo().equals("") && fechanac != null && fechaing != null && empleado.getCorreo().toLowerCase().endsWith("@seguroshorizonte.com")) {

                if (this.comprobarCorreo(empleado.getCorreo().trim().toLowerCase()).equals("aceptado")) {




                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(fechanac);
                    XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                    empleado.setFechaNacimiento(date1);

                    GregorianCalendar c2 = new GregorianCalendar();
                    c2.setTime(fechaing);
                    XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
                    empleado.setFechaIngreso(date2);

                    Dependencia de = (Dependencia) sesion.getAttribute("dependenciaSeleccionada");
                    empleado.setIdDependencia(de);
                    empleado.setTipoIdentificacion(86);
                    empleado.setObservation("I");
                    empleado.setId(BigDecimal.ZERO);

                    if (this.agregarUsuario(empleado)) {


                        Empleado empagregado;
                        empagregado = this.agregarEmpleado(empleado);
                        this.agregarEstadoEmpleado(empleado);
                        mostrarMensaje(0, "Exito", "Empleado Agregado");



                        sesion.setAttribute("empleadosdseleccionado", empagregado);
                        sesion.setAttribute("nosupervisor", "false");

                        sesion.setAttribute("supervisordseleccionado", null);
                        sesion.setAttribute("supervisor2", null);
                        try {
                            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/verEmpleadod.xhtml");

                        } catch (IOException e) {
                            //  LOG.error("Redirect failed");
                            throw new FacesException(e);
                        }

                    } else {
                        mostrarMensaje(3, "Error", "Empleado no pudo ser registrado");

                    }
                } else {
                    mostrarMensaje(2, "Error", "Empleado no existe en LDAP");

                }
            } else {
                mostrarMensaje(1, "Error", "Debe llenar todos los campos");

            }

        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(AgregarEmpleadodController.class.getName()).log(Level.SEVERE, null, ex);
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

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosd(java.lang.String idDependencia, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosd(idDependencia, filtron, filtroan, filtroc);
    }

    private boolean agregarUsuario(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.agregarUsuario(empleado);
    }

    private Empleado agregarEmpleado(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.agregarEmpleado(empleado);
    }

    private String comprobarCorreo(java.lang.String correo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.comprobarCorreo(correo);
    }

    private void agregarEstadoEmpleado(com.pangea.evap.services.Empleado empleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.agregarEstadoEmpleado(empleado);
    }
}
