/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import com.popsales.util.OUtils;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Renato
 */
public class FlavorPizza {

    private String sku;
    private String flavor;
    private String description;
    private BigDecimal price;

    public FlavorPizza() {
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getFlavor() {
        return flavor;
    }

    public String htmlGetFlavor(Item i) {
        try {
            if (flavor != null) {
                if (i.getProduct().getPromo()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<div class='row' style='margin-top: 7px !important;'>"
                            + nome(i) + valor(i)
                            + "</div> "
                            + "<div class='row'>"
                            + descricao(i)
                            + "</div>");
                    return sb.toString();
                } else {
                    StringBuilder sb = new StringBuilder();
                    //sb.append("<span style='display: inline-table !important; max-width: 100%; width: auto;' class='description-pizza'><strong>" + flavor + "</strong></div> <div class='description-value' style='display: inline-block !important;'><small><s>" + OUtils.formatarMoeda(getPrice().doubleValue()) + "</s></small> <span class='color-value'>" + OUtils.formatarMoeda(i.getProduct().getPrice().doubleValue()) + "</span></span>");
                    //sb.append("</div><br/><div style='float: right;'><i style='font-size: 12px; color: gray; font-weight: 400;'>" + description + "</i></div>");
                    //sb.append("<hr/>");
                    sb.append("<div class='row'>");
                    sb.append(nome(i));
                    sb.append(valor(i));
                    sb.append("</div> ");
                    sb.append("<div class='row'>");
                    sb.append(descricao(i));
                    sb.append("</div>");
                    return sb.toString();
                    //return "<span style='display: inline-table !important;' class='description-pizza'><strong>" + flavor + "</strong></div> <div class='description-value' style='display: inline-block !important;'><span class='color-value'>" + OUtils.formatarMoeda(getPrice().doubleValue()) + "</span></span>";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return flavor;
        }
    }
    
    public String htmlGetFlavorDesktop(Item i) {
        try {
            if (flavor != null) {
                if (i.getProduct().getPromo()) {
                    return "<span class='description-pizza'><strong>" + flavor + "</strong></div>  <div class='description-value'><small><s>" + OUtils.formatarMoeda(getPrice().doubleValue()) + "</s></small> <span class='color-value'>" + OUtils.formatarMoeda(i.getProduct().getPrice().doubleValue()) + "</span></span>";
                } else {
                    return "<span class='description-pizza'><strong>" + flavor + "</strong></div>  <div class='description-value'><span class='color-value'>" + OUtils.formatarMoeda(getPrice().doubleValue()) + "</span></span>";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return flavor;
        }
    }

    public String nome(Item i) {
        if (i.getProduct().getPromo()) {
            return "<div class='col-xs-6 no-padding'><strong>" + flavor + "</strong></div>";
        } else {
            return "<div class='col-xs-6 no-padding'><strong>" + flavor + "</strong></div>";
        }
    }

    public String valor(Item i) {
        if (i.getProduct().getPromo()) {
            return "<div class='text-right'><small><s>" + OUtils.formatarMoeda(getPrice().doubleValue()) + "</s></small> <span class='color-value'>" + OUtils.formatarMoeda(i.getProduct().getPrice().doubleValue()) + "</span></div></div>";
        } else {
            return "<div class='text-right'><span class='color-value'>" + OUtils.formatarMoeda(price.doubleValue()) + "</span></div>";
        }
    }

    public String descricao(Item i) {
        if (description != null) {
            return "<div class='col-xs-12 no-padding'><i style='font-size: 12px; color: gray; font-weight: 400; display: inline-table;'>" + description + "</i></div>";
        } else {
            return "<div class='col-xs-12 no-padding'><i style='font-size: 12px; color: gray; font-weight: 400; display: inline-table;'>Pizzas</i></div>";
        }
    }

    public String getHtmlDescription(Item i) {
        try {
            if (flavor != null) {
                if (description != null) {
                    return "";
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public BigDecimal getPrice() {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        if (description == null) {
            return description = "";
        } else {
            return description;
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.flavor);
        hash = 37 * hash + Objects.hashCode(this.price);
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
        final FlavorPizza other = (FlavorPizza) obj;
        if (!Objects.equals(this.flavor, other.flavor)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return true;
    }

}
