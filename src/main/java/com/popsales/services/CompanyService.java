/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popsales.model.Company;
import com.popsales.model.dto.EnderecoDTO;
import com.popsales.util.Constantes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Marvin
 */
@Stateless
public class CompanyService {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    public Company saveCompany(Company c) throws IOException, Exception {

        RequestBody body = RequestBody.create(new Gson().toJson(c), Constantes.JSON); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(Constantes.URL + "/company/")
                .put(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        String b = response.body().string();
        Company a = new Gson().fromJson(b, Company.class);
        return a;
    }

    public Company loadCompanyName(String name) throws IOException, Exception {
        if (name == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        Company comp = new Company();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/company/name/" + name)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();

            comp = new Gson().fromJson(json, Company.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro Carai");
        }

        return comp;
    }
    
    public List<EnderecoDTO> buscarEndereco(String endereco, String uf, String cidade) throws IOException {
        List<EnderecoDTO> saida = new ArrayList();
        endereco = endereco.toUpperCase().replace(" ", "%20").replace("RUA", "").replace("AV.", "").replace("AVENIDA", "").replace("TRAVESSA", "");


        Request request = new Request.Builder()
                .url("http://metre.ddns.net:88/endereco.php?cep=" + endereco + "&uf=" + uf + "&cidade=" + cidade)
                .get()
                .build();
        Response response = httpClient.newCall(request).execute();
        if (response.code() == 202) {
            throw new IOException("Nenhum dado retornado!");
        } else {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        }

        // Get response body
        String json = response.body().string();

        saida = new Gson().fromJson(json, new TypeToken<List<EnderecoDTO>>() {
        }.getType());

        return saida;
    }
    

}
