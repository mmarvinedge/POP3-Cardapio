/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Renato
 */
public class OUtils {

    public static String formataData(Date data, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
        if (data != null) {
            return df.format(data);
        } else {
            return "";
        }
    }
    
    public static String formataNinePhone(String tel){
          String fone = tel;
        //String fone = "553438233745";
        String whitou55 = fone.subSequence(2, fone.length()).toString();
        String ddd = whitou55.subSequence(0, 2).toString();
        String whitoudd = whitou55.substring(2, whitou55.length()).toString();
        
        Boolean isCelular = whitoudd.startsWith("9") || whitoudd.startsWith("8");
        if(isCelular){
            return "("+ddd+")9"+whitoudd.substring(0, 4)+"-"+whitoudd.substring(4, 8);
        }else{
            return "("+ddd+")"+whitoudd.substring(0, 4)+"-"+whitoudd.substring(4, 8);
            
        }
        
    }
    
    public static String formatarMoeda(Double val) {
        if (val == null) {
            return "R$ 0,00";
        }
        try {

            NumberFormat nF = NumberFormat.getCurrencyInstance();
            nF.setCurrency(Currency.getInstance(new Locale("pt", "BR")));
            return nF.format(val).replace("BRL", "R$ ");
        } catch (Exception e) {
            System.out.println("ERRO AO TENTAR FORMATAR: " + val);
            return "R$ " + val;
        }
    }
}
