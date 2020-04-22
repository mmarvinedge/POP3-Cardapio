/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popsales.model.Category;
import com.popsales.model.Company;
import com.popsales.model.Order;
import com.popsales.model.Product;
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
import okhttp3.ResponseBody;

/**
 *
 * @author Renato
 */
@Stateless
public class CategoryServices {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    public Company loadCompany(String idCompany) throws IOException, Exception {
        if (idCompany == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        Company comp = new Company();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/company/" + idCompany)
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

    public List<Category> getCategoryList(String idCompany) throws IOException, Exception {
        if (idCompany == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        List<Category> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/categories/")
                .header("company_id", idCompany)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();

            saida = new Gson().fromJson(json, new TypeToken<List<Category>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Erro Carai");
        }

        return saida;
    }

    public List<Product> getProducts(String idCompany, String idCat) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/byCategory/" + idCat)
                .header("company_id", idCompany)
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

        saida = new Gson().fromJson(json, new TypeToken<List<Product>>() {
        }.getType());

        return saida;
    }
    public List<Product> getProductsPromo(String idCompany) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/promo/")
                .header("company_id", idCompany)
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
        saida = new Gson().fromJson(json, new TypeToken<List<Product>>() {
        }.getType());

        return saida;
    }

    public void sendOrder(Order order, String companyID) throws IOException {
        System.out.println(Constantes.URL);

        RequestBody body = RequestBody.create(new Gson().toJson(order), Constantes.JSON); // new
        // RequestBody body = RequestBody.create(JSON, json); // old
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/")
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        String b = response.body().toString();
        System.out.println(b);
    }

}
