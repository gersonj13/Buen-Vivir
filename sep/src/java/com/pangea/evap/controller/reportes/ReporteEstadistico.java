package com.pangea.evap.controller.reportes;

import com.pangea.evap.services.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import org.primefaces.event.FlowEvent;

 
// @Author Ing. Gerson Ramirez 
@ManagedBean(name = "reporteEstadistico")
@SessionScoped
public class ReporteEstadistico {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private Evaluacion current;
    private List<Evaluacion> evaluaciones;
    private List<Dependencia> dependencias;
    private List<Empleado> empleados;
    private List<Dependencia> auxdependencias;
    private Date FechaiEvaluacion, FechafEvaluacion;
    private Date Fechaie, Fechafe;
    private BigDecimal idEvaluacion;
    private int paso;
    private String Categorias;
    private String series;
    private String seriesPorcentaje;
    private String CategoriasPromedio;
    private String CategoriasPorcentaje;
    private String seriesPromedio;
    private String TipoDependencia;
    private String tipoReporte;
    private List<String> auxDependenciasSeleccionada;
    private List<String> dependenciasNombreTransformado;
    private List<String> EmpleadosNombreTransformado;
    private String Panel1;

    public String getPanel1() {
        return Panel1;
    }

    public void setPanel1(String Panel1) {
        this.Panel1 = Panel1;
    }

    public String getPanel2() {
        return Panel2;
    }

    public void setPanel2(String Panel2) {
        this.Panel2 = Panel2;
    }
    private String Panel2;

    public List<String> getEmpleadosNombreTransformado() {
        return EmpleadosNombreTransformado;
    }

    public void setEmpleadosNombreTransformado(List<String> EmpleadosNombreTransformado) {
        this.EmpleadosNombreTransformado = EmpleadosNombreTransformado;
    }
    private List<String> EvaluacionSeleccionada;
    private List<String> DependenciasSeleccionada;
    private List<String> EmpleadoSeleccionado;
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<String> getEmpleadoSeleccionado() {
        return EmpleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(List<String> EmpleadoSeleccionado) {
        this.EmpleadoSeleccionado = EmpleadoSeleccionado;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public String getCategoriasPorcentaje() {
        return CategoriasPorcentaje;
    }

    public void setCategoriasPorcentaje(String CategoriasPorcentaje) {
        this.CategoriasPorcentaje = CategoriasPorcentaje;
    }

    public String getSeriesPorcentaje() {
        return seriesPorcentaje;
    }

    public void setSeriesPorcentaje(String seriesPorcentaje) {
        this.seriesPorcentaje = seriesPorcentaje;
    }

    public String getCategoriasPromedio() {
        return CategoriasPromedio;
    }

    public void setCategoriasPromedio(String CategoriasPromedio) {
        this.CategoriasPromedio = CategoriasPromedio;
    }

    public String getSeriesPromedio() {
        return seriesPromedio;
    }

    public void setSeriesPromedio(String seriesPromedio) {
        this.seriesPromedio = seriesPromedio;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCategorias() {
        return Categorias;
    }

    public void setCategorias(String Categorias) {
        this.Categorias = Categorias;
    }

    public int getPaso() {
        return paso;
    }

    public void setPaso(int paso) {
        this.paso = paso;
    }

    public String getTipoDependencia() {
        return TipoDependencia;
    }

    public List<String> getDependenciasSeleccionada() {
        return DependenciasSeleccionada;
    }

    public void setDependenciasSeleccionada(List<String> DependenciasSeleccionada) {
        this.DependenciasSeleccionada = DependenciasSeleccionada;
    }

    public void setTipoDependencia(String TipoDependencia) {
        this.TipoDependencia = TipoDependencia;
    }

    public List<String> getEvaluacionSeleccionada() {
        return EvaluacionSeleccionada;
    }

    public void setEvaluacionSeleccionada(List<String> EvaluacionSeleccionada) {
        this.EvaluacionSeleccionada = EvaluacionSeleccionada;
    }

    public BigDecimal getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(BigDecimal idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

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

    public Date getFechafEvaluacion() {
        return FechafEvaluacion;
    }

    public void setFechafEvaluacion(Date FechafEvaluacion) {
        this.FechafEvaluacion = FechafEvaluacion;
    }

    public Date getFechaiEvaluacion() {
        return FechaiEvaluacion;
    }

    public void setFechaiEvaluacion(Date FechaiEvaluacion) {
        this.FechaiEvaluacion = FechaiEvaluacion;
    }

    public List<Dependencia> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<Dependencia> dependencias) {
        this.dependencias = dependencias;
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

    public ReporteEstadistico() throws Exception {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        if (sesion.getAttribute("evaluacionesEstadistico").toString().compareTo("nada") != 0) {
            evaluaciones = (List<Evaluacion>) sesion.getAttribute("evaluacionesEstadistico");

        }


    }

    public void filtrarEvaluaciones() throws DatatypeConfigurationException, ParseException_Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        HttpSession sesion = (HttpSession) ec.getSession(true);

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(FechaiEvaluacion);
        XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        GregorianCalendar c2 = new GregorianCalendar();

        c2.setTime(FechafEvaluacion);
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);

        evaluaciones = this.evaluacionFecha(date1, date2);
        EvaluacionSeleccionada = new ArrayList<String>();

        sesion.setAttribute("evaluacionesEstadistico", evaluaciones);


    }

    public void recargarDependencias(ValueChangeEvent evt) {

        String seleccionado = (String) evt.getNewValue();

        dependencias = retornaDependenciasNivel(seleccionado);

    }

    public void seleccionartodasd() {
        DependenciasSeleccionada = new ArrayList<String>();
        for (int i = 0; i < dependencias.size(); i++) {
            DependenciasSeleccionada.add(dependencias.get(i).getDescripcion());
        }
    }

    public void seleccionartodose() {
        EmpleadoSeleccionado = new ArrayList<String>();
        for (int i = 0; i < empleados.size(); i++) {
            EmpleadoSeleccionado.add(empleados.get(i).getId().toString());
        }
    }

    public void desseleccionartodose() {
        EmpleadoSeleccionado = null;
        EmpleadoSeleccionado = new ArrayList<String>();
    }

    public void desseleccionatodasd() {
        DependenciasSeleccionada = null;
        DependenciasSeleccionada = new ArrayList<String>();
    }

    public void seleccionartodase() {
        EvaluacionSeleccionada = new ArrayList<String>();
        for (int i = 0; i < evaluaciones.size(); i++) {
            EvaluacionSeleccionada.add(evaluaciones.get(i).getNombreEvaluacion());
        }
    }

    public void desseleccionatodase() {
        EvaluacionSeleccionada = null;
        EvaluacionSeleccionada = new ArrayList<String>();
    }

    public void resetFiltro() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);


        evaluaciones = this.retornaEvaluaciones();
        EvaluacionSeleccionada = new ArrayList<String>();


        sesion.setAttribute("evaluacionesEstadistico", evaluaciones);
    }

    public void muestraMensajeA() {

        if (mensaje.compareTo("exito") != 0) {
            mostrarMensaje(1, "Error", mensaje);
        }
    }

    public void muestraMensajeS() {

        mensaje = "exito";
        if (paso == 0 && EvaluacionSeleccionada.isEmpty()) {
            mensaje = "Debe Seleccionar al menos una Evaluacion";
        }
        if (paso == 1 && DependenciasSeleccionada.isEmpty()) {
            mensaje = "Debe Seleccionar al menos una unidad de la organizacion";
        }
        if (paso == 1 && DependenciasSeleccionada.size() > 0) {
            if (TipoDependencia.compareTo("1") == 0) {

                auxdependencias = dependencias;
                auxDependenciasSeleccionada = DependenciasSeleccionada;
                dependencias = retornaDependenciasNivel("2");
                seleccionartodasd();

            }



            empleados = this.retornaEmpleadosGrupodependencias(TipoDependencia, DependenciasSeleccionada);
            EmpleadoSeleccionado = new ArrayList<String>();

        }


        if (mensaje.compareTo("exito") != 0) {
            mostrarMensaje(1, "Error", mensaje);
        }

    }

    public String onFlowProcess(FlowEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);
        if (event.getOldStep().compareTo("Organizacion") == 0 && DependenciasSeleccionada.isEmpty() && event.getNewStep().compareTo("Evaluaciones") != 0) {
            return "Organizacion";
        }
        if (event.getOldStep().compareTo("Evaluaciones") == 0 && EvaluacionSeleccionada.isEmpty()) {
            return "Evaluaciones";
        }
        if (event.getNewStep().compareTo("Organizacion") == 0) {
            if (event.getOldStep().compareTo("Evaluaciones") == 0) {
                sesion.setAttribute("espr", EvaluacionSeleccionada);
            } else {
                EvaluacionSeleccionada = (List<String>) sesion.getAttribute("espr");
                DependenciasSeleccionada = (List<String>) sesion.getAttribute("ospr");
                dependencias = (List<Dependencia>) sesion.getAttribute("ospry");
                TipoDependencia = (String) sesion.getAttribute("osprx");
            }
            paso = 1;
        }
        if (event.getNewStep().compareTo("Empleado") == 0) {
            sesion.setAttribute("ospr", DependenciasSeleccionada);
            sesion.setAttribute("ospry", dependencias);
            sesion.setAttribute("osprx", TipoDependencia);
            paso = 2;
        }
        if (event.getNewStep().compareTo("Evaluaciones") == 0) {
            EvaluacionSeleccionada = (List<String>) sesion.getAttribute("espr");
            mensaje = "exito";
            paso = 0;
        }
        return event.getNewStep();

    }

    public void GenerarGraficaEmpleados() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        List<StringArray> resultado, resultado2, resultado3;
        EvaluacionSeleccionada = (List<String>) sesion.getAttribute("espr");
        Panel1 = "Historico Puntos";
        Panel2 = "Promedio Puntos";

        if (EmpleadoSeleccionado.size() > 0) {


            EmpleadosNombreTransformado = transformaNombreID();
            resultado = this.pEAEmpleados(TipoDependencia, EvaluacionSeleccionada, EmpleadoSeleccionado);
            Categorias = "";
            Categorias += "\'" + EmpleadosNombreTransformado.get(0) + "\'";
            for (int i = 1; i < EmpleadosNombreTransformado.size(); i++) {

                Categorias += "," + "\'" + EmpleadosNombreTransformado.get(i) + "\'";

            }
            series = "[ ";
            series += "{ name: " + "\'" + EvaluacionSeleccionada.get(0) + "\'" + "," + " data: [";
            series += resultado.get(0).getItem().get(0);
            for (int i = 1; i < resultado.size(); i++) {
                StringArray s = resultado.get(i);
                series += " , " + s.getItem().get(0);

            }
            series += " ] }";

            for (int i = 1; i < EvaluacionSeleccionada.size(); i++) {

                series += ", { name: " + "\'" + EvaluacionSeleccionada.get(i) + "\'" + "," + " data: [";
                series += resultado.get(0).getItem().get(i);
                for (int j = 1; j < resultado.size(); j++) {
                    StringArray s = resultado.get(j);
                    series += " , " + s.getItem().get(i);

                }
                series += " ] }";

            }

            series += " ]";

            ////////////////////////////////////////////////////GRAFICA PROMEDIO GERENCIA
            resultado2 = this.pEAEmpleadosP(TipoDependencia, EvaluacionSeleccionada, EmpleadoSeleccionado);
            CategoriasPromedio = "";
            CategoriasPromedio += "\'" + EvaluacionSeleccionada.get(0) + "\'";
            for (int i = 1; i < EvaluacionSeleccionada.size(); i++) {

                CategoriasPromedio += "," + "\'" + EvaluacionSeleccionada.get(i) + "\'";

            }
            seriesPromedio = "[ ";
            seriesPromedio += "{ name: " + "\'" + EmpleadosNombreTransformado.get(0) + "\'" + "," + " data: [";
            seriesPromedio += resultado2.get(0).getItem().get(0);
            for (int i = 1; i < resultado2.get(0).getItem().size(); i++) {

                seriesPromedio += " , " + resultado2.get(0).getItem().get(i);

            }
            seriesPromedio += " ] }";

            for (int i = 1; i < EmpleadosNombreTransformado.size(); i++) {

                seriesPromedio += ", { name: " + "\'" + EmpleadosNombreTransformado.get(i) + "\'" + "," + " data: [";
                seriesPromedio += resultado2.get(i).getItem().get(0);
                for (int j = 1; j < resultado2.get(i).getItem().size(); j++) {

                    seriesPromedio += " , " + resultado2.get(i).getItem().get(j);

                }
                seriesPromedio += " ] }";

            }

            seriesPromedio += " ]";
            ///////////////////////////////// GRAFICA PORCENTAJE GERENCIA
            resultado3 = this.pEPE(TipoDependencia, EvaluacionSeleccionada, EmpleadoSeleccionado);
            CategoriasPorcentaje = "";
            CategoriasPorcentaje += "\'" + EmpleadosNombreTransformado.get(0) + "\'";
            for (int i = 1; i < EmpleadosNombreTransformado.size(); i++) {

                CategoriasPorcentaje += "," + "\'" + EmpleadosNombreTransformado.get(i) + "\'";

            }
            seriesPorcentaje = "[ ";
            seriesPorcentaje += "{ name: " + "\'" + "Promedio" + "\'" + "," + " data: [";
            String cal = resultado3.get(0).getItem().get(0);
            seriesPorcentaje += cal;
            for (int i = 1; i < resultado3.size(); i++) {
                StringArray s = resultado3.get(i);
                String calculo = s.getItem().get(0);

                seriesPorcentaje += " , " + calculo;

            }
            seriesPorcentaje += " ] }";



            seriesPorcentaje += " ]";



        }
    }

    public void GenerarGraficaOrganizacion() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        List<StringArray> resultado, resultado2, resultado3;
        EvaluacionSeleccionada = (List<String>) sesion.getAttribute("espr");
        Panel1 = "Promedio Puntos";
        Panel2 = "Porcentaje Evaluados";
        if (DependenciasSeleccionada.size() > 0) {

            //Verifico si es de la organizacion. en ese caso recargo todas las gerencias.
            if (TipoDependencia.compareTo("1") == 0) {

                auxdependencias = dependencias;
                auxDependenciasSeleccionada = DependenciasSeleccionada;
                dependencias = retornaDependenciasNivel("2");
                seleccionartodasd();

            }


            dependenciasNombreTransformado = transformaNombreDescripcion();


            ///////////////////////////////// GRAFICA ACUMULADA GERENCIA
            resultado = this.puntosEvaluacionAcumulados(TipoDependencia, EvaluacionSeleccionada, DependenciasSeleccionada);
            Categorias = "";
            Categorias += "\'" + dependenciasNombreTransformado.get(0) + "\'";
            for (int i = 1; i < dependenciasNombreTransformado.size(); i++) {

                Categorias += "," + "\'" + dependenciasNombreTransformado.get(i) + "\'";

            }
            series = "[ ";
            series += "{ name: " + "\'" + EvaluacionSeleccionada.get(0) + "\'" + "," + " data: [";
            series += resultado.get(0).getItem().get(0);
            for (int i = 1; i < resultado.size(); i++) {
                StringArray s = resultado.get(i);
                series += " , " + s.getItem().get(0);

            }
            series += " ] }";

            for (int i = 1; i < EvaluacionSeleccionada.size(); i++) {

                series += ", { name: " + "\'" + EvaluacionSeleccionada.get(i) + "\'" + "," + " data: [";
                series += resultado.get(0).getItem().get(i);
                for (int j = 1; j < resultado.size(); j++) {
                    StringArray s = resultado.get(j);
                    series += " , " + s.getItem().get(i);

                }
                series += " ] }";

            }

            series += " ]";
////////////////////////////////////////////////////GRAFICA PROMEDIO GERENCIA
            resultado2 = this.puntosEvaluacionPromedios(TipoDependencia, EvaluacionSeleccionada, DependenciasSeleccionada);
            CategoriasPromedio = "";
            CategoriasPromedio += "\'" + EvaluacionSeleccionada.get(0) + "\'";
            for (int i = 1; i < EvaluacionSeleccionada.size(); i++) {

                CategoriasPromedio += "," + "\'" + EvaluacionSeleccionada.get(i) + "\'";

            }
            seriesPromedio = "[ ";
            seriesPromedio += "{ name: " + "\'" + dependenciasNombreTransformado.get(0) + "\'" + "," + " data: [";
            seriesPromedio += resultado2.get(0).getItem().get(0);
            for (int i = 1; i < resultado2.get(0).getItem().size(); i++) {

                seriesPromedio += " , " + resultado2.get(0).getItem().get(i);

            }
            seriesPromedio += " ] }";

            for (int i = 1; i < dependenciasNombreTransformado.size(); i++) {

                seriesPromedio += ", { name: " + "\'" + dependenciasNombreTransformado.get(i) + "\'" + "," + " data: [";
                seriesPromedio += resultado2.get(i).getItem().get(0);
                for (int j = 1; j < resultado2.get(i).getItem().size(); j++) {

                    seriesPromedio += " , " + resultado2.get(i).getItem().get(j);

                }
                seriesPromedio += " ] }";

            }

            seriesPromedio += " ]";
            ///////////////////////////////// GRAFICA PORCENTAJE GERENCIA
            resultado3 = this.puntosEvaluacionCantidad(TipoDependencia, EvaluacionSeleccionada, DependenciasSeleccionada);
            CategoriasPorcentaje = "";
            CategoriasPorcentaje += "\'" + dependenciasNombreTransformado.get(0) + "\'";
            for (int i = 1; i < dependenciasNombreTransformado.size(); i++) {

                CategoriasPorcentaje += "," + "\'" + dependenciasNombreTransformado.get(i) + "\'";

            }
            seriesPorcentaje = "[ ";
            seriesPorcentaje += "{ name: " + "\'" + EvaluacionSeleccionada.get(0) + "\'" + "," + " data: [";
            String cal = resultado3.get(0).getItem().get(0);
            int auxvp = Integer.parseInt(cal.split("-")[0]);
            int auxvg = Integer.parseInt(cal.split("-")[1]);
            double auxporcentaje = auxvp * 100 / auxvg;
            seriesPorcentaje += String.valueOf(auxporcentaje);
            for (int i = 1; i < resultado3.size(); i++) {
                StringArray s = resultado3.get(i);
                String calculo = s.getItem().get(0);
                int vp = Integer.parseInt(calculo.split("-")[0]);
                int vg = Integer.parseInt(calculo.split("-")[1]);
                double porcentaje = vp * 100 / vg;
                seriesPorcentaje += " , " + String.valueOf(porcentaje);

            }
            seriesPorcentaje += " ] }";

            for (int i = 1; i < EvaluacionSeleccionada.size(); i++) {

                seriesPorcentaje += ", { name: " + "\'" + EvaluacionSeleccionada.get(i) + "\'" + "," + " data: [";
                cal = resultado3.get(0).getItem().get(i);
                auxvp = Integer.parseInt(cal.split("-")[0]);
                auxvg = Integer.parseInt(cal.split("-")[1]);
                auxporcentaje = auxvp * 100 / auxvg;
                seriesPorcentaje += auxporcentaje;
                for (int j = 1; j < resultado3.size(); j++) {
                    StringArray s = resultado3.get(j);
                    String calculo = s.getItem().get(i);
                    int vp = Integer.parseInt(calculo.split("-")[0]);
                    int vg = Integer.parseInt(calculo.split("-")[1]);
                    double porcentaje = vp * 100 / vg;
                    seriesPorcentaje += " , " + String.valueOf(porcentaje);

                }
                seriesPorcentaje += " ] }";

            }

            seriesPorcentaje += " ]";
            if (TipoDependencia.compareTo("1") == 0) {

                dependencias = auxdependencias;
                DependenciasSeleccionada = auxDependenciasSeleccionada;


            }
        }
    }

    public List<String> transformaNombreDescripcion() {
        List<String> depenNombre = new ArrayList<String>();

        for (int i = 0; i < DependenciasSeleccionada.size(); i++) {
            boolean enc = false;
            for (int j = 0; j < dependencias.size() && !enc; j++) {

                if (dependencias.get(j).getDescripcion().compareTo(DependenciasSeleccionada.get(i).toString()) == 0) {
                    depenNombre.add(dependencias.get(j).getNombre());
                    enc = true;
                }
            }


        }
        return depenNombre;

    }

    public List<String> transformaNombreID() {
        List<String> empNombre = new ArrayList<String>();

        for (int i = 0; i < EmpleadoSeleccionado.size(); i++) {
            boolean enc = false;
            for (int j = 0; j < empleados.size() && !enc; j++) {

                if (empleados.get(j).getId().toString().compareTo(EmpleadoSeleccionado.get(i).toString()) == 0) {
                    empNombre.add(empleados.get(j).getNombres() + " " + empleados.get(j).getApellidos());
                    enc = true;
                }
            }


        }
        return empNombre;

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

    public void iniciar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);


        evaluaciones = this.retornaEvaluaciones();


        sesion.setAttribute("evaluacionesEstadistico", evaluaciones);

        FechaiEvaluacion = new Date();
        FechafEvaluacion = new Date();
        DependenciasSeleccionada = new ArrayList<String>();
        EvaluacionSeleccionada = new ArrayList<String>();
        EmpleadoSeleccionado = new ArrayList<String>();
        Categorias = "";
        series = "";
        seriesPorcentaje = "";
        CategoriasPorcentaje = "";
        CategoriasPromedio = "";
        seriesPromedio = "";
        TipoDependencia = "";
        paso = 0;
        mensaje = "exito";

    }

    public String prepareList() {
        current = new Evaluacion();
        
        return "List";
    }

    public void inicializar() {
        current = new Evaluacion();
    }

    public void TransformarFechas1(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechaie = xcal.toGregorianCalendar().getTime();

    }

    public void TransformarFechas2(XMLGregorianCalendar cal) {

        XMLGregorianCalendar xcal = cal;
        Fechafe = xcal.toGregorianCalendar().getTime();

    }

    private java.util.List<com.pangea.evap.services.Evaluacion> retornaEvaluaciones() {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEvaluaciones();
    }

    private java.util.List<com.pangea.evap.services.Evaluacion> evaluacionFecha(javax.xml.datatype.XMLGregorianCalendar fi, javax.xml.datatype.XMLGregorianCalendar ff) throws ParseException_Exception {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.evaluacionFecha(fi, ff);
    }

    private java.util.List<com.pangea.evap.services.Dependencia> retornaDependenciasNivel(java.lang.String nivel) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaDependenciasNivel(nivel);
    }

    private java.util.List<com.pangea.evap.services.StringArray> puntosEvaluacionAcumulados(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.puntosEvaluacionAcumulados(nivel, evaluacion, dependencia);
    }

    private java.util.List<com.pangea.evap.services.StringArray> puntosEvaluacionPromedios(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.puntosEvaluacionPromedios(nivel, evaluacion, dependencia);
    }

    private java.util.List<com.pangea.evap.services.StringArray> puntosEvaluacionCantidad(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.puntosEvaluacionCantidad(nivel, evaluacion, dependencia);
    }

    private java.util.List<com.pangea.evap.services.Empleado> retornaEmpleadosGrupodependencias(java.lang.String nivel, java.util.List<java.lang.String> dependencia) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.retornaEmpleadosGrupodependencias(nivel, dependencia);
    }

   
    private java.util.List<com.pangea.evap.services.StringArray> pEAEmpleados(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> empleados) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.pEAEmpleados(nivel, evaluacion, empleados);
    }

    private java.util.List<com.pangea.evap.services.StringArray> pEAEmpleadosP(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> empleados) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.pEAEmpleadosP(nivel, evaluacion, empleados);
    }

    private java.util.List<com.pangea.evap.services.StringArray> pEPE(java.lang.String nivel, java.util.List<java.lang.String> evaluacion, java.util.List<java.lang.String> empleados) {
        com.pangea.evap.services.GestionEvaluacion port = service.getGestionEvaluacionPort();
        return port.pEPE(nivel, evaluacion, empleados);
    }
}
