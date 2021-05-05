/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Tadeu-PC
 */
public class Item {

    private String id;
    private String sku; //codigo do produto
    private String name;
    public BigDecimal price;
    public BigDecimal quantity;
    public BigDecimal totalAds;
    public BigDecimal total;
    public String obs;
    public List<Attribute> attributes;
    public List<AttributeValue> attributesValues;
    public Product product;
    public String printer;
    private List<FlavorPizza> flavors;

    public Item() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public List<AttributeValue> getAttributesValues() {
        return attributesValues;
    }

    public void setAttributesValues(List<AttributeValue> attributesValues) {
        this.attributesValues = attributesValues;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAds() {
        return totalAds;
    }

    public void setTotalAds(BigDecimal totalAds) {
        this.totalAds = totalAds;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<FlavorPizza> getFlavors() {
        return flavors;
    }

    public void setFlavors(List<FlavorPizza> flavors) {
        this.flavors = flavors;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.sku);
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.price);
        hash = 89 * hash + Objects.hashCode(this.quantity);
        hash = 89 * hash + Objects.hashCode(this.totalAds);
        hash = 89 * hash + Objects.hashCode(this.total);
        hash = 89 * hash + Objects.hashCode(this.obs);
        hash = 89 * hash + Objects.hashCode(this.attributes);
        hash = 89 * hash + Objects.hashCode(this.attributesValues);
        hash = 89 * hash + Objects.hashCode(this.product);
        hash = 89 * hash + Objects.hashCode(this.printer);
        hash = 89 * hash + Objects.hashCode(this.flavors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.sku, other.sku)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.obs, other.obs)) {
            return false;
        }
        if (!Objects.equals(this.printer, other.printer)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.quantity, other.quantity)) {
            return false;
        }
        if (!Objects.equals(this.totalAds, other.totalAds)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        if (!Objects.equals(this.attributes, other.attributes)) {
            return false;
        }
        if (!Objects.equals(this.attributesValues, other.attributesValues)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.flavors, other.flavors)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", sku=" + sku + ", name=" + name + ", price=" + price + ", quantity=" + quantity + ", totalAds=" + totalAds + ", total=" + total + ", obs=" + obs + ", attributes=" + attributes + ", attributesValues=" + attributesValues + ", product=" + product + ", printer=" + printer + ", flavors=" + flavors + '}';
    }
    
}
