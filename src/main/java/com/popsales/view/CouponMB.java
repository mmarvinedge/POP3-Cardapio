/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Company;
import com.popsales.model.CouponCode;
import com.popsales.model.Order;
import com.popsales.util.OUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Marvin
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class CouponMB {

    Boolean couponValid;

    public String couponValid(List<CouponCode> coupons, String couponCode, Order lastOrder, BigDecimal total, BigDecimal totalSemTaxa) {
        if (coupons != null && coupons.size() > 0) {
            for (CouponCode c : coupons) {
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getEnable()) {
                    if (c.getCount() >= c.getQuantity()) {
                        // cupom atingiu limite máximo de utilizações
                        return "expired";
                    }
                    if (lastOrder.getCoupon() != null) {
                        if (lastOrder.getCoupon().equalsIgnoreCase(couponCode)) {
                            String today = OUtils.formataData(new Date(), "yyyy-MM-dd");
                            String dateOrder = lastOrder.getDtRegister().substring(0, 10);
                            System.out.println("hoje: " + today + " dateOrder: " + dateOrder);
                            if (today.equalsIgnoreCase(dateOrder)) {
                                return "onetime";
                            }

                            if (c.getDeliveryCost() && totalSemTaxa.doubleValue() < c.getMinimalValue().doubleValue()) {
                                return "minimal";
                            }

                            if (total.doubleValue() < c.getMinimalValue().doubleValue()) {
                                return "minimal";
                            }

                            return "true";
                        } else if (!lastOrder.getCoupon().equalsIgnoreCase(couponCode)) {
                            if (c.getDeliveryCost() && totalSemTaxa.doubleValue() < c.getMinimalValue().doubleValue()) {
                                // total(sem taxa entrega) não contempla valor minimo do cupom
                                return "minimal";
                            }
                            if (total.doubleValue() < c.getMinimalValue().doubleValue()) {
                                // total+taxa entrega não contempla valor minimo do cupom
                                return "minimal";
                            }
                            return "true";
                        }
                    }
                } 
            }
        }
        // essa company não possui cupons cadastrados
        return "false";
    }

    public String checkCouponNoOrder(List<CouponCode> coupons, String couponCode, BigDecimal total, BigDecimal totalSemTaxa) {
        if (coupons != null && coupons.size() > 0) {
            for (CouponCode c : coupons) {
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getEnable()) {
                    if (c.getCount() >= c.getQuantity()) {
                        // cupom atingiu limite máximo de utilizações
                        return "expired";
                    }
                    if (c.getDeliveryCost() && totalSemTaxa.doubleValue() < c.getMinimalValue().doubleValue()) {
                        // total(sem taxa entrega) não contempla valor minimo do cupom
                        return "minimal";
                    }
                    if (total.doubleValue() < c.getMinimalValue().doubleValue()) {
                        // total+taxa entrega não contempla valor minimo do cupom
                        return "minimal";
                    }
                    return "true";
                }
            }
        }
        // company não possui cupons cadastrados
        return "false";
    }

}
