/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.AttributeValue;
import com.popsales.model.Category;
import com.popsales.model.Company;
import com.popsales.model.Item;
import com.popsales.model.Merchant;
import com.popsales.model.Order;
import com.popsales.model.Product;
import com.popsales.services.CategoryServices;
import com.popsales.util.OUtils;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Renato
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class OrderMB implements Serializable {

    CategoryServices categoriaService = new CategoryServices();

    private String idCompany = null;
    private Company company = new Company();
    private List<Category> categories = new ArrayList();
    private List<Product> products = new ArrayList();
    private Product product = new Product();
    private Category categorySelected = new Category();
    private Item item = new Item();
    private Order order = new Order();
    private List<AttributeValue> adicionais = new ArrayList();
    private BigDecimal totalAdicionais = BigDecimal.ZERO;

    public OrderMB() {

    }

    @PostConstruct
    public void init() {
        idCompany = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (idCompany == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString().replace("index.jsf", "") + "notfound/";
            PrimeFaces.current().executeScript("window.location = '" + url + "';");
        } else {
            try {
                company = categoriaService.loadCompany(idCompany);
                System.out.println("COMPANY: " + company.toString());
                System.out.println("VALOR COST: " + company.getDeliveryCost());
                order.setDeliveryCost(company.getDeliveryCost());
                order.getAddress().setCity((company.getAddress() != null && company.getAddress().getCity() != null) ? company.getAddress().getCity() : "");

            } catch (Exception e) {
            }

            loadCategorias();
        }
    }

    private void loadCategorias() {
        try {
            if (idCompany == null) {
            }
            categories = categoriaService.getCategoryList(idCompany);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void selectGroup(Category category, String id) {
        try {
            if (idCompany == null) {
            }
            if (category == null || idCompany == null) {
                return;
            }
            categorySelected = category;
            products = categoriaService.getProducts(idCompany, id);
            System.out.println("PRODUCTS");
        } catch (IOException ex) {
            PrimeFaces.current().executeScript("connectionErrorMsg('" + ex.getMessage() + "')");
            Logger.getLogger(OrderMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCart() {
        if (order.getProducts() == null) {
            order.setProducts(new ArrayList());
        }
        item.setAttributesValues(adicionais);
        item.setPrice(item.getProduct().getPrice());
        item.setTotalAds(totalAdicionais);
        item.setTotal(item.getPrice().multiply(item.getQuantity()).add(item.getTotalAds()));
        order.getProducts().add(item);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        calcularTotal();
        item = new Item();
        adicionais = new ArrayList<>();
        totalAdicionais = BigDecimal.ZERO;
    }

    private void calcularTotal() {
        order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()));
    }

    public void removeCart(Item i) {
        order.getProducts().remove(i);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        calcularTotal();
    }

    public String genCodigo() {
        try {
            String nome = order.getClientInfo().getName().substring(0, 2);
            String foneNoMask = order.getClientInfo().getPhone().replace("-", "").replace("(", "").replace(")", "");
            String foneCod = foneNoMask.substring(foneNoMask.length() - 4, foneNoMask.length());
            return nome + "-" + foneCod;
        } catch (Exception e) {
            return new Random().nextInt(6);
        }
    }

    public void registarPedido() {
        try {
            order.setNum_order(genCodigo());
            order.setDtRegister(OUtils.formataData(new Date(), "dd/MM/yyyy HH:mm:ss"));
            order.setStatus("Aguardando");
            order.setMerchant(new Merchant(company));
            categoriaService.sendOrder(order, idCompany);
            order = new Order();
            if (company.getAddress() != null && company.getAddress().getCity() != null) {
                order.getAddress().setCity(company.getAddress().getCity());
            }
            order.setDeliveryCost(company.getDeliveryCost());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.item = new Item();
        item.setName(product.getName());
        item.setPrice(product.getPrice());
        item.setQuantity(BigDecimal.ONE);
        item.setSku(product.getSku());
        item.setProduct(product);
        item.setPrinter(product.getPrinter());
    }

    public String getIdCompany() {
        return idCompany;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String formatarMoeda(Double val) {
        if (val == null) {
            return "R$ 0,00";
        }
        try {
            return NumberFormat.getCurrencyInstance().format(val);
        } catch (Exception e) {
            System.out.println("ERRO AO TENTAR FORMATAR: " + val);
            return "R$ " + val;
        }
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public Company getCompany() {
        if (company == null) {
            try {
                company = categoriaService.loadCompany(idCompany);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<AttributeValue> getAdicionais() {
        return adicionais;
    }

    public void setAdicionais(List<AttributeValue> adicionais) {
        this.adicionais = adicionais;
    }

    public BigDecimal getTotalAdicionais() {
        return totalAdicionais;
    }

    public void setTotalAdicionais(BigDecimal totalAdicionais) {
        this.totalAdicionais = totalAdicionais;
    }

    public void processarTotalAdicionais() {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            return;
        }
        totalAdicionais = BigDecimal.ZERO;
        adicionais.stream().forEach(c -> {
            if (c.getPrice() != null && c.getPrice().doubleValue() > 0) {
                totalAdicionais = totalAdicionais.add(c.getPrice().multiply(item.getQuantity()));
            }
        });
        System.out.println("TOTAL ADICIONAL: " + totalAdicionais);
        item.setTotalAds(totalAdicionais);
        item.setTotal((item.getPrice().multiply(item.getQuantity())).add(item.getTotalAds()));
        PrimeFaces.current().ajax().update("frmModal:grpValue");
    }

    public void change() {
        System.out.println(item.getQuantity());
        processarTotalAdicionais();
    }

}
