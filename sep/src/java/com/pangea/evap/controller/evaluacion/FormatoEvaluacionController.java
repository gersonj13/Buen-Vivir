package com.pangea.evap.controller.evaluacion;

import com.pangea.evap.controller.util.NodoP;
import com.pangea.evap.services.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "formatoEvaluacionController")
@RequestScoped
public class FormatoEvaluacionController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    List<NodoP> factores;
    Empleado emp, supervisor;
    List<Planteamiento> planteamiento_seleccionado;
    boolean nulo;
    private List<com.pangea.evap.services.Planteamiento> planteamientos;
    private String text = "";
    private String lista = "";
    private boolean visibilidad;
    private List<String[]> resultadospp;
    private List<String[]> resultadospm;

    public boolean isVisibilidad() {
        return visibilidad;
    }

    public void setPlanteamientos(List<Planteamiento> planteamientos) {
        this.planteamientos = planteamientos;
    }

    public void setVisibilidad(boolean visibilidad) {
        this.visibilidad = visibilidad;
    }

    public boolean isNulo() {
        return nulo;
    }

    public void setNulo(boolean nulo) {
        this.nulo = nulo;
    }

    public List<Planteamiento> getPlanteamiento_seleccionado() {
        return planteamiento_seleccionado;
    }

    public void setPlanteamiento_seleccionado(List<Planteamiento> planteamiento_seleccionado) {
        this.planteamiento_seleccionado = planteamiento_seleccionado;
    }

    public Empleado getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Empleado supervisor) {
        this.supervisor = supervisor;
    }

    public List<String[]> getResultadospm() {
        return resultadospm;
    }

    public void setResultadospm(List<String[]> resultadospm) {
        this.resultadospm = resultadospm;
    }

    public List<String[]> getResultadospp() {
        return resultadospp;
    }

    public void setResultadospp(List<String[]> resultadospp) {
        this.resultadospp = resultadospp;
    }

    public Empleado getEmp() {
        return emp;
    }

    public void setEmp(Empleado emp) {
        this.emp = emp;
    }

    public FormatoEvaluacionController() {

        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);

        factores = (List<NodoP>) sesion.getAttribute("listafactores");
        if (sesion.getAttribute("pSel") != null) {
            planteamiento_seleccionado = (List<Planteamiento>) sesion.getAttribute("pSel");
        }
        if (sesion.getAttribute("fSel") != null) {
            resultadospm = (List<String[]>) sesion.getAttribute("fSel");
        }
        visibilidad = false;

    }

    public void setearEmpleado() {

        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        emp = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionado");

        visibilidad = false;

    }

    public void evaluar() {
        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        visibilidad = false;


        nulo = false;
        resultadospp = new ArrayList<String[]>();
        resultadospm = new ArrayList<String[]>();

        for (int i = 0; i < factores.size(); i++) {
            String[] res;

            String y = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("group" + factores.get(i).getIdFactor());
            res = y.split("-");
            resultadospp.add(res);

        }
        sesion.setAttribute("fSel", resultadospm);
        planteamiento_seleccionado = new ArrayList<Planteamiento>();

        for (int i = 0; i < resultadospp.size() && !nulo; i++) {
            String[] par;
            par = resultadospp.get(i);

            int fac = Integer.parseInt(par[0]);
            int fs = 0;
            for (int jx = 0; jx < factores.size(); jx++) {
                if (factores.get(jx).getIdFactor().compareTo(String.valueOf(fac)) == 0) {
                    fs = jx;
                }
            }


            if (par[1].compareTo("n") == 0) {
                nulo = true;
            }

            if (!nulo) {

                int argumento = 0;
                for (int ix = 0; ix < factores.get(fs).getListaPlan().size(); ix++) {
                    if (factores.get(fs).getListaPlan().get(ix).getId().toString().compareTo(par[1]) == 0) {
                        argumento = ix;
                    }
                }


                planteamiento_seleccionado.add(factores.get(fs).getListaPlan().get(argumento));
                String pfp = String.valueOf(fs) + "-" + String.valueOf(argumento);
                resultadospm.add(pfp.split("-"));
            }
        }
        if (!nulo) {
            sesion.setAttribute("pSel", planteamiento_seleccionado);
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
            TipoEvaluacion ti = new TipoEvaluacion();
            ti.setId(new BigDecimal(1));
            resultado.setTipoEvaluacionx(ti);
            ////////////////////////////////////////////////////////////////
            resultado.setObservacion(FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("observacion"));
            //////////////////////////////////////////////////////////////////
            resultado.getPlanteamientos().addAll(planteamiento_seleccionado);
            /////////////////////////////////////////////////////////////////
            Evaluacion eva = (Evaluacion) sesion.getAttribute("formatoEvaluacion");
            resultado.setEvaluacionx(eva);
            String err;
            err = this.registrarEvaluacion(resultado);
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();

            if (err.compareTo("error") == 0) {
                try {
                    ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEvaluaciones.xhtml");

                } catch (IOException e) {
                    //  LOG.error("Redirect failed");
                    throw new FacesException(e);
                }
            }


            try {
                ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/resultadoEvaluacion.xhtml");

            } catch (IOException e) {
                //  LOG.error("Redirect failed");
                throw new FacesException(e);
            }
        } else {

            visibilidad = false;
            mostrarMensaje(3, "Error", "Debe evaluar todos los Factores");

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

    public void continuar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/evaluacion/listaEvaluaciones.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }

    public int contar(int factor) {
        int acumula = 0;

        for (int i = 0; i < factor; i++) {
            acumula += factores.get(i).getListaPlan().size();
        }



        return acumula;
    }

    public void getGenerarEvaluacion() {



        ExternalContext ex = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession sesion = (HttpSession) ex.getSession(true);
        Evaluacion e = (Evaluacion) sesion.getAttribute("formatoEvaluacion");
        planteamientos = getFormatoEvaluacion("1");
        factores = new ArrayList<NodoP>();


        Iterator it = planteamientos.iterator();

        while (it.hasNext()) {

            Planteamiento pla = (Planteamiento) it.next();

            Iterator itfact = factores.iterator();

            if (factores.isEmpty()) {
                NodoP np = new NodoP();
                np.setIdFactor(pla.getIdPresentacionFactor().getId().toString());
                np.getListaPlan().add(pla);
                factores.add(np);
            } else {
                int sw = 0;
                while (itfact.hasNext()) {
                    NodoP nlista = (NodoP) itfact.next();

                    if (nlista.getIdFactor().compareTo(pla.getIdPresentacionFactor().getId().toString()) == 0) {
                        nlista.getListaPlan().add(pla);
                        factores.add(nlista);
                        sw = 1;
                    }
                }
                if (sw == 0) {
                    NodoP np = new NodoP();
                    np.setIdFactor(pla.getIdPresentacionFactor().getId().toString());
                    np.getListaPlan().add(pla);
                    factores.add(np);
                }
            }
        }

    }

    public List<Planteamiento> getPlanteamientos() {
        return planteamientos;
    }

    public List<NodoP> getFactores() {
        return factores;
    }

    public void setFactores(List<NodoP> factores) {
        this.factores = factores;
    }

    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String registrarEvaluacion(com.pangea.evap.services.ResultadoEvaluacionTO resultado) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.registrarEvaluacion(resultado);
    }

    private java.util.List<com.pangea.evap.services.Planteamiento> getFormatoEvaluacion(java.lang.String idEvaluacion) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.getFormatoEvaluacion(idEvaluacion);
    }
}
