/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popsales.model.CouponCode;
import com.popsales.util.Constantes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Marvin
 */
@Stateless
public class CouponService {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    public List<CouponCode> couponsByCompany(String idCompany) throws IOException, Exception {
        if (idCompany == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        List<CouponCode> coupons = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/coupon/coupons/")
                .header("company_id", idCompany)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();
            coupons = new Gson().fromJson(json, new TypeToken<List<CouponCode>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro Carai");
        }
        return coupons;

    }

}
