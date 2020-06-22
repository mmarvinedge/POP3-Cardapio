/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.popsales.model.Company;
import com.popsales.util.Constantes;
import java.io.IOException;
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
    
}
