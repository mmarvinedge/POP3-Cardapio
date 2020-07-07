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
                System.out.println(c.getCount() + " limite: " + c.getQuantity());
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getCount() <= c.getQuantity()) {
                    if (c.getEnable()) {
                        if (lastOrder.getCoupon() != null) {
                            if (lastOrder.getCoupon().equalsIgnoreCase(couponCode)) {
                                String today = OUtils.formataData(new Date(), "yyyy-MM-dd");
                                String dateOrder = lastOrder.getDtRegister().substring(0, 10);
                                System.out.println("hoje: " + today + " dateOrder: " + dateOrder);
                                if (today.equalsIgnoreCase(dateOrder)) {
                                    return "onetime";
                                } else {
                                    System.out.println(c.getMinimalValue().doubleValue() + " " + total.doubleValue());
                                    if (c.getDeliveryCost()) {
                                        if (totalSemTaxa.doubleValue() < c.getMinimalValue().doubleValue()) {
                                            return "minimal";
                                        }
                                    } else {
                                        if (total.doubleValue() < c.getMinimalValue().doubleValue()) {
                                            return "minimal";
                                        }
                                    }
                                    return "true";
                                }
                            } else if (!lastOrder.getCoupon().equalsIgnoreCase(couponCode)) {
                                if (!c.getPercentual() && total.doubleValue() < c.getMinimalValue().doubleValue()) {
                                    return "minimal";
                                }
                                return "true";
                            }
                        } else {
                            // esse número não utilizou um cupom no seu último pedido
                            if (!c.getPercentual() && total.doubleValue() < c.getMinimalValue().doubleValue()) {
                                return "minimal";
                            }
                            return "true";
                        }
                    } else {
                        return "false";
                    }
                } else if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getCount() >= c.getQuantity()) {
                    return "expired";
                }
            }
        } else {
            // essa company não possui cupons cadastrados
            return "false";
        }
        return "false";
    }

    public String checkCouponNoOrder(List<CouponCode> coupons, String couponCode, BigDecimal total, BigDecimal totalSemTaxa) {
        if (coupons != null && coupons.size() > 0) {
            for (CouponCode c : coupons) {
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getCount() >= c.getQuantity()) {
                    return "expired";
                } else {
                    if (c.getDeliveryCost()) {
                        if (totalSemTaxa.doubleValue() < c.getMinimalValue().doubleValue()) {
                            return "minimal";
                        }
                    } else {
                        if (total.doubleValue() < c.getMinimalValue().doubleValue()) {
                            return "minimal";
                        }
                    }
                    return "true";
                }
            }
            // esse número não utilizou este cupom
            return "false";
        } else {
            // esse número não utilizou um cupom no seu último pedido
            return "false";
        }
    }

}
