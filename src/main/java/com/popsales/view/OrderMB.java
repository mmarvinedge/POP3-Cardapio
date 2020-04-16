/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Category;
import com.popsales.model.Company;
import com.popsales.model.Item;
import com.popsales.model.Merchant;
import com.popsales.model.Order;
import com.popsales.model.Product;
import com.popsales.services.CategoryServices;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
                order.getAddress().setCity(company.getAddress().getCity());
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
            System.out.println("ID CATEGORY: " + id);
            if (category == null) {
                System.out.println("CATEGORY ULL");
            }
            if (category == null || idCompany == null) {
                return;
            }
            categorySelected = category;
            products = categoriaService.getProducts(idCompany, id);
        } catch (IOException ex) {
            PrimeFaces.current().executeScript("connectionErrorMsg('" + ex.getMessage() + "')");
            Logger.getLogger(OrderMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCart() {
        if (order.getProducts() == null) {
            order.setProducts(new ArrayList());
        }
        order.getProducts().add(item);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getQuantity() * m.getPrice()).reduce(0.0, Double::sum));
        calcularTotal();
        item = new Item();
    }

    private void calcularTotal() {
        order.setTotal(order.getProducts().stream().map(m -> m.getQuantity() * m.getPrice()).reduce(0.0, Double::sum) - order.getDiscountValue() + order.getDeliveryCost());
        System.out.println("TOTAL: " + order.getTotal());
    }

    public void removeCart(Item i) {
        order.getProducts().remove(i);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getQuantity() * m.getPrice()).reduce(0.0, Double::sum));
        calcularTotal();
    }

    public void registarPedido() {
        try {
            order.setDtRegister(new Date().toString());
            order.setStatus("Aguardando");
            order.setMerchant(new Merchant(company));
            categoriaService.sendOrder(order, idCompany);
            PrimeFaces.current().executeScript("confirm()");
            PrimeFaces.current().ajax().update("grpPrincipal");
            order = new Order();
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
        item.setQuantity(1D);
        item.setSku(product.getSku());
        item.setProduct(product);
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

}
