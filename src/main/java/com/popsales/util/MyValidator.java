/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.event.Rewrite;
import org.ocpsoft.rewrite.param.Validator;

/**
 *
 * @author Renato
 */
@FacesValidator("com.validators.MyValidator")
public class MyValidator implements Validator{

  public void validate(FacesContext ct, UIComponent co, Object obj) throws ValidatorException { 
    if(!continueValidation()){
      return;
    }
    // validation process
  }

protected boolean continueValidation() {
    String skipValidator= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("skipValidator");
    if (skipValidator != null && skipValidator.equalsIgnoreCase("true")) {
      return false;
    }
    return true;
  } 

    @Override
    public boolean isValid(Rewrite rwrt, EvaluationContext ec, Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}