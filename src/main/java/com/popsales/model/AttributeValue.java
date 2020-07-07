/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Márvin Edge
 */
public class AttributeValue implements Serializable {

    private String id;
    private String sku;
    private String attribute_sku;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal total;
    private Boolean disabled;

    public AttributeValue() {
    }

    public AttributeValue(AttributeValue ab) {
        this.id = ab.getId();
        this.sku = ab.getSku();
        this.attribute_sku = ab.getAttribute_sku();
        this.name = ab.getName();
        this.description = ab.getDescription();
        this.price = ab.getPrice();
        this.quantity = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.disabled = ab.getDisabled();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getAttribute_sku() {
        return attribute_sku;
    }

    public void setAttribute_sku(String attribute_sku) {
        this.attribute_sku = attribute_sku;
    }

    public String getName() {
        return name.toUpperCase();
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        if (quantity == null) {
            quantity = BigDecimal.ZERO;
        }
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getDisabled() {
        if (disabled == null) {
            disabled = false;
        }
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "AttributeValue{" + "id=" + id + ", sku=" + sku + ", attribute_sku=" + attribute_sku + ", name=" + name + ", description=" + description + ", price=" + price + '}';
    }

}
