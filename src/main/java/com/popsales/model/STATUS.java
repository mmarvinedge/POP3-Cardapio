/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

/**
 *
 * @author PICHAU
 */
public enum STATUS {

    PLACED("Pedido realizado"),
    CONFIRMED("Pedido confirmado pelo restaurante"),
    DISPATCHED("Pedido em rota de entrega"),
    DELIVERED("Pedido entregue"),
    CANCELLED("Pedido cancelado");

    private final String descrition;

    STATUS(String descrition) {
        this.descrition = descrition;
    }
}
