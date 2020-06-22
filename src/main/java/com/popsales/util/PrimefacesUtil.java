/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.util;

import java.util.Collection;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Renato
 */
public class PrimefacesUtil {

    public static void Execute(String exp) {
        PrimeFaces.current().executeScript(exp);
    }

    public static void Update(String exp) {
        PrimeFaces.current().ajax().update(exp);
    }

    public static void Update(Collection<String> exp) {
        PrimeFaces.current().ajax().update(exp);
    }

}
