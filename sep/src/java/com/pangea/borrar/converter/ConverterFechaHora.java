
package com.pangea.borrar.converter;

import com.fenix.util.jbVarios;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author psistemas
 */

@FacesConverter(value="FechaHoraConverter")
public class ConverterFechaHora implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        jbVarios jvar=new jbVarios();
        return jvar.stringToFecha(value, "dd/MM/yyyy hh:mm:ss");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {        
        if (value instanceof Date){
            Date fecha=(Date) value;
            jbVarios jvar= new jbVarios();
            return jvar.fechaToString(fecha, "dd/MM/yyyy hh:mm:ss");
        }else{
            throw new IllegalArgumentException("No se puede convertir Objetos que no sean fechas en FechaConverters");
        }
    }
}
