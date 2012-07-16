package com.pangea.evap.controller.evaluacion;

import com.pangea.evap.controller.util.NodoP;
import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Ing. Gerson Ramirez
 */
@ManagedBean(name = "empleadoBean")
@SessionScoped
public class EmpleadoController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    List<EmpleadoTO> empleados;
    List<TipoEvaluacion> tipos;
    List<String> tipostring;
    private String tiposel;
    private boolean visibilidad;
    private EmpleadoTO current;

    public boolean isVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(boolean visibilidad) {
        this.visibilidad = visibilidad;
    }

    public String getTiposel() {
        return tiposel;
    }

    public List<String> getTipostring() {
        return tipostring;
    }

    public void setTipostring(List<String> tipostring) {
        this.tipostring = tipostring;
    }

    public void setTiposel(String tiposel) {
        this.tiposel = tiposel;
    }

    public List<TipoEvaluacion> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoEvaluacion> tipos) {
        this.tipos = tipos;
    }
    private ArrayList<String> evalua;

    public ArrayList<String> getEvalua() {
        return evalua;
    }

    public void setEvalua(ArrayList<String> evalua) {
        this.evalua = evalua;
    }

    public void init() {
    }

    public String estilo(String codigo) {

        if (codigo.equals("pendiente")) {
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

    public EmpleadoTO getCurrent() {
        return current;
    }

    public void setCurrent(EmpleadoTO current) {
        this.current = current;
    }

    public List<EmpleadoTO> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(ArrayList<EmpleadoTO> empleados) {
        this.empleados = empleados;
    }

    public EmpleadoController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);



        evalua = new ArrayList<String>();

        empleados = (List<EmpleadoTO>) sesion.getAttribute("empleados");
        tipos = (List<TipoEvaluacion>) sesion.getAttribute("tiposevaluacion");
        tipostring = new ArrayList<String>();
        for (int i = 0; i < tipos.size(); i++) {
            tipostring.add(tipos.get(i).getNombre());
        }
        tiposel = "Evaluacion";
        visibilidad = false;


    }

    public void iniciar() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);



        evalua = new ArrayList<String>();

        empleados = (List<EmpleadoTO>) sesion.getAttribute("empleados");
        tipos = (List<TipoEvaluacion>) sesion.getAttribute("tiposevaluacion");
        tipostring = new ArrayList<String>();
        for (int i = 0; i < tipos.size(); i++) {
            tipostring.add(tipos.get(i).getNombre());
        }
        tiposel = "Evaluacion";
        visibilidad = false;

    }

    public void prepareEvaluaciones() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEvaluaciones.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }



    }

    public void prepareSeleccion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        sesion.setAttribute("empleadoSeleccionado", current);


    }

    public void aceptar() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        ResultadoEvaluacionTO resultado = new ResultadoEvaluacionTO();
        visibilidad = false;
        Empleado sup = new Empleado(), empleado2;
        EmpleadoTO emp2;
        empleado2 = new Empleado();
        emp2 = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionado");

        //////////////////////////////////////////////////////////////
        empleado2.setApellidos(emp2.getApellidos());
        empleado2.setCodigo(emp2.getCodigo());
        empleado2.setFechaIngreso(emp2.getFechaIngreso());
        empleado2.setFechaNacimiento(emp2.getFechaNacimiento());
        empleado2.setId(emp2.getId());
        empleado2.setIdCargo(emp2.getIdCargo());
        empleado2.setIdDependencia(emp2.getIdDependencia());
        empleado2.setIdUsuario(emp2.getIdUsuario());
        empleado2.setNombres(emp2.getNombres());
        empleado2.setNroIdentificacion(emp2.getNroIdentificacion());
        empleado2.setTipoIdentificacion(emp2.getTipoIdentificacion());
        //////////////////////////////////////////////////////////////
        resultado.setEmpleadox(empleado2);


        sup.setId(new BigDecimal(sesion.getAttribute("codSupervisor").toString()));
        resultado.setSupervisor(sup);
        /////////////////////////////////////////////////////////////////////
        TipoEvaluacion ti = this.retornaTipoEvaluacion("Vacaciones");
        resultado.setTipoEvaluacionx(ti);
        ////////////////////////////////////////////////////////////////
        resultado.setObservacion("");
        //////////////////////////////////////////////////////////////////
        resultado.getPlanteamientos().clear();
        /////////////////////////////////////////////////////////////////
        Evaluacion eva = (Evaluacion) sesion.getAttribute("formatoEvaluacion");
        resultado.setEvaluacionx(eva);
        String err;

        err = this.registrarEvaluacionVacaciones(resultado);


        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEvaluaciones.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void cancelar() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();




        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEmpleados.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }

    public void aceptarreposo() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        ResultadoEvaluacionTO resultado = new ResultadoEvaluacionTO();

        Empleado sup = new Empleado(), empleado2;
        EmpleadoTO emp2;
        empleado2 = new Empleado();
        emp2 = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionado");

        //////////////////////////////////////////////////////////////
        empleado2.setApellidos(emp2.getApellidos());
        empleado2.setCodigo(emp2.getCodigo());
        empleado2.setFechaIngreso(emp2.getFechaIngreso());
        empleado2.setFechaNacimiento(emp2.getFechaNacimiento());
        empleado2.setId(emp2.getId());
        empleado2.setIdCargo(emp2.getIdCargo());
        empleado2.setIdDependencia(emp2.getIdDependencia());
        empleado2.setIdUsuario(emp2.getIdUsuario());
        empleado2.setNombres(emp2.getNombres());
        empleado2.setNroIdentificacion(emp2.getNroIdentificacion());
        empleado2.setTipoIdentificacion(emp2.getTipoIdentificacion());
        //////////////////////////////////////////////////////////////
        resultado.setEmpleadox(empleado2);


        sup.setId(new BigDecimal(sesion.getAttribute("codSupervisor").toString()));
        resultado.setSupervisor(sup);
        /////////////////////////////////////////////////////////////////////
        TipoEvaluacion ti = this.retornaTipoEvaluacion("Reposo");
        resultado.setTipoEvaluacionx(ti);
        ////////////////////////////////////////////////////////////////
        resultado.setObservacion("");
        //////////////////////////////////////////////////////////////////
        resultado.getPlanteamientos().clear();
        /////////////////////////////////////////////////////////////////
        Evaluacion eva = (Evaluacion) sesion.getAttribute("formatoEvaluacion");
        resultado.setEvaluacionx(eva);

        this.registrarEvaluacionReposo(resultado);

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEvaluaciones.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }

    }

    public void cancelarreposo() {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();




        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEmpleados.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }

    public void prepareEvaluacion() {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);
        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionado");
        if (tiposel.compareTo("Evaluacion") == 0) {


            Evaluacion ev = (Evaluacion) sesion.getAttribute("formatoEvaluacion");

            List<NodoP> factores;

            List<Planteamiento> planteamientos = getFormatoEvaluacion(ev.getId().toString());
            factores = new ArrayList<NodoP>();


            Iterator it = planteamientos.iterator();

            while (it.hasNext()) {

                Planteamiento pla = (Planteamiento) it.next();



                if (factores.isEmpty()) {
                    NodoP np = new NodoP();
                    np.setIdFactor(pla.getIdPresentacionFactor().getId().toString());
                    np.setDescFactor(pla.getIdPresentacionFactor().getIdFactor().getDescripcion());
                    np.setNombre(pla.getIdPresentacionFactor().getIdFactor().getNombre());
                    np.getListaPlan().add(pla);
                    factores.add(np);
                } else {

                    Iterator itfact = factores.iterator();

                    int sw = 0;
                    while (itfact.hasNext()) {
                        NodoP nlista = (NodoP) itfact.next();

                        if (nlista.getIdFactor().compareTo(pla.getIdPresentacionFactor().getId().toString()) == 0) {
                            nlista.getListaPlan().add(pla);
                            //factores.add(nlista);
                            sw = 1;
                        }
                    }
                    if (sw == 0) {
                        NodoP np = new NodoP();
                        np.setIdFactor(pla.getIdPresentacionFactor().getId().toString());
                        np.setDescFactor(pla.getIdPresentacionFactor().getIdFactor().getDescripcion());
                        np.getListaPlan().add(pla);
                        np.setNombre(pla.getIdPresentacionFactor().getIdFactor().getNombre());
                        factores.add(np);
                    }
                }
            }

            sesion.setAttribute("listafactores", factores);
            sesion.setAttribute("teval", "Evaluacion");




            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/formatoEvaluacion.xhtml");

            } catch (IOException e) {
                //  LOG.error("Redirect failed");
                throw new FacesException(e);
            }
        }
        if (tiposel.compareTo("Vacaciones") == 0) {


            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/vacaciones.xhtml");

            } catch (IOException e) {
                //  LOG.error("Redirect failed");
                throw new FacesException(e);
            }
        }
        if (tiposel.compareTo("Reposo") == 0) {


            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/reposo.xhtml");

            } catch (IOException e) {
                //  LOG.error("Redirect failed");
                throw new FacesException(e);
            }
        }


    }

    public void setearEmpleado() {

        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionado");



    }

    private java.util.List<com.pangea.evap.services.Planteamiento> getFormatoEvaluacion(java.lang.String idEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getFormatoEvaluacion(idEvaluacion);
    }

    private TipoEvaluacion retornaTipoEvaluacion(java.lang.String nombretipo) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaTipoEvaluacion(nombretipo);
    }

    private String registrarEvaluacionVacaciones(com.pangea.evap.services.ResultadoEvaluacionTO resultado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.registrarEvaluacionVacaciones(resultado);
    }

    private String registrarEvaluacionReposo(com.pangea.evap.services.ResultadoEvaluacionTO resultado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.registrarEvaluacionReposo(resultado);
    }
}
