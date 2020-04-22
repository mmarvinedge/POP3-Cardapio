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
 * @author JOAO PAULO
 */
public class Company {

    private String id;
    private String companyName;
    private String name;
    private String phone;
    private String owner;
    private Address address;
    private BigDecimal deliveryCost;
    private String logo;
    private TimeOpen time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneUnformat() {
        if(phone == null){
            phone = "";
        }
        return phone.replace("(", "").replace(")", "").replace("-", "").trim();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(BigDecimal deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public TimeOpen getTime() {
        return time;
    }

    public void setTime(TimeOpen time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", companyName=" + companyName + ", name=" + name + ", phone=" + phone + ", owner=" + owner + ", address=" + address + ", deliveryCost=" + deliveryCost + ", logo=" + logo + ", time=" + time + '}';
    }

}
