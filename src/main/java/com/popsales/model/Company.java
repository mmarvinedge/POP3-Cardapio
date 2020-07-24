/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Id;

/**
 *
 * @author JOAO PAULO
 */
public class Company {

    @Id
    private String id;
    private String companyName;

    private String name;
    private String socialReason;
    private String phone;
    private String color;
    private String logo;
    private String owner;
    private String domain;
    private String cgccpf;
    private Address address;
    private BigDecimal deliveryCost;
    private TimeOpen time;
    private String funcionamento;
    private String messageWelcome;

    private List<Bairro> bairros;
    private String nameUrl;
    private String aproxTime;
    private Boolean integration;
    private Boolean trial;
    private String trialDate;
    private List<CouponCode> coupons;
    private Boolean uniqueDeliveryCost;
    private Integer licenseType;
    private String licenseDate;
    private Boolean onlyMenu;
    private Boolean freeVersion;
    private Boolean block;
    private Boolean worksCoupon;
    private Boolean deliveryOnly, withdrawalOnly, decimalQuantity;
    private String email;
    private Shift shift;
    private BigDecimal minimalValue;
    private Boolean open;

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

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public BigDecimal getMinimalValue() {
        return minimalValue;
    }

    public void setMinimalValue(BigDecimal minimalValue) {
        this.minimalValue = minimalValue;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCgccpf() {
        return cgccpf;
    }

    public void setCgccpf(String cgccpf) {
        this.cgccpf = cgccpf;
    }

    public Boolean getIntegration() {
        return integration;
    }

    public void setIntegration(Boolean integration) {
        this.integration = integration;
    }

    public Boolean getTrial() {
        return trial;
    }

    public void setTrial(Boolean trial) {
        this.trial = trial;
    }

    public String getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(String trialDate) {
        this.trialDate = trialDate;
    }

    public Integer getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Integer licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final Company other = (Company) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
