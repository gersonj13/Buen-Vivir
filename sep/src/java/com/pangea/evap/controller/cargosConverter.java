package com.pangea.evap.controller;


import com.pangea.evap.services.Cargo;
import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "cargosConverter")
public class cargosConverter implements Converter, Serializable {

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
        if (arg2 instanceof Cargo) {
            str = "" + ((Cargo) arg2).getId();
        }
        return str;
    }
}
