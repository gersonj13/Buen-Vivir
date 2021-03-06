
package com.pangea.evap.controller.gerentes;
import com.pangea.evap.services.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceRef;

// @Author Ing. Gerson Ramirez  
@ManagedBean(name = "resultadogBean")
@RequestScoped
public class ResultadogController {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/EVAServices/GestionEvaluacion.wsdl")
    private GestionEvaluacion_Service service;
    private EmpleadoTO current;
    List<EmpleadoTO> empleados;
    private boolean visibilidad;
    private List<StringArray> lista;
    private List<String[]> lista2;

    public List<String[]> getLista2() {
        return lista2;
    }

    public void setLista2(List<String[]> lista2) {
        this.lista2 = lista2;
    }

    public List<StringArray> getLista() {
        return lista;
    }

    public void setLista(List<StringArray> lista) {
        this.lista = lista;
    }

    public boolean isVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(boolean visibilidad) {
        this.visibilidad = visibilidad;
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

    public ResultadogController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sesion = (HttpSession) ec.getSession(true);

        current = (EmpleadoTO) sesion.getAttribute("empleadoSeleccionadog");

        lista = (List<StringArray>) sesion.getAttribute("listaResultado");


        lista2 = new ArrayList<String[]>();

        for (int i = 0; i < lista.size(); i++) {


            String[] ls = new String[3];
            ls[0] = lista.get(i).getItem().get(0);
            ls[1] = lista.get(i).getItem().get(1);
            ls[2] = lista.get(i).getItem().get(2);
            lista2.add(ls);
        }



    }

   

    public void continuar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        try {
            ec.redirect(ec.getRequestContextPath() + "/faces/Proyecto/gerentes/empleadosg.xhtml");

        } catch (IOException e) {
            //  LOG.error("Redirect failed");
            throw new FacesException(e);
        }
    }
}