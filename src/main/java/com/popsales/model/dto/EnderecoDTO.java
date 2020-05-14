/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.model.dto;

/**
 *
 * @author Renato
 */
public class EnderecoDTO {

    private String tipo_logradouro;
    private String logradouro;
    private String complemento;
    private String cidade;
    private String cod_ibge;
    private String uf;
    private String bairro;
    private String cep;
    private String latitude;
    private String longitude;

    public EnderecoDTO() {
    }

    public String getTipo_logradouro() {
        return tipo_logradouro;
    }

    public void setTipo_logradouro(String tipo_logradouro) {
        this.tipo_logradouro = tipo_logradouro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCod_ibge() {
        return cod_ibge;
    }

    public void setCod_ibge(String cod_ibge) {
        this.cod_ibge = cod_ibge;
    }

    @Override
    public String toString() {
        return "EnderecoDTO{" + "tipo_logradouro=" + tipo_logradouro + ", logradouro=" + logradouro + '}';
    }

}
