/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Bairro;
import com.popsales.model.Category;
import com.popsales.model.Company;
import com.popsales.model.Product;
import com.popsales.services.CategoryServices;
import com.popsales.services.CompanyService;
import com.popsales.services.CouponService;
import com.popsales.services.OrderService;
import com.popsales.util.JSFUtil;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Renato
 */
@ManagedBean
@ViewScoped
public class EmpresaMB implements Serializable {

    private CategoryServices categoriaService = new CategoryServices();
    private CompanyService companyService = new CompanyService();

    private Company company = new Company();
    private Category categorySelected = new Category();
    //pedidos ou menu
    private Boolean menu = false;
    private String phone = "";

    private List<Category> categories = new ArrayList();
    private List<Product> products = new ArrayList();
    private List<Product> productsPromo = new ArrayList();

    private JSFUtil util = new JSFUtil();

    public EmpresaMB() {
        carregarCompany();
        carregarCategorias();
        carregarProdutos(null);
    }

    private void carregarCompany() {
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        String phone = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel");
        System.out.println("PHONE:? " + phone);
        if (phone != null) {
            this.phone = phone;
        }
        if (name != null) {
            try {
                this.company = companyService.loadCompanyName(name);
            } catch (Exception e) {
                e.printStackTrace();
                this.company = null;
            }
        } else {
            this.company = null;
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString().replace("index.jsf", "") + "notfound/";
            PrimeFaces.current().executeScript("window.location = '" + url + "';");
        }
        String menu = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu");
        if (menu != null) {
            this.menu = true;
        }

    }

    private void carregarCategorias() {
        try {
            categories = categoriaService.getCategoryList(company.getId());
        } catch (Exception e) {
            System.err.println("Erro ao carregar categorias");
            e.printStackTrace();
        }
    }

    public void carregarProdutos(Category cat) {
        try {
            if (cat == null) {
                productsPromo = categoriaService.getProductsPromo(company.getId());
            } else {
                categorySelected = cat;
                products = categoriaService.getProducts(company.getId(), cat.getId());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar os produtos!");
            e.printStackTrace();
        }
    }

    public Company getCompany() throws IOException {
        if (company.getBairros() == null || company.getBairros().size() == 0) {
            List<String> bairros = new ArrayList();
            company.setBairros(new ArrayList());
            bairros = categoriaService.getBairros(company.getAddress().getCity());
            for (String b : bairros) {
                if (company.getDeliveryCost() == null) {
                    company.getBairros().add(new Bairro(b, BigDecimal.ZERO));
                } else {
                    company.getBairros().add(new Bairro(b, company.getDeliveryCost()));
                }
            }
        }
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Boolean getMenu() {
        return menu;
    }

    public void setMenu(Boolean menu) {
        this.menu = menu;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public JSFUtil getUtil() {
        return util;
    }

    public void carregarEmpresa(Company company) {
        company = this.company;
    }

    public String getStart() {
        return "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Product> getProductsPromo() {
        return productsPromo;
    }

    public void setProductsPromo(List<Product> productsPromo) {
        this.productsPromo = productsPromo;
    }

}
