/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;

/**
 *
 * @author Marvin
 */
public class CouponCode {

    private String id;
    private String slug;
    private Integer quantity;
    private Integer count;
    private BigDecimal discount;
    private Boolean percentual;
    private BigDecimal minimalValue;
    private Boolean deliveryCost;
    private Boolean enable;

    public CouponCode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getPercentual() {
        if (percentual == null) {
            percentual = true;
        }
        return percentual;
    }

    public void setPercentual(Boolean percentual) {
        this.percentual = percentual;
    }

    public BigDecimal getMinimalValue() {
        if(minimalValue == null) {
            minimalValue = BigDecimal.ZERO;
        }
        return minimalValue;
    }

    public void setMinimalValue(BigDecimal minimalValue) {
        this.minimalValue = minimalValue;
    }

    public Boolean getDeliveryCost() {
        if (deliveryCost == null) {
            deliveryCost = true;
        }
        return deliveryCost;
    }

    public void setDeliveryCost(Boolean deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

}
