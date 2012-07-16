package com.pangea.evap.controller.presentacionesformato;


import com.pangea.evap.services.PresentacionFactor;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

@FacesConverter(value = "PrimeFacesPickListConverter")
public class pickListConverter implements Converter {

 
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
 
        return getObjectFromUIPickListComponent(component,value);
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        String string;
        if(object == null){
            string="";
        }else{
            try{
                string = String.valueOf(((PresentacionFactor)object).getId().toString());
            }catch(ClassCastException cce){
                throw new ConverterException();
            }
        }
        return string;
    }
 
    @SuppressWarnings("unchecked")
    private PresentacionFactor getObjectFromUIPickListComponent(UIComponent component, String value) {
        final DualListModel<PresentacionFactor> dualList;
        try{
            dualList = (DualListModel<PresentacionFactor>) ((PickList)component).getValue();
            PresentacionFactor pre = getObjectFromList(dualList.getSource(),Integer.valueOf(value));
            if(pre==null){
                pre = getObjectFromList(dualList.getTarget(),Integer.valueOf(value));
            }
             
            return pre;
        }catch(ClassCastException cce){
            throw new ConverterException();
        }catch(NumberFormatException nfe){
            throw new ConverterException();
        }
    }
 
    private PresentacionFactor getObjectFromList(final List<?> list, final Integer identifier) {
        for(final Object object:list){
            final PresentacionFactor pre = (PresentacionFactor) object;
            if(pre.getId().toString().equals(String.valueOf(identifier))){
                return pre;
            }
        }
        return null;
    }
}