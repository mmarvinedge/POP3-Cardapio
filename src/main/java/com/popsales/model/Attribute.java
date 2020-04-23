/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PICHAU
 */
public class Attribute implements Serializable {

    private String sku;
    private String name;
    private String description;
    private String type;
    private Boolean highestPrice;
    private String quantityType;
    private Integer quantity;
    private BigDecimal price;
    private String rule;
    private List<AttributeValue> values = new ArrayList();

    private Boolean travado = false;

    public Attribute() {
    }

    public Attribute(Attribute at) {
        this.sku = at.getSku();
        this.name = at.getName();
        this.description = at.getDescription();
        this.type = at.getType();
        this.highestPrice = at.getHighestPrice();
        this.quantityType = at.getQuantityType();
        this.quantity = at.getQuantity();
        this.price = at.getPrice();
        this.rule = at.getRule();
        this.values = at.getValues();
        if (values != null) {
           List<AttributeValue> vals = new ArrayList();
            for (AttributeValue value : values) {
                vals.add(new AttributeValue(value));
            }
            values = vals;
        }

    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(Boolean highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<AttributeValue> getValues() {
        values.forEach(c -> {
            c.setAttribute_sku(c.getId());
        });
        return values;
    }

    public void setValues(List<AttributeValue> values) {
        this.values = values;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Boolean getTravado() {
        return travado;
    }

    public void setTravado(Boolean travado) {
        this.travado = travado;
    }

    @Override
    public String toString() {
        return "Attribute{" + "sku=" + sku + ", name=" + name + ", description=" + description + ", type=" + type + ", highestPrice=" + highestPrice + ", quantityType=" + quantityType + ", quantity=" + quantity + ", price=" + price + '}';
    }

}
