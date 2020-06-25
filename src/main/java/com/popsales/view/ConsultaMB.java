/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Order;
import com.popsales.services.OrderService;
import com.popsales.util.OUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Renato
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class ConsultaMB {

    private OrderService orderService = new OrderService();
    private Order order = new Order();
    private String pedido, nome = "";
    private Date dtRegistro = null;

    public ConsultaMB() {
        pedido = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pedido");
    }

    @PostConstruct
    public void init() {
        try {
            order = orderService.orderByID(pedido);
            dtRegistro = converterData(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Order getOrder() {
        return order;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static Date converterData(Order c) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("pt_BR")).parse(c.getDtRegister());
    }

    public Date getDtRegistro() {
        return dtRegistro;
    }

    public String formatarData(Date data, String type) {
        if (type.equalsIgnoreCase("Data")) {
            return OUtils.formataData(data, "dd/MM/yyyy");
        } else {
            return OUtils.formataData(data, "HH:mm:ss");
        }

    }

}
