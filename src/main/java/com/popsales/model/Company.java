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
    private String funcionamento;
    private String messageWelcome;

    private List<Bairro> bairros;
    private String nameUrl;
    private String aproxTime;
    private Boolean uniqueDeliveryCost;
    private Boolean onlyMenu;
    private Boolean worksCoupon;
    private List<CouponCode> coupons;
    private Boolean freeVersion;
    private Boolean deliveryOnly, withdrawalOnly, decimalQuantity;

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
        if (phone == null) {
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

    public String getFuncionamento() {
        return funcionamento;
    }

    public void setFuncionamento(String funcionamento) {
        this.funcionamento = funcionamento;
    }

    public String getMessageWelcome() {
        if (messageWelcome == null) {
            messageWelcome = "Para iniciar seu pedido";
        }
        return messageWelcome;
    }

    public void setMessageWelcome(String messageWelcome) {
        this.messageWelcome = messageWelcome;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", companyName=" + companyName + ", name=" + name + ", phone=" + phone + ", owner=" + owner + ", address=" + address + ", deliveryCost=" + deliveryCost + ", logo=" + logo + ", time=" + time + '}';
    }

    public String getNameUrl() {
        return nameUrl;
    }

    public void setNameUrl(String nameUrl) {
        this.nameUrl = nameUrl;
    }

    public String getAproxTime() {
        return aproxTime;
    }

    public void setAproxTime(String aproxTime) {
        this.aproxTime = aproxTime;
    }

    public Boolean getUniqueDeliveryCost() {
        if (uniqueDeliveryCost == null) {
            if (bairros != null && bairros.size() > 0) {
                uniqueDeliveryCost = false;
            } else {
                uniqueDeliveryCost = true;
            }
        }
        return uniqueDeliveryCost;
    }

    public void setUniqueDeliveryCost(Boolean uniqueDeliveryCost) {
        this.uniqueDeliveryCost = uniqueDeliveryCost;
    }

    public Boolean getOnlyMenu() {
        if (onlyMenu == null) {
            onlyMenu = false;
        }
        return onlyMenu;
    }

    public void setOnlyMeny(Boolean onlyMenu) {
        this.onlyMenu = onlyMenu;
    }

    public Boolean getWorksCoupon() {
        if (worksCoupon == null) {
            worksCoupon = false;
        }
        return worksCoupon;
    }

    public void setWorksCoupon(Boolean worksCoupon) {
        this.worksCoupon = worksCoupon;
    }

    public List<CouponCode> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponCode> coupons) {
        this.coupons = coupons;
    }

    public Boolean getFreeVersion() {
        if (freeVersion == null) {
            freeVersion = false;
        }
        return freeVersion;
    }

    public void setFreeVersion(Boolean freeVersion) {
        this.freeVersion = freeVersion;
    }

    public Boolean getDeliveryOnly() {
        if (deliveryOnly == null) {
            deliveryOnly = true;
        }
        return deliveryOnly;
    }

    public void setDeliveryOnly(Boolean deliveryOnly) {
        this.deliveryOnly = deliveryOnly;
    }

    public Boolean getWithdrawalOnly() {
        if (withdrawalOnly == null) {
            withdrawalOnly = true;
        }
        return withdrawalOnly;
    }

    public void setWithdrawalOnly(Boolean withdrawalOnly) {
        this.withdrawalOnly = withdrawalOnly;
    }

    public Boolean getDecimalQuantity() {
        if (decimalQuantity == null) {
            decimalQuantity = false;
        }
        return decimalQuantity;
    }

    public void setDecimalQuantity(Boolean decimalQuantity) {
        this.decimalQuantity = decimalQuantity;
    }

}
