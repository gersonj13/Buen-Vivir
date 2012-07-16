package com.pangea.borrar.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author psistemas
 */
@FacesConverter(value = "ConverterSubString")
public class ConverterSubString implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String obj = (String) value;
        if (obj.length() > 12) {
            return obj.substring(0, 12).concat("...");
        }
        return obj;
    }
}
