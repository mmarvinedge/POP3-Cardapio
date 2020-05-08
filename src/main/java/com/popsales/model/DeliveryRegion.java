/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Renato
 */
public class DeliveryRegion {

    private List<String> bairros;
    private BigDecimal taxa;
    private Boolean entrega;

    public DeliveryRegion() {

    }

    public List<String> getBairros() {
        return bairros;
    }

    public void setBairros(List<String> bairros) {
        this.bairros = bairros;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public Boolean getEntrega() {
        return entrega;
    }

    public void setEntrega(Boolean entrega) {
        this.entrega = entrega;
    }

}
