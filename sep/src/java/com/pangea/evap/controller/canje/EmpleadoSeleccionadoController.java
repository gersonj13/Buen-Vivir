package com.pangea.evap.controller.canje;

import com.pangea.borrar.converter.util.JsfUtil;
import com.pangea.evap.services.*;
import java.io.File;
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

/**
 *
 * @author Ing. Gerson Ramirez
 */
@ManagedBean(name = "empleadoseleccionadoBean")
@SessionScoped
public class EmpleadoSeleccionadoController {

   @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private List<Seleccionado> seleccionados;
    private List<Canje> canje;
    private Corte corte;
    private String filtron, filtroa, filtroc;
    private Seleccionado eseleccionado;

    public List<Canje> getCanje() {
        return canje;
    }

    public void setCanje(List<Canje> canje) {
        this.canje = canje;
    }

    public Seleccionado getEseleccionado() {
        return eseleccionado;
    }

    public void setEseleccionado(Seleccionado eseleccionado) {
        this.eseleccionado = eseleccionado;
    }

    public Corte getCorte() {
        return corte;
    }

    public void setCorte(Corte corte) {
        this.corte = corte;
    }

    public List<Seleccionado> getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(List<Seleccionado> seleccionados) {
        this.seleccionados = seleccionados;
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

    public void prepareCortes() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);



        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/canje/corteCanje.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    //////////////////////////////////////////////////////////////////////////////////////////
    public void init() {
    }

    public void rowEditListener(RowEditEvent event) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            HttpSession sesion = (HttpSession) ec.getSession(true);
            Canje can = (Canje) event.getObject();
            this.editarCanje(can);
            canje = this.retornaCorteSeleccionado(eseleccionado);


        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));

        }
    }

    public void filtrar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);


        List<Seleccionado> listaSel = this.retornaEmpleadosSeleccionados(corte.getId().toString(), filtron, filtroa, filtroc);
        sesion.removeAttribute("listaSeleccionados");
        sesion.setAttribute("listaSeleccionados", listaSel);
        seleccionados = (List<Seleccionado>) sesion.getAttribute("listaSeleccionados");

    }

    public String estilo(String codigo) {

        if (codigo.equals("1")) {
            return "background-color: mistyrose;";
        }
        return " background-color: white;";
    }

    public String comprobarImagen(String cedula) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String ruta1;

        ruta1 = ec.getRealPath("/resources/images/usuarios/" + cedula + ".JPG");
        File f = new File(ruta1);
        boolean existe = f.exists();


        if (!existe) {

            return "/faces/resources/images/usuarios/noimage.jpg";
        }
        return "/faces/resources/images/usuarios/" + cedula + ".JPG";
    }

    public void prepareEmpleadoseleccionado() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadosdseleccionado", eseleccionado);
        Empleado sup = this.retornaSupervisor(eseleccionado.getId().toString());

        if (sup == null) {
            sesion.setAttribute("nosupervisor", "false");
        } else {
            sesion.setAttribute("nosupervisor", "true");
        }
        sesion.setAttribute("supervisordseleccionado", sup);
        sesion.setAttribute("supervisor2", null);
        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/organizacion/verEmpleadod.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }


    }

    public EmpleadoSeleccionadoController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        seleccionados = (List<Seleccionado>) sesion.getAttribute("listaSeleccionados");
        corte = (Corte) sesion.getAttribute("corteSeleccionado");



    }

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        seleccionados = (List<Seleccionado>) sesion.getAttribute("listaSeleccionados");
        corte = (Corte) sesion.getAttribute("corteSeleccionado");

    }

    public void canjes() {


        canje = this.retornaCorteSeleccionado(eseleccionado);

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

    private Empleado retornaSupervisor(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaSupervisor(idEmpleado);
    }

    private java.util.List<com.pangea.evap.services.Dependencia> retornaDependencias() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaDependencias();
    }

    private java.util.List<com.pangea.evap.services.Cargo> retornaCargos(java.lang.String filtro) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaCargos(filtro);
    }


    private java.util.List<com.pangea.evap.services.Canje> retornaCorteSeleccionado(com.pangea.evap.services.Seleccionado eseleccionado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaCorteSeleccionado(eseleccionado);
    }

    private void editarCanje(com.pangea.evap.services.Canje canje) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        port.editarCanje(canje);
    }

    private java.util.List<com.pangea.evap.services.Seleccionado> retornaEmpleadosSeleccionados(java.lang.String corte, java.lang.String filtron, java.lang.String filtroan, java.lang.String filtroc) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosSeleccionados(corte, filtron, filtroan, filtroc);
    }
}
