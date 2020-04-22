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
import java.text.SimpleDateFormat;
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

    public Boolean home = true;
    public Boolean fechado = true;

    CategoryServices categoriaService = new CategoryServices();

    private String idCompany = null;
    private Company company = new Company();
    private List<Category> categories = new ArrayList();
    private List<Product> products = new ArrayList();
    private List<Product> productsPromo = new ArrayList();
    private Product product = new Product();
    private Category categorySelected = new Category();
    private Item item = new Item();
    private Order order = new Order();
    private List<AttributeValue>[] adicionais;
    private List<List<AttributeValue>> adicionaisArray = new ArrayList();
    private BigDecimal totalAdicionais = BigDecimal.ZERO;

    public OrderMB() {
        receber = true;

        idCompany = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (idCompany == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString().replace("index.jsf", "") + "notfound/";
            PrimeFaces.current().executeScript("window.location = '" + url + "';");
        } else {
            try {
                company = categoriaService.loadCompany(idCompany);
                tratarEstabelecimentoAberto();
                order.setDeliveryCost(company.getDeliveryCost());
                order.getAddress().setCity((company.getAddress() != null && company.getAddress().getCity() != null) ? company.getAddress().getCity() : "");

            } catch (Exception e) {
            }
        }

    }

    public void tratarEstabelecimentoAberto() {
        String dia = new SimpleDateFormat("EE").format(new Date());
        System.out.println("DIA: " + dia);
        String horaAgora = new SimpleDateFormat("HH").format(new Date());
        System.out.println("HORA: " + horaAgora);

        Integer horaAbertura = 0;
        Integer horaFechamento = 0;

        if (company.getTime() == null) {
            fechado = false;
            return;
        }

        if (dia.equals("Dom") || dia.equals("Sun")) {
            if (!company.getTime().getDom()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenDom();
            horaFechamento = company.getTime().getCloseDom();
        } else if (dia.equals("Seg") || dia.equals("Mon")) {
            if (!company.getTime().getSeg()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenSeg();
            horaFechamento = company.getTime().getCloseSeg();
        } else if (dia.equals("Ter") || dia.equals("Tue")) {
            if (!company.getTime().getTer()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenTer();
            horaFechamento = company.getTime().getCloseTer();
        } else if (dia.equals("Qua") || dia.equals("Wed")) {
            if (!company.getTime().getQua()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenQua();
            horaFechamento = company.getTime().getCloseQua();
        } else if (dia.equals("Qui") || dia.equals("Thu")) {
            if (!company.getTime().getQui()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenQui();
            horaFechamento = company.getTime().getCloseQui();
        } else if (dia.equals("Sex") || dia.equals("Fri")) {
            if (!company.getTime().getSex()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenSex();
            horaFechamento = company.getTime().getCloseSex();
        } else if (dia.equals("Sab") || dia.equals("Sat")) {
            if (!company.getTime().getSab()) {
                fechado = true;
                return;
            }
            horaAbertura = company.getTime().getOpenSab();
            horaFechamento = company.getTime().getCloseSab();
        } 
        
        System.out.println("");
        
        Integer horaA = Integer.parseInt(horaAgora);

        System.out.println("1 "+ (horaA >= horaAbertura && horaFechamento < 0));
        System.out.println("2 "+ (horaA >= horaAbertura && horaFechamento < 0));
        if (horaA >= horaAbertura && horaFechamento < 0) {
            fechado = false;
            return;
        } else {
            if (Integer.parseInt(horaAgora) >= horaAbertura && Integer.parseInt(horaAgora) <= horaFechamento) {
                fechado = false;
            } else {
                fechado = true;
            }

        }

    }

    @PostConstruct
    public void init() {

        loadCategorias();
    }

    private void loadCategorias() {
        try {
            if (idCompany == null) {
            }
            categories = categoriaService.getCategoryList(idCompany);
            productsPromo = categoriaService.getProductsPromo(idCompany);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Boolean receber, retirar;

    public void listener(String ori) {
        if (ori.equals("receber")) {
            order.setDelivery(true);
            retirar = false;
            receber = true;
        } else {
            order.setDelivery(false);
            retirar = true;
            receber = false;
        }
        adicionarRemoverTaxa();
    }

    public void adicionarRemoverTaxa() {
        if (order != null && order.getDelivery()) {
            order.setDeliveryCost(company.getDeliveryCost());
        } else {
            order.setDeliveryCost(BigDecimal.ZERO);
        }
        calcularTotal();
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
        item.setAttributesValues(new ArrayList());
        for (List<AttributeValue> list : adicionais) {
            item.getAttributesValues().addAll(list);
        }
        if (item.getProduct().getCategoryMain().getName().equals("Pizzas")) {

        } else {
            item.setPrice(item.getProduct().getPrice());
            item.setTotalAds(totalAdicionais);
            item.setTotal(item.getPrice().multiply(item.getQuantity()).add(item.getTotalAds()));
        }
        order.getProducts().add(item);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        calcularTotal();
        item = new Item();
        adicionais = new ArrayList[0];
        totalAdicionais = BigDecimal.ZERO;
        PrimeFaces.current().executeScript(" $('#outPutTotal').text('" + formatarMoeda(order.getSubtotal().doubleValue()) + "').fadeIn();");
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
            String foneCod = foneNoMask.substring(foneNoMask.length() - 4, foneNoMask.length()) + new Random().nextInt((9 - 1) + 1) + 0;
            return nome + "-" + foneCod;
        } catch (Exception e) {
            return new Random().nextInt(7) + "";
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
        adicionaisArray = new ArrayList();

        adicionais = new ArrayList[product.getAttributes() != null ? product.getAttributes().size() : 0];

        if (adicionais.length > 0) {
            for (int i = 0; i < product.getAttributes().size(); i++) {
                List<AttributeValue> saida = new ArrayList<>();
                adicionaisArray.add(saida);
            }
        }
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

    public List<AttributeValue>[] getAdicionais() {
        return adicionais;
    }

    public void setAdicionais(List<AttributeValue>[] adicionais) {
        this.adicionais = adicionais;
    }

    public BigDecimal getTotalAdicionais() {
        return totalAdicionais;
    }

    public void setTotalAdicionais(BigDecimal totalAdicionais) {
        this.totalAdicionais = totalAdicionais;
    }

    public void processarTotalPizza() {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            return;
        }
        System.out.println("REGRA: " + item.getProduct().getRulePricePizza());
        if (item.getProduct().getPromo()) {
            item.setPrice(item.getProduct().getPrice());
            item.setTotal(item.getPrice());
            return;
        }
        if (item.getProduct().getRulePricePizza().equals("Média")) {
            item.setPrice(item.getFlavors().stream().map(m -> m.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(item.getFlavors().size())));
        } else {
            item.setPrice(item.getFlavors().stream().map(m -> m.getPrice()).max((BigDecimal o1, BigDecimal o2) -> o1.compareTo(o2)).get());
        }
        System.out.println("PRICE: " + item.getPrice());
        item.setTotal(item.getPrice());

    }

    public void processarTotalAdicionais() {
        if (item == null || item.getPrice() == null || item.getQuantity() == null) {
            return;
        }
        System.out.println("CHANGE: ");
        totalAdicionais = BigDecimal.ZERO;
        for (List<AttributeValue> adicionai : adicionais) {
            if (adicionai != null && adicionai.size() > 0) {
                for (AttributeValue av : adicionai) {
                    totalAdicionais = totalAdicionais.add(av.getPrice().multiply(item.getQuantity()));
                }
            }
        }

        System.out.println("totalAdicionais: " + totalAdicionais);
        item.setTotalAds(totalAdicionais);
        item.setTotal((item.getPrice().multiply(item.getQuantity())).add(item.getTotalAds()));
        PrimeFaces.current().ajax().update("frmModal:grpValue");
    }

    public void change() {
        processarTotalAdicionais();
    }

    public List<Product> getProductsPromo() {
        return productsPromo;
    }

    public void setProductsPromo(List<Product> productsPromo) {
        this.productsPromo = productsPromo;
    }

    public Boolean getReceber() {
        return receber;
    }

    public void setReceber(Boolean receber) {
        this.receber = receber;
    }

    public Boolean getRetirar() {
        return retirar;
    }

    public void setRetirar(Boolean retirar) {
        this.retirar = retirar;
    }

    public List<List<AttributeValue>> getAdicionaisArray() {
        return adicionaisArray;
    }

    public void setAdicionaisArray(List<List<AttributeValue>> adicionaisArray) {
        this.adicionaisArray = adicionaisArray;
    }

    public Boolean getHome() {
        return home;
    }

    public void setHome(Boolean home) {
        try {

            Thread.sleep(1000);

        } catch (Exception e) {
        }
        this.home = home;
    }

    public Boolean getFechado() {
        return fechado;
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }

}
