/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
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
    private Boolean enable;
    private String description;
    private Boolean availability;
    private String imageType;
    private String imageBase64;
    private BigDecimal price;
    private String companyId;
    private Category categoryMain;
    private List<Category> categories;
    private List<Attribute> attributes;
    private List<Attribute> attributesSelected;
    private String printer;
    private Boolean promo;

    private String sizePizza;
    private Integer maxPizza;
    private List<FlavorPizza> flavorsPizza;
    private String rulePricePizza;

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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    @Override
    public String toString() {
        return "Product{" + " sku=" + sku + ", name=" + name + ", order=" + order + ", enable=" + enable + ", description=" + description + ", availability=" + availability + ", imageType=" + imageType + ", imageBase64=" + imageBase64 + ", price=" + price + ", companyId=" + companyId + ", attributes=" + attributes + '}';
    }

}
