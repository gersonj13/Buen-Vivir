package com.pangea.evap.controller.session;

import com.pangea.evap.services.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "sessionBean")
@SessionScoped
public class sessionBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private boolean logeado = false;
    private String errorx;
    private List<String> lstabla = new ArrayList<String>();
    private int valores[] = {10, 10, 10, 15};
    private String login;
    private String pass;
    private int contador = 5;
    private boolean supervisor, recursosh;
    private Empleado emp;
    private Dependencia ubicacion;
    private boolean Gerente;
    private boolean selec;

    public boolean isSelec() {
        return selec;
    }

    public void setSelec(boolean selec) {
        this.selec = selec;
    }

    public Seleccionado getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Seleccionado seleccionado) {
        this.seleccionado = seleccionado;
    }
    private Seleccionado seleccionado;

    public boolean isGerente() {
        return Gerente;
    }

    public void setGerente(boolean Gerente) {
        this.Gerente = Gerente;
    }

    public Dependencia getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Dependencia ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Empleado getEmp() {
        return emp;
    }

    public void setEmp(Empleado emp) {
        this.emp = emp;
    }

    public boolean isRecursosh() {
        return recursosh;
    }

    public void setRecursosh(boolean recursosh) {
        this.recursosh = recursosh;
    }

    public boolean isSupervisor() {
        return supervisor;
    }

    public void setSupervisor(boolean supervisor) {
        this.supervisor = supervisor;
    }

    public int getContador() {
        return contador;
    }

    public boolean mostrarPuntos() {


        return this.getEvaluacionesAbiertas(emp.getId().toString());
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public void increment() {

        contador--;
        if (contador == 0) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();


            try {
//            ec.redirect(ec.getRequestContextPath() + "/");

                ec.redirect(ec.getRequestContextPath());
                contador = 5;
            } catch (IOException e) {
                //  LOG.error("Redirect failed");
            }
        }
    }

    public int[] getValores() {
        return valores;
    }

    public void setValores(int[] valores) {

        this.valores = valores;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    private int valorLlegada;

    public int getValorLlegada() {
        return valorLlegada;
    }

    public void setValorLlegada(int valorLlegada) {
        this.valorLlegada = valorLlegada;
    }

    public sessionBean() {
    }

    public List<String> getLstabla() {
        return lstabla;
    }

    public void setLstabla(List<String> lstabla) {
        this.lstabla = lstabla;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public String getErrorx() {
        return errorx;
    }

    public void setError(String error) {
    }

    public void paso() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {

            ec.redirect(ec.getRequestContextPath() + "../Proyecto/evaluacion/listaEvaluaciones.xhtml");
            //
        } catch (IOException ex) {
            Logger.getLogger(sessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void off() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        try {

            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/sesionoff.xhtml");
            //
        } catch (IOException ex) {
            Logger.getLogger(sessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String iniciar() throws Exception {


        errorx = "Usuario o password incorrecto ";

        String email = "";
        String codigo = "";

         email = this.login(login, pass);
       // email = "angel.armas@seguroshorizonte.com";
        //email="Lorenzo.mulet@seguroshorizonte.com";
        if (!email.equals("rechazado") && !email.equals("pass")) {
            errorx = null;
            logeado = true;
            emp = this.retornaEmpleado(email);
            if (emp != null) {
                codigo = emp.getId().toString();

                ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
                HttpSession sesion = (HttpSession) ex.getSession(true);
                supervisor = isSupervisor_1(codigo);
                recursosh = this.isRecursos(email);
                ubicacion = this.isGerente_1(email);
                Gerente = false;


                seleccionado = this.isSeleccionado(codigo);
                selec = false;

                if (seleccionado != null) {
                    selec = true;
                    sesion.setAttribute("selec", seleccionado);

                }


                if (ubicacion != null) {
                    sesion.setAttribute("ubicacion", ubicacion);
                    Gerente = true;

                }

                if (supervisor) {
                    sesion.setAttribute("Perfil", "Supervisor");

                } else {
                    sesion.setAttribute("Perfil", "Empleado");

                }
                sesion.setAttribute("codSupervisor", codigo);
                sesion.setAttribute("Cargo", emp.getIdCargo().getNombre());

                sesion.setAttribute("NomUsu", emp.getNombres() + " " + emp.getApellidos());

                sesion.setAttribute("Empleado", emp);

                sesion.setAttribute("Usuario", login);
                sesion.setAttribute("Pagina", "../Proyecto/inicio2.xhtml"); //Asigno Pagina
                sesion.setAttribute("dependencia", "nada");
                sesion.setAttribute("dependencia2", "nada");
                sesion.setAttribute("factores", "nada");
                sesion.setAttribute("formatos", "nada");
                sesion.setAttribute("cargosn", "nada");
                sesion.setAttribute("evaluaciones", "nada");
                sesion.setAttribute("evaluacionesg", "nada");

                sesion.setAttribute("reportevaluaciones", "nada");
                sesion.setAttribute("reportevaluacionesgerentes", "nada");
                sesion.setAttribute("empleadoseliminados", "nada");
                sesion.setAttribute("periodos", "nada");
                sesion.setAttribute("cortes", "nada");
                sesion.setAttribute("listacanjes", "nada");
                sesion.setAttribute("evaluacionesempleado", "nada");

                sesion.setAttribute("evaluacionesEstadistico", "nada");
                List<TipoEvaluacion> tiposevaluacion;
                tiposevaluacion = this.retornaTiposEvaluacion();
                sesion.setAttribute("tiposevaluacion", tiposevaluacion);
            } else {
                logeado = false;
                errorx = "Consultar Tecnologia";
            }
        } else {
            errorx = "Usuario o password incorrecto ";
            if (email.equals("pass")) {
                errorx = "Usuario Debe Cambiar Password ";
            }
        }



        if (errorx == null) {


            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/menup/principal.xhtml");

            } catch (IOException e) {
                throw new FacesException(e);
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            if ("".compareTo(login) == 0 || "".compareTo(pass) == 0) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Campos Incompletos", "Por favor ingrese Login y Contraseña"));
            } else {

                if (errorx.compareTo("Consultar Tecnologia") == 0) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error con Correo", "Debe dirigirse a Gerencia de Tecnologia"));

                } else {
                    if (email.equals("pass")) {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Usuario Debe Cambiar Password"));

                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrecta"));

                    }
                }
            }


        }
        return null;
    }

    public String cerrar() {
        logeado = false;
        return "/login/index";
    }

    public String cerrarSesion(ActionEvent ac) {
        logeado = false;
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        sesion.invalidate();
        pass = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        try {
            ec.redirect(ec.getRequestContextPath());
        } catch (IOException ex1) {
            Logger.getLogger(sessionBean.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return "/login/index";
    }

    public String volver() {
        return "/index";
    }

    public static boolean isNumeric(String s) {
        try {
            double y = Double.parseDouble(s);
            return true;
        } catch (NumberFormatException err) {
            return false;
        }
    }

    private boolean isSupervisor_1(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.isSupervisor(idEmpleado);
    }

    private String login(java.lang.String user, java.lang.String password) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.login(user, password);
    }

    private Empleado retornaEmpleado(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleado(idEmpleado);
    }

    private boolean isRecursos(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.isRecursos(idEmpleado);
    }

    private boolean getEvaluacionesAbiertas(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getEvaluacionesAbiertas(idEmpleado);
    }

    private java.util.List<com.pangea.evap.services.TipoEvaluacion> retornaTiposEvaluacion() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaTiposEvaluacion();
    }

    private Dependencia isGerente_1(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.isGerente(idEmpleado);
    }

    private Seleccionado isSeleccionado(java.lang.String idEmpleado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.isSeleccionado(idEmpleado);
    }
}
