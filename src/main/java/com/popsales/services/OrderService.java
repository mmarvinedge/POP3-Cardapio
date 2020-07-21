/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.popsales.model.Order;
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
public class OrderService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    public Order lastOrderByPhone(String idCompany, String phone) throws IOException, Exception {
        if (idCompany == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        Order order = new Order();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/lastByPhoneInCompany/" + phone)
                .header("company_id", idCompany)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();
            System.out.println(json);
            System.out.println(response);
            order = gson.fromJson(json, Order.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro Carai");
        }
        return order;
    }

    public Order orderByID(String id) throws IOException, Exception {

        Order order = new Order();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/" + id)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();
            order = gson.fromJson(json, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro Carai");
        }
        return order;
    }
    
    public void sendOrder(Order order, String companyID) throws IOException {
        System.out.println(Constantes.URL);
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        RequestBody body = RequestBody.create(gson.toJson(order), Constantes.JSON); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/")
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        String b = response.body().string();
        System.out.println(b);
    }
}
