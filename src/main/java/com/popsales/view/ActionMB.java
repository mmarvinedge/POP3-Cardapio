/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Renato
 */
@ViewScoped
@ManagedBean
public class ActionMB {

    private String acao = "grupo";
    private Boolean home = true;

    public ActionMB() {
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        if (acao != null && acao.equalsIgnoreCase("fechar")) {
            PrimeFaces.current().executeScript("alerta('Informe 1 item!')");
            return;
        }

        this.acao = acao;
        PrimeFaces.current().ajax().update("grpPrincipal");
        PrimeFaces.current().ajax().update("pnlend");
    }

    public void setAcaoFechar(String acao, int size) {
        if (acao != null && acao.equalsIgnoreCase("fechar") && size == 0) {
            PrimeFaces.current().executeScript("alerta('Seu carrinho está vazio!')");
            return;
        }
        this.acao = acao;
        PrimeFaces.current().ajax().update("grpPrincipal");
        PrimeFaces.current().ajax().update("pnlend");
    }

    public void setAcaoNOUpdate(String acao) {
        System.out.println("change");
        this.acao = acao;
    }

    public Boolean getHome() {
        return home;
    }

    public void setHome(Boolean home) {
        try {
            System.out.println("CLICOU?");
            Thread.sleep(1000);
            this.home = home;
            System.out.println("HOME: " + this.home);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
