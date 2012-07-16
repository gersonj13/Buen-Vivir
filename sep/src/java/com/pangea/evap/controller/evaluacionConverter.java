package com.pangea.evap.controller;


import com.pangea.evap.services.Evaluacion;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "evaluacionConverter")
public class evaluacionConverter implements Converter, Serializable {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String string) {
        if (string.isEmpty()) {
            return null;
        }

        return string;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String str = "";
        if (arg2 instanceof Evaluacion) {
            str = "" + ((Evaluacion) arg2).getId();
        }
        return str;
    }

   
}