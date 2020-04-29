/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.util.Objects;

/**
 *
 * @author Renato
 */
public class TimeOpen {

    private Boolean dom, seg, ter, qua, qui, sex, sab;
    private String openDom, openSeg, openTer, openQua, openQui, openSex, openSab;
    private String closeDom, closeSeg, closeTer, closeQua, closeQui, closeSex, closeSab;

    public Boolean getDom() {
        return dom;
    }

    public void setDom(Boolean dom) {
        this.dom = dom;
    }

    public Boolean getSeg() {
        return seg;
    }

    public void setSeg(Boolean seg) {
        this.seg = seg;
    }

    public Boolean getTer() {
        return ter;
    }

    public void setTer(Boolean ter) {
        this.ter = ter;
    }

    public Boolean getQua() {
        return qua;
    }

    public void setQua(Boolean qua) {
        this.qua = qua;
    }

    public Boolean getQui() {
        return qui;
    }

    public void setQui(Boolean qui) {
        this.qui = qui;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Boolean getSab() {
        return sab;
    }

    public void setSab(Boolean sab) {
        this.sab = sab;
    }

    public String getOpenDom() {
        return openDom;
    }

    public void setOpenDom(String openDom) {
        this.openDom = openDom;
    }

    public String getOpenSeg() {
        return openSeg;
    }

    public void setOpenSeg(String openSeg) {
        this.openSeg = openSeg;
    }

    public String getOpenTer() {
        return openTer;
    }

    public void setOpenTer(String openTer) {
        this.openTer = openTer;
    }

    public String getOpenQua() {
        return openQua;
    }

    public void setOpenQua(String openQua) {
        this.openQua = openQua;
    }

    public String getOpenQui() {
        return openQui;
    }

    public void setOpenQui(String openQui) {
        this.openQui = openQui;
    }

    public String getOpenSex() {
        return openSex;
    }

    public void setOpenSex(String openSex) {
        this.openSex = openSex;
    }

    public String getOpenSab() {
        return openSab;
    }

    public void setOpenSab(String openSab) {
        this.openSab = openSab;
    }

    public String getCloseDom() {
        return closeDom;
    }

    public void setCloseDom(String closeDom) {
        this.closeDom = closeDom;
    }

    public String getCloseSeg() {
        return closeSeg;
    }

    public void setCloseSeg(String closeSeg) {
        this.closeSeg = closeSeg;
    }

    public String getCloseTer() {
        return closeTer;
    }

    public void setCloseTer(String closeTer) {
        this.closeTer = closeTer;
    }

    public String getCloseQua() {
        return closeQua;
    }

    public void setCloseQua(String closeQua) {
        this.closeQua = closeQua;
    }

    public String getCloseQui() {
        return closeQui;
    }

    public void setCloseQui(String closeQui) {
        this.closeQui = closeQui;
    }

    public String getCloseSex() {
        return closeSex;
    }

    public void setCloseSex(String closeSex) {
        this.closeSex = closeSex;
    }

    public String getCloseSab() {
        return closeSab;
    }

    public void setCloseSab(String closeSab) {
        this.closeSab = closeSab;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.dom);
        hash = 89 * hash + Objects.hashCode(this.seg);
        hash = 89 * hash + Objects.hashCode(this.ter);
        hash = 89 * hash + Objects.hashCode(this.qua);
        hash = 89 * hash + Objects.hashCode(this.qui);
        hash = 89 * hash + Objects.hashCode(this.sex);
        hash = 89 * hash + Objects.hashCode(this.sab);
        hash = 89 * hash + Objects.hashCode(this.openDom);
        hash = 89 * hash + Objects.hashCode(this.openSeg);
        hash = 89 * hash + Objects.hashCode(this.openTer);
        hash = 89 * hash + Objects.hashCode(this.openQua);
        hash = 89 * hash + Objects.hashCode(this.openQui);
        hash = 89 * hash + Objects.hashCode(this.openSex);
        hash = 89 * hash + Objects.hashCode(this.openSab);
        hash = 89 * hash + Objects.hashCode(this.closeDom);
        hash = 89 * hash + Objects.hashCode(this.closeSeg);
        hash = 89 * hash + Objects.hashCode(this.closeTer);
        hash = 89 * hash + Objects.hashCode(this.closeQua);
        hash = 89 * hash + Objects.hashCode(this.closeQui);
        hash = 89 * hash + Objects.hashCode(this.closeSex);
        hash = 89 * hash + Objects.hashCode(this.closeSab);
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
        final TimeOpen other = (TimeOpen) obj;
        if (!Objects.equals(this.dom, other.dom)) {
            return false;
        }
        if (!Objects.equals(this.seg, other.seg)) {
            return false;
        }
        if (!Objects.equals(this.ter, other.ter)) {
            return false;
        }
        if (!Objects.equals(this.qua, other.qua)) {
            return false;
        }
        if (!Objects.equals(this.qui, other.qui)) {
            return false;
        }
        if (!Objects.equals(this.sex, other.sex)) {
            return false;
        }
        if (!Objects.equals(this.sab, other.sab)) {
            return false;
        }
        if (!Objects.equals(this.openDom, other.openDom)) {
            return false;
        }
        if (!Objects.equals(this.openSeg, other.openSeg)) {
            return false;
        }
        if (!Objects.equals(this.openTer, other.openTer)) {
            return false;
        }
        if (!Objects.equals(this.openQua, other.openQua)) {
            return false;
        }
        if (!Objects.equals(this.openQui, other.openQui)) {
            return false;
        }
        if (!Objects.equals(this.openSex, other.openSex)) {
            return false;
        }
        if (!Objects.equals(this.openSab, other.openSab)) {
            return false;
        }
        if (!Objects.equals(this.closeDom, other.closeDom)) {
            return false;
        }
        if (!Objects.equals(this.closeSeg, other.closeSeg)) {
            return false;
        }
        if (!Objects.equals(this.closeTer, other.closeTer)) {
            return false;
        }
        if (!Objects.equals(this.closeQua, other.closeQua)) {
            return false;
        }
        if (!Objects.equals(this.closeQui, other.closeQui)) {
            return false;
        }
        if (!Objects.equals(this.closeSex, other.closeSex)) {
            return false;
        }
        if (!Objects.equals(this.closeSab, other.closeSab)) {
            return false;
        }
        return true;
    }

}
