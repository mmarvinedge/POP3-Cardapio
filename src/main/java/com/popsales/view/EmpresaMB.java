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
import com.popsales.util.JSFUtil;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    private Boolean phoneParam = false;
    private String horaAbertura, horaFechamento;
    
    

    private JSFUtil util = new JSFUtil();

    public EmpresaMB() {
        carregarCompany();
        carregarCategorias();
        carregarProdutos(null);
    }

    private void carregarCompany() {
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        String phone = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel");
        if (phone != null) {
            this.phone = phone;
            this.phoneParam = true;
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
                if (menu) {
                    productsPromo = categoriaService.getProductsMenuPromo(company.getId());
                } else {
                    productsPromo = categoriaService.getProductsPromo(company.getId());
                }
            } else {
                categorySelected = cat;
                if (menu) {
                    products = categoriaService.getProductsMenu(company.getId(), cat.getId());
                } else {
                    products = categoriaService.getProducts(company.getId(), cat.getId());
                }
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
//            bairros = categoriaService.getBairros(company.getAddress().getCity());
//            for (String b : bairros) {
//                if (company.getDeliveryCost() == null) {
//                    company.getBairros().add(new Bairro(b, BigDecimal.ZERO, true));
//                } else {
//                    company.getBairros().add(new Bairro(b, company.getDeliveryCost(), true));
//                }
//            }
        }
        company.setBairros(company.getBairros().stream().filter(b -> b.getEntrega()).collect(Collectors.toList()));
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

    public Boolean getPhoneParam() {
        return phoneParam;
    }

    public void setPhoneParam(Boolean phoneParam) {
        this.phoneParam = phoneParam;
    }

    public Boolean validaTimes() {
        if (company.getTime() != null) {
            String dia = new SimpleDateFormat("EE").format(new Date());
            switch (dia) {
                case "Seg":
                case "Mon":
                    if (company.getTime().getOpenSeg() != null && company.getTime().getCloseSeg()!= null) {
                        horaAbertura = company.getTime().getOpenSeg();
                        horaFechamento = company.getTime().getCloseSeg();
                        return true;
                    }
                    break;
                case "Ter":
                case "Tue":
                    if (company.getTime().getOpenTer()!= null && company.getTime().getCloseTer()!= null) {
                        horaAbertura = company.getTime().getOpenTer();
                        horaFechamento = company.getTime().getCloseTer();
                        return true;
                    }
                    break;
                case "Qua":
                case "Wed":
                    if (company.getTime().getOpenQua()!= null && company.getTime().getCloseQua()!= null) {
                        horaAbertura = company.getTime().getOpenQua();
                        horaFechamento = company.getTime().getCloseQua();
                        return true;
                    }
                    break;
                case "Qui":
                case "Thu":
                    if (company.getTime().getOpenQui()!= null && company.getTime().getCloseQui()!= null) {
                        horaAbertura = company.getTime().getOpenQui();
                        horaFechamento = company.getTime().getCloseQui();
                        return true;
                    }
                    break;
                case "Sex":
                case "Fri":
                    if (company.getTime().getOpenSex()!= null && company.getTime().getCloseSex()!= null) {
                        horaAbertura = company.getTime().getOpenSex();
                        horaFechamento = company.getTime().getCloseSex();
                        return true;
                    }
                    break;
                case "Sab":
                case "Sáb":
                case "Sat":
                    if (company.getTime().getOpenSab()!= null && company.getTime().getCloseSab()!= null) {
                        horaAbertura = company.getTime().getOpenSab();
                        horaFechamento = company.getTime().getCloseSab();
                        return true;
                    }
                    break;
                case "Dom":
                case "Sun":
                    if (company.getTime().getOpenDom() != null && company.getTime().getCloseDom() != null) {
                        horaAbertura = company.getTime().getOpenDom();
                        horaFechamento = company.getTime().getCloseDom();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public String getHoraAbertura() {
        return horaAbertura;
    }

    public void setHoraAbertura(String horaAbertura) {
        this.horaAbertura = horaAbertura;
    }

    public String getHoraFechamento() {
        return horaFechamento;
    }

    public void setHoraFechamento(String horaFechamento) {
        this.horaFechamento = horaFechamento;
    }
    
}
