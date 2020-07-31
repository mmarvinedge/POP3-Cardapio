/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.io.Serializable;

/**
 *
 * @author Renato
 */
public class ProductDay implements Serializable{

    private Boolean naoVenderDom = false;
    private Boolean naoVenderSeg = false;
    private Boolean naoVenderTer = false;
    private Boolean naoVenderQua = false;
    private Boolean naoVenderQui = false;
    private Boolean naoVenderSex = false;
    private Boolean naoVenderSab = false;

    public Boolean getNaoVenderDom() {
      
        return naoVenderDom;
    }

    public void setNaoVenderDom(Boolean naoVenderDom) {
        this.naoVenderDom = naoVenderDom;
    }

    public Boolean getNaoVenderSeg() {
      
        return naoVenderSeg;
    }

    public void setNaoVenderSeg(Boolean naoVenderSeg) {
        this.naoVenderSeg = naoVenderSeg;
    }

    public Boolean getNaoVenderTer() {
      
        return naoVenderTer;
    }

    public void setNaoVenderTer(Boolean naoVenderTer) {
        this.naoVenderTer = naoVenderTer;
    }

    public Boolean getNaoVenderQua() {
     
        return naoVenderQua;
    }

    public void setNaoVenderQua(Boolean naoVenderQua) {
        this.naoVenderQua = naoVenderQua;
    }

    public Boolean getNaoVenderQui() {
       
        return naoVenderQui;
    }

    public void setNaoVenderQui(Boolean naoVenderQui) {
        this.naoVenderQui = naoVenderQui;
    }

    public Boolean getNaoVenderSex() {
      
        return naoVenderSex;
    }

    public void setNaoVenderSex(Boolean naoVenderSex) {
        this.naoVenderSex = naoVenderSex;
    }

    public Boolean getNaoVenderSab() {
      
        return naoVenderSab;
    }

    public void setNaoVenderSab(Boolean naoVenderSab) {
        this.naoVenderSab = naoVenderSab;
    }

}
