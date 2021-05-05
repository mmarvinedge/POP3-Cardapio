/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author JOAO PAULO
 */
public class Product implements Serializable {

    private String id;
    private String sku;
    private String name;
    private String order;
    private Boolean enabled;
    private String description;
    private Boolean availability;
    private String imageType;
    private String imageBase64;
    private BigDecimal price;
    private BigDecimal priceOriginal;
    private String companyId;
    private Category categoryMain;
    private List<Category> categories;
    private List<Attribute> attributes;
    private List<Attribute> attributesSelected;
    private String printer;
    private Boolean promo;
    private Boolean fraction;

    private String sizePizza;
    private Integer maxPizza;
    private List<FlavorPizza> flavorsPizza;
    private String rulePricePizza;
    private ProductDay productDay;
    private Shift shift;
    private BigDecimal priceMenu;
    private Boolean onlyMenu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Boolean getEnabled() {
        if (enabled == null) {
            return true;
        }
        return enabled;
    }

    public void setEnable(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getImageType() {
        if (imageType == null || imageType.isEmpty()) {
            imageType = "image/png";
        }
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageBase64() {
        if (imageBase64 == null) {
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = req.getRequestURL().toString();
            String urll = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
            return urll + "resources/img/nophoto.png";
        }
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Category getCategoryMain() {
        return categoryMain;
    }

    public void setCategoryMain(Category categoryMain) {
        this.categoryMain = categoryMain;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public Boolean getPromo() {
        return promo;
    }

    public void setPromo(Boolean promo) {
        this.promo = promo;
    }

    public List<Attribute> getAttributesSelected() {
        return attributesSelected;
    }

    public void setAttributesSelected(List<Attribute> attributesSelected) {
        this.attributesSelected = attributesSelected;
    }

    public String getSizePizza() {
        return sizePizza;
    }

    public void setSizePizza(String sizePizza) {
        this.sizePizza = sizePizza;
    }

    public Integer getMaxPizza() {
        return maxPizza;
    }

    public void setMaxPizza(Integer maxPizza) {
        this.maxPizza = maxPizza;
    }

    public List<FlavorPizza> getFlavorsPizza() {
        if (flavorsPizza != null && flavorsPizza.size() > 0) {
            return flavorsPizza.stream().filter(f -> !f.getDisabled()).collect(Collectors.toList());
        }
        return flavorsPizza;
    }

    public void setFlavorsPizza(List<FlavorPizza> flavorsPizza) {
        this.flavorsPizza = flavorsPizza;
    }

    public String getRulePricePizza() {
        return rulePricePizza;
    }

    public void setRulePricePizza(String rulePricePizza) {
        this.rulePricePizza = rulePricePizza;
    }

    public BigDecimal getPriceOriginal() {
        return priceOriginal;
    }

    public void setPriceOriginal(BigDecimal priceOriginal) {
        this.priceOriginal = priceOriginal;
    }

    public ProductDay getProductDay() {
        return productDay;
    }

    public void setProductDay(ProductDay productDay) {
        this.productDay = productDay;
    }

    public BigDecimal getPriceMenu() {
        return priceMenu;
    }

    public void setPriceMenu(BigDecimal priceMenu) {
        this.priceMenu = priceMenu;
    }

    public Boolean getOnlyMenu() {
        return onlyMenu;
    }

    public void setOnlyMenu(Boolean showMenu) {
        this.onlyMenu = showMenu;
    }

    public Boolean getFraction() {
        if (fraction == null) {
            fraction = false;
        }
        return fraction;
    }

    public void setFraction(Boolean fraction) {
        this.fraction = fraction;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", sku=" + sku + ", name=" + name + ", order=" + order + ", enabled=" + enabled + ", description=" + description + ", availability=" + availability + ", imageType=" + imageType + ", imageBase64=" + imageBase64 + ", price=" + price + ", priceOriginal=" + priceOriginal + ", companyId=" + companyId + ", categoryMain=" + categoryMain + ", categories=" + categories + ", attributes=" + attributes + ", attributesSelected=" + attributesSelected + ", printer=" + printer + ", promo=" + promo + ", fraction=" + fraction + ", sizePizza=" + sizePizza + ", maxPizza=" + maxPizza + ", flavorsPizza=" + flavorsPizza + ", rulePricePizza=" + rulePricePizza + ", productDay=" + productDay + ", shift=" + shift + ", priceMenu=" + priceMenu + ", onlyMenu=" + onlyMenu + '}';
    }

    public Shift getShift() {
        if (shift == null) {
            shift = new Shift();
        }
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

}
