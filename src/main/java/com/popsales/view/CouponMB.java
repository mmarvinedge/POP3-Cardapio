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

    public String couponValid(List<CouponCode> coupons, String couponCode, Order lastOrder) {
        if (coupons != null && coupons.size() > 0) {
            for (CouponCode c : coupons) {
                System.out.println(c.getCount() + " limite: " + c.getQuantity());
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getCount() <= c.getQuantity()) {
                    if (c.getEnable()) {
                        System.out.println("entrei aqui");
                        if (lastOrder.getCoupon() != null) {
                            if (lastOrder.getCoupon().equalsIgnoreCase(couponCode)) {
                                String today = OUtils.formataData(new Date(), "yyyy-MM-dd");
                                String dateOrder = lastOrder.getDtRegister().substring(0, 10);
                                System.out.println("hoje: " + today + " dateOrder: " + dateOrder);
                                if (today.equalsIgnoreCase(dateOrder)) {
                                    return "onetime";
                                } else {
                                    // este cliente usou esse cupom porém não foi hoje
                                    return "true";
                                }
                            }
                        } else {
                            // esse número não utilizou um cupom no seu último pedido
                            return "true";
                        }
                    } else if (!c.getEnable()) {
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

    public String checkCouponNoOrder(List<CouponCode> coupons, String couponCode) {
        if (coupons != null && coupons.size() > 0) {
            for (CouponCode c : coupons) {
                if (c.getSlug().equalsIgnoreCase(couponCode.toUpperCase()) && c.getCount() >= c.getQuantity()) {
                    return "false";
                } else {
                    return "true";
                }
            }
            // esse número não utilizou este cupom
            return "true";
        } else {
            // esse número não utilizou um cupom no seu último pedido
            return "true";
        }
    }

}
