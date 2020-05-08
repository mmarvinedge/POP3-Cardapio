/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 *
 * @author Renato
 */
public class Bairro {

    private String bairro;
    private BigDecimal taxa;
    
    private String html;

    public Bairro() {
    }

    public Bairro(String bairro) {
        this.bairro = bairro;
    }

    public Bairro(String bairro, BigDecimal taxa) {
        this.bairro = bairro;
        this.taxa = taxa;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

   
    
    

}
