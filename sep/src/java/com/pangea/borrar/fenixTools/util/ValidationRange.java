/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangea.borrar.fenixTools.util;

import javax.faces.application.FacesMessage;
import javax.faces.validator.Validator;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author psistemas
 */
@FacesValidator(value="ValidarRango")
public class ValidationRange implements Validator {

   /**
    * Method validate
    */
   public void validate(FacesContext context,
           UIComponent component,
           Object object)
           throws ValidatorException {

      String var = object.toString();

      if (var.length() < 3) {
         throw new ValidatorException(
                 new FacesMessage("esto no va", null));
      }
   }
}
