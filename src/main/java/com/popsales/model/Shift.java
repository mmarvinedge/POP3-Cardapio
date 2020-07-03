/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model;

import java.util.Objects;

/**
 *
 * @author Marvin
 */
public class Shift {

    private Boolean morning;
    private Boolean afternoon;
    private Boolean night;
    private String begginMorning, endMorning;
    private String begginAfternoon, endAfternoon;
    private String begginNight, endNight;

    public Boolean getMorning() {
        if (morning == null) {
            return Boolean.TRUE;
        } else {
            return morning;
        }
    }

    public void setMorning(Boolean morning) {
        this.morning = morning;
    }

    public Boolean getAfternoon() {
        if (afternoon == null) {
            return Boolean.TRUE;
        } else {
            return afternoon;
        }
    }

    public void setAfternoon(Boolean afternoon) {
        this.afternoon = afternoon;
    }

    public Boolean getNight() {
        if (night == null) {
            return Boolean.TRUE;
        } else {
            return night;
        }
    }

    public void setNight(Boolean night) {
        this.night = night;
    }

    public String getBegginMorning() {
        return begginMorning;
    }

    public void setBegginMorning(String begginMorning) {
        this.begginMorning = begginMorning;
    }

    public String getEndMorning() {
        return endMorning;
    }

    public void setEndMorning(String endMorning) {
        this.endMorning = endMorning;
    }

    public String getBegginAfternoon() {
        return begginAfternoon;
    }

    public void setBegginAfternoon(String begginAfternoon) {
        this.begginAfternoon = begginAfternoon;
    }

    public String getEndAfternoon() {
        return endAfternoon;
    }

    public void setEndAfternoon(String endAfternoon) {
        this.endAfternoon = endAfternoon;
    }

    public String getBegginNight() {
        return begginNight;
    }

    public void setBegginNight(String begginNight) {
        this.begginNight = begginNight;
    }

    public String getEndNight() {
        return endNight;
    }

    public void setEndNight(String endNight) {
        this.endNight = endNight;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.morning);
        hash = 37 * hash + Objects.hashCode(this.afternoon);
        hash = 37 * hash + Objects.hashCode(this.night);
        hash = 37 * hash + Objects.hashCode(this.begginMorning);
        hash = 37 * hash + Objects.hashCode(this.endMorning);
        hash = 37 * hash + Objects.hashCode(this.begginAfternoon);
        hash = 37 * hash + Objects.hashCode(this.endAfternoon);
        hash = 37 * hash + Objects.hashCode(this.begginNight);
        hash = 37 * hash + Objects.hashCode(this.endNight);
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
        final Shift other = (Shift) obj;
        if (!Objects.equals(this.begginMorning, other.begginMorning)) {
            return false;
        }
        if (!Objects.equals(this.endMorning, other.endMorning)) {
            return false;
        }
        if (!Objects.equals(this.begginAfternoon, other.begginAfternoon)) {
            return false;
        }
        if (!Objects.equals(this.endAfternoon, other.endAfternoon)) {
            return false;
        }
        if (!Objects.equals(this.begginNight, other.begginNight)) {
            return false;
        }
        if (!Objects.equals(this.endNight, other.endNight)) {
            return false;
        }
        if (!Objects.equals(this.morning, other.morning)) {
            return false;
        }
        if (!Objects.equals(this.afternoon, other.afternoon)) {
            return false;
        }
        if (!Objects.equals(this.night, other.night)) {
            return false;
        }
        return true;
    }
    
    
}
