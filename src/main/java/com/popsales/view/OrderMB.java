/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
import com.popsales.model.Bairro;
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
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

    public String horario = "";
    private String ord;
    private String dia = "";

    private String bairroManual;
    private List<Bairro> bairros = new ArrayList();
    private List<Bairro> bairrosParams = new ArrayList();

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
        System.out.println("INIT");

        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        System.out.println("NAME: "+name);
        if (name != null) {
            try {
                Company cop = categoriaService.loadCompanyName(name);
                if (cop != null) {
                    company = cop;
                    idCompany = company.getId();
                }
            } catch (Exception ex) {
                Logger.getLogger(OrderMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            idCompany = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        }
        System.out.println("idCompany: " + idCompany);
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
                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel") != null) {
                    String phone = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel");
                    order.getClientInfo().setPhone(OUtils.formataNinePhone(phone));
                }
                if (company.getBairros() != null) {
                    bairrosParams = company.getBairros();
                }
            } catch (Exception e) {
            }
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("order") != null) {
            ord = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("order");
        }
    }

    public void tratarEstabelecimentoAberto() {
        try {
            String dia = new SimpleDateFormat("EE").format(new Date());

            System.out.println("DIA: " + dia);
            String horaAgora = new SimpleDateFormat("HH:mm").format(new Date());
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
                horaAbertura = Integer.parseInt(company.getTime().getOpenDom().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseDom().replace(":", "").trim());
                horario = company.getTime().getOpenDom() + " às " + company.getTime().getCloseDom();
            } else if (dia.equals("Seg") || dia.equals("Mon")) {
                if (!company.getTime().getSeg()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenSeg().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseSeg().replace(":", "").trim());
                horario = company.getTime().getOpenSeg() + " às " + company.getTime().getCloseSeg();
            } else if (dia.equals("Ter") || dia.equals("Tue")) {
                if (!company.getTime().getTer()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenTer().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseTer().replace(":", "").trim());
                horario = company.getTime().getOpenTer() + " às " + company.getTime().getCloseTer();
            } else if (dia.equals("Qua") || dia.equals("Wed")) {
                if (!company.getTime().getQua()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenQua().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseQua().replace(":", "").trim());
                horario = company.getTime().getOpenQua() + " às " + company.getTime().getCloseQua();
            } else if (dia.equals("Qui") || dia.equals("Thu")) {
                if (!company.getTime().getQui()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenQui().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseQui().replace(":", "").trim());
                horario = company.getTime().getOpenQui() + " às " + company.getTime().getCloseQui();
            } else if (dia.equals("Sex") || dia.equals("Fri")) {
                if (!company.getTime().getSex()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenSex().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseSex().replace(":", "").trim());
                horario = company.getTime().getOpenSex() + " às " + company.getTime().getCloseSex();
            } else if (dia.equals("Sab") || dia.equals("Sat") || dia.equals("Sáb") ) {
                if (!company.getTime().getSab()) {
                    fechado = true;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenSab().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseSab().replace(":", "").trim());
                horario = company.getTime().getOpenSab() + " às " + company.getTime().getCloseSab();
            }
            this.dia = dia;

            System.out.println("");

            Integer horaA = Integer.parseInt(horaAgora.replace(":", "").trim());

            System.out.println("1 " + (horaA >= horaAbertura && horaFechamento < horaAbertura));
            if (horaA >= horaAbertura && horaFechamento < horaAbertura) {
                fechado = false;
                return;
            } else {
                if (horaA >= horaAbertura && horaA <= horaFechamento) {
                    fechado = false;
                } else {
                    fechado = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            fechado = false;
        }

    }

    public Boolean productoIsNotVendidoDia(Product p) {
        if (p.getProductDay() == null) {
            return false;
        }
        if (dia.equals("Dom") || dia.equals("Sun")) {
            return p.getProductDay().getNaoVenderDom() == true;
        } else if (dia.equals("Seg") || dia.equals("Mon")) {
            return p.getProductDay().getNaoVenderSeg() == true;

        } else if (dia.equals("Ter") || dia.equals("Tue")) {
            return p.getProductDay().getNaoVenderTer() == true;
        } else if (dia.equals("Qua") || dia.equals("Wed")) {
            return p.getProductDay().getNaoVenderQua() == true;
        } else if (dia.equals("Qui") || dia.equals("Thu")) {
            return p.getProductDay().getNaoVenderQui() == true;
        } else if (dia.equals("Sex") || dia.equals("Fri")) {
            return p.getProductDay().getNaoVenderSex() == true;
        } else if (dia.equals("Sab") || dia.equals("Sat")) {
            return p.getProductDay().getNaoVenderSab() == true;
        } else {
            return false;
        }
    }

    @PostConstruct
    public void init() {

        loadCategorias();
        listaBairros();
    }

    private void loadCategorias() {
        try {
            if (idCompany == null) {
            }
            categories = categoriaService.getCategoryList(idCompany);
            productsPromo = categoriaService.getProductsPromo(idCompany);
        } catch (Exception ex) {
            System.out.println("NAO FOI POSSIVEL CARREGAR O ID COMPANY");
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
        if (item.getProduct().getCategoryMain().getName().equals("Pizzas") && item.getFlavors() != null && item.getFlavors().size() == 0 && item.getProduct().getFlavorsPizza().size() > 0) {
            PrimeFaces.current().executeScript("alerta('Selecione o sabor de pizza!')");
            return;
        }
        if (item.getTotal().doubleValue() > 0) {
            if (order.getProducts() == null) {
                order.setProducts(new ArrayList());
            }
            //REPROCESSAMENTO DOS ATRIBUTOS, CASO NAO SEJA LANÇADO O ITEM NÃO SERA ENVIADO!
            BigDecimal totalAdicionais = BigDecimal.ZERO;
            List<Attribute> atrs = new ArrayList();
            if (item.getAttributes() != null) {
                for (Attribute atr : item.getAttributes()) {
                    List<AttributeValue> novos = new ArrayList();
                    for (AttributeValue value : atr.getValues()) {
                        if (value.getQuantity() != null && value.getQuantity().doubleValue() > 0) {
                            novos.add(value);
                            if (value.getTotal() != null) {
                                totalAdicionais = totalAdicionais.add(value.getTotal());
                            }
                        }
                    }
                    atr.setValues(novos);
                    if (atr.getValues().size() > 0) {

                        atrs.add(atr);
                    }
                }
            }

            item.setAttributes(atrs);
            System.out.println("TOTAL ADS: " + totalAdicionais);
            item.setTotalAds(totalAdicionais);
            //item.setPrice(item.getProduct().getPrice());
            //item.setTotal(item.getPrice().multiply(item.getQuantity()).add(item.getTotalAds()));

            order.getProducts().add(item);
            order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
            calcularTotal();

            totalAdicionais = BigDecimal.ZERO;
            PrimeFaces.current().executeScript("closeModal('mdlPedir')");
            PrimeFaces.current().executeScript("effect('ima-" + item.getProduct().getId() + "')");
            item = new Item();
            adicionais = new ArrayList[0];
        } else {
            PrimeFaces.current().executeScript("alerta('Selecione as opções!')");
        }

    }

    private void calcularTotal() {
        order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()));
    }

    public void removeCart(Item i) {
        order.getProducts().remove(i);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        System.out.println("SIZE: " + order.getProducts().size());
        calcularTotal();
        PrimeFaces.current().ajax().update("outPutTotal");
        PrimeFaces.current().ajax().update("grpNumbers");
        PrimeFaces.current().ajax().update("frmMySide");
        PrimeFaces.current().ajax().update("frmFechar:btnSEND");
    }

    public String genCodigo() {
        try {
            String nome = order.getClientInfo().getName().substring(0, 2);
            String foneNoMask = order.getClientInfo().getPhone().replace("-", "").replace("(", "").replace(")", "");
            String foneCod = foneNoMask.substring(foneNoMask.length() - 4, foneNoMask.length()) + new Random().nextInt(9);
            return nome + "-" + foneCod;
        } catch (Exception e) {
            return new Random().nextInt(7) + "";
        }
    }

    public void registarPedido() {
        try {
            System.out.println("SIZ11111111111E: " + order.getProducts().size());
            if (order.getProducts().size() == 0 || order.getProducts().isEmpty()) {
                System.out.println("alerta");
                return;
            }
            order.setNum_order(genCodigo());
            order.setDtRegister(OUtils.formataData(new Date(), "yyyy-MM-dd HH:mm:ss"));
            order.setStatus("Aguardando");
            order.setMerchant(new Merchant(company));
            for (Item it : order.getProducts()) {
                Product novo = new Product();
                novo.setId(it.getProduct().getId());
                novo.setSku(it.getProduct().getSku());
                novo.setPrice(it.getPrice());
                novo.setName(it.getName());
                it.setProduct(novo);
            }

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
        item.setAttributesValues(new ArrayList());
        item.setAttributes(new ArrayList());
        if (product.getAttributes() != null) {
            for (Attribute at : product.getAttributes()) {
                item.getAttributes().add(new Attribute(at));
            }
        }
        adicionaisArray = new ArrayList();
        item.setPrice(product.getPrice());
        item.setTotal(product.getPrice());
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

    public void addQnt(AttributeValue av, Attribute at) {
        av.setQuantity(av.getQuantity().add(BigDecimal.ONE));
        av.setTotal(av.getQuantity().multiply(av.getPrice()));
        atingiuMaximo(at);
    }

    public void minQnt(AttributeValue av, Attribute at) {
        if (av.getQuantity().intValue() < 0) {
            av.setQuantity(BigDecimal.ZERO);
            return;
        }
        av.setQuantity(av.getQuantity().subtract(BigDecimal.ONE));
        av.setTotal(av.getQuantity().multiply(av.getPrice()));
        atingiuMaximo(at);
    }

    public void atingiuMaximo(Attribute at) {
        BigDecimal bd = at.getValues().stream().map(m -> m.getQuantity()).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (at.getQuantity() > bd.intValue()) {
            at.setTravado(false);
        } else {
            at.setTravado(true);
        }
        processarTotaisAdcs();

    }

    public void processarTotaisAdcs() {
        //item.setTotalAds(item.getAttributes().stream().filter(mm -> mm.getValues() != null).map(m -> m.getValues()).findAny().get().stream().filter(c -> c != null && c.getTotal() != null).map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal totalAds = BigDecimal.ZERO;
        if (item.getAttributes() != null) {
            for (Attribute at : item.getAttributes()) {
                if (at.getValues() != null && at.getValues().size() > 0) {
                    for (AttributeValue av : at.getValues()) {
                        if (av.getTotal() != null) {
                            System.out.println(av.getName() + ": " + av.getQuantity() + " * " + av.getPrice() + " = " + av.getTotal());
                            totalAds = totalAds.add(av.getTotal());
                        }
                    }
                }
            }
        }
        item.setTotalAds(totalAds);
        BigDecimal total = (item.getPrice().add(item.getTotalAds())).multiply(item.getQuantity());
        item.setTotal(total);
        PrimeFaces.current().ajax().update("frmModal:grp");
    }

    public String formatarMoeda(Double val) {
        if (val == null) {
            return "R$ 0,00";
        }
        try {

            NumberFormat nF = NumberFormat.getCurrencyInstance();
            nF.setCurrency(Currency.getInstance(new Locale("pt", "BR")));
            return nF.format(val).replace("BRL", "R$ ");
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
        if (item.getFlavors() != null && item.getFlavors().size() > 0) {
            if (item.getProduct().getRulePricePizza().equals("Média")) {
                item.setPrice(item.getFlavors().stream().map(m -> m.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(item.getFlavors().size())));
            } else {
                item.setPrice(item.getFlavors().stream().map(m -> m.getPrice()).max((BigDecimal o1, BigDecimal o2) -> o1.compareTo(o2)).get());
            }
        } else {
            item.setPrice(BigDecimal.ZERO);
        }
        System.out.println("PRICE: " + item.getPrice());
        item.setTotal(item.getPrice().multiply(item.getQuantity()));

    }

    public void change() {
        processarTotaisAdcs();
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<Bairro> listaBairros() {

        if (company.getDeliveryCost() != null) {
            if (company.getAddress().getCity() != null) {
                try {
                    List<String> bairros = categoriaService.getBairros(company.getAddress().getCity());
                    for (String bairro : bairros) {
                        this.bairros.add(new Bairro(bairro, company.getDeliveryCost()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Bairro bairrosParam : bairrosParams) {
                this.bairros.add(new Bairro(bairrosParam.getBairro(), bairrosParam.getTaxa()));
            }

        }
        return bairros;
    }

    public String getBairroManual() {
        return bairroManual;
    }

    public void setBairroManual(String bairroManual) {
        this.bairroManual = bairroManual;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }

    public void adicionarBairro() {

    }

    public void validarTaxaServico() {
        System.out.println("CALL");
        if (company.getDeliveryCost() == null) {
            System.out.println("CALL 1");
            if (order.getAddress().getAuto() != null) {
                System.out.println("CALL 2");
                Optional<Bairro> find = bairrosParams.stream().filter(c -> c.getBairro().equalsIgnoreCase(order.getAddress().getAuto())).findAny();
                if (find.isPresent()) {
                    order.setDeliveryCost(find.get().getTaxa());
                    calcularTotal();
                }
            }
        }
    }

}
