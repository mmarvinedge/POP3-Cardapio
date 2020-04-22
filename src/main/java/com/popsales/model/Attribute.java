/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private List<AttributeValue> values;

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

    @Override
    public String toString() {
        return "Attribute{" + "sku=" + sku + ", name=" + name + ", description=" + description + ", type=" + type + ", highestPrice=" + highestPrice + ", quantityType=" + quantityType + ", quantity=" + quantity + ", price=" + price + '}';
    }

}
