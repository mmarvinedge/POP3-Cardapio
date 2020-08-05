/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.popsales.model.Bairro;
import com.popsales.model.Category;
import com.popsales.model.Company;
import com.popsales.model.Order;
import com.popsales.model.Product;
import com.popsales.model.dto.EnderecoDTO;
import com.popsales.util.Constantes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateless;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Renato
 */
@Stateless
public class CategoryServices {
    
    public final String companyID = "company_id";

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

    public List<Category> getCategoryList(String idCompany) throws IOException, Exception {
        if (idCompany == null) {
            throw new Exception("NAO CHEGOU ID DA COMPANIA");
        }
        List<Category> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/categories/")
                .header(companyID, idCompany)
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
                .header(companyID, idCompany)
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

    public List<Product> getProductsPromo(String idCompany) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/promo/")
                .header(companyID, idCompany)
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

    public List<String> getBairros(String city) throws IOException {
        List<String> saida = new ArrayList();
        Request request = new Request.Builder()
                .url("http://metre.ddns.net:88/bairro.php?cidade=" + city)
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
        List<Bairro> bairs = new Gson().fromJson(json, new TypeToken<List<Bairro>>() {
        }.getType());
        for (Bairro bair : bairs) {
            saida.add(bair.getBairro());
        }
        Collections.sort(saida, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        saida.add("Não Possui na lista!");
        return saida;
    }
    
    public List<Product> getProductsByCompany(String idCompany) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/")
                .header(companyID, idCompany)
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
    
    public List<Product> getProductsByTurn(String idCompany) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/allByTurn")
                .header(companyID, idCompany)
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
    
    public List<Product> getProductsMenuPromo(String idCompany) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/promoMenu/")
                .header(companyID, idCompany)
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
    
    public List<Product> getProductsMenu(String idCompany, String idCat) throws IOException {
        List<Product> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + "/product/byCategoryMenu/" + idCat)
                .header(companyID, idCompany)
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

}
