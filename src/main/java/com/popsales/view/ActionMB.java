/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

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
    }

    public void setAcaoFechar(String acao, int size) {
        if (acao != null && acao.equalsIgnoreCase("fechar") && size == 0) {
            PrimeFaces.current().executeScript("alerta('Informe 1 item!')");
            return;
        }

        this.acao = acao;
        PrimeFaces.current().ajax().update("grpPrincipal");
    }

    public void setAcaoNOUpdate(String acao) {
        System.out.println("change");
        this.acao = acao;
    }

}
