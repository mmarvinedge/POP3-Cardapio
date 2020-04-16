/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

/**
 *
 * @author PICHAU
 */
public enum CATEGORY_ENUM { 

    LUNCH("Almoço"),
    SNACK("Lanche"),
    ICE("Sorvete"),
    DRINKS("Bebidas");

    private final String descrition;

    CATEGORY_ENUM(String descrition) {
        this.descrition = descrition;
    }
    
    
}
