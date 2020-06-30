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
import com.popsales.model.CouponCode;
import com.popsales.model.Item;
import com.popsales.model.Merchant;
import com.popsales.model.Order;
import com.popsales.model.Product;
import com.popsales.model.dto.EnderecoDTO;
import com.popsales.services.CategoryServices;
import com.popsales.services.CompanyService;
import com.popsales.services.CouponService;
import com.popsales.services.OrderService;
import com.popsales.util.OUtils;
import com.popsales.util.PrimefacesUtil;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Renato
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class OrderMB implements Serializable {

    public Boolean home = true;
    public Boolean fechado = true;

    public String horario = "";
    private String ord;
    private String dia = "";
    String menuOnly = "0";

    private String bairroManual;
    private List<Bairro> bairros = new ArrayList();

    private List<EnderecoDTO> enderecos = new ArrayList();
    private EnderecoDTO enderecoFiltro;

    CategoryServices categoriaService = new CategoryServices();
    OrderService orderService = new OrderService();
    CompanyService companyService = new CompanyService();
    CouponService couponService = new CouponService();

    private String idCompany = null;
    private Company company = new Company();
    private List<Category> categories = new ArrayList();
    private List<Product> products = new ArrayList();
    private List<Product> productsPromo = new ArrayList();
    private List<CouponCode> coupons = new ArrayList();
    private Product product = new Product();
    private Category categorySelected = new Category();
    private Item item = new Item();
    private Order order = new Order();
    private CouponMB couponMB = new CouponMB();
    private List<AttributeValue>[] adicionais;
    private List<List<AttributeValue>> adicionaisArray = new ArrayList();
    private BigDecimal totalAdicionais = BigDecimal.ZERO;
    private String couponCode = "";
    private Boolean couponValid = false;
    private Order lastOrder = new Order();

    public OrderMB() {
        constructor();
    }

    public void constructor() {
        System.out.println("CONSTRUTOR");
        receber = true;
        enderecoFiltro = new EnderecoDTO();
        order = new Order();
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        if (name != null) {
            try {
                Company cop = categoriaService.loadCompanyName(name);
                if (cop != null) {
                    company = cop;
                    coupons = company.getCoupons();
                    idCompany = company.getId();
                    if (!company.getDeliveryOnly()) {
                        order.setDelivery(false);
                        order.setDeliveryCost(BigDecimal.ZERO);
                    }
                    if (!company.getWithdrawalOnly()) {
                        order.setDelivery(true);
                        if (company.getUniqueDeliveryCost()) {
                            order.setDeliveryCost(company.getDeliveryCost());
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(OrderMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            idCompany = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        }

        if (idCompany == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString().replace("index.jsf", "") + "notfound/";
            PrimeFaces.current().executeScript("window.location = '" + url + "';");
        } else {
            try {
                company = categoriaService.loadCompany(idCompany);
                coupons = company.getCoupons();
                idCompany = company.getId();
                menuOnly = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu");
                if (menuOnly != null && menuOnly == "1") {
                    company.setOnlyMeny(true);
                }

                tratarEstabelecimentoAberto();

                order.getAddress().setCity((company.getAddress() != null && company.getAddress().getCity() != null) ? company.getAddress().getCity() : "");
                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel") != null) {
                    String phone = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tel");
                    try {
                        System.out.println("telefoneeeeeeeeeeeeee " + OUtils.formataNinePhone(phone));
                        lastOrder = orderService.lastOrderByPhone(idCompany, phone);
                        System.out.println("date do ultimo pedido " + lastOrder.getDtRegister().substring(0, 10));
                        if (lastOrder != null) {
                            if (lastOrder.getAddress().getCity().equalsIgnoreCase(company.getAddress().getCity())) {
                                order.setAddress(lastOrder.getAddress());
                            } else {
                                order.getAddress().setCity(company.getAddress().getCity());
                            }
                            order.getClientInfo().setName(lastOrder.getClientInfo().getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    order.getClientInfo().setPhone(OUtils.formataNinePhone(phone));
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

            String horaAgora = new SimpleDateFormat("HH:mm").format(new Date());

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
                if (company.getTime().getOpenDom() == null && company.getTime().getCloseDom() == null) {
                    fechado = false;
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
                if (company.getTime().getOpenSeg() == null && company.getTime().getCloseSeg() == null) {
                    fechado = false;
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
                if (company.getTime().getOpenTer() == null && company.getTime().getCloseTer() == null) {
                    fechado = false;
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
                if (company.getTime().getOpenQua() == null && company.getTime().getCloseQua() == null) {
                    fechado = false;
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
                if (company.getTime().getOpenQui() == null && company.getTime().getCloseQui() == null) {
                    fechado = false;
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
                if (company.getTime().getOpenSex() == null && company.getTime().getCloseSex() == null) {
                    fechado = false;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenSex().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseSex().replace(":", "").trim());
                horario = company.getTime().getOpenSex() + " às " + company.getTime().getCloseSex();
            } else if (dia.equals("Sab") || dia.equals("Sat") || dia.equals("Sáb")) {
                if (!company.getTime().getSab()) {
                    fechado = true;
                    return;
                }
                if (company.getTime().getOpenSab() == null && company.getTime().getCloseSab() == null) {
                    fechado = false;
                    return;
                }
                horaAbertura = Integer.parseInt(company.getTime().getOpenSab().replace(":", "").trim());
                horaFechamento = Integer.parseInt(company.getTime().getCloseSab().replace(":", "").trim());
                horario = company.getTime().getOpenSab() + " às " + company.getTime().getCloseSab();
            }
            this.dia = dia;

            Integer horaA = Integer.parseInt(horaAgora.replace(":", "").trim());

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
        if (!p.getEnabled()) {
            return true;
        }
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
        System.out.println("POST CONSTRUC");
        loadCategorias();
        listaBairros();
    }

    public void preReload() {
        System.out.println("PRE RELOAD");
        String name = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
        String reloadID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");

        System.out.println("NAME: " + name);
        System.out.println("ID: " + reloadID);

        if ((name != null && company != null && company.getName() != null && !name.equals(company.getNameUrl())) || (name != null && (company == null || company.getName() == null))) {
            System.out.println("RECARREGAR");
            constructor();
            init();
        } else {

            if (reloadID != null && company != null && company.getId() != null && !reloadID.equals(company.getId())) {
                constructor();
                init();
                System.out.println("RECARREGAR");
            }
        }
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
            ex.printStackTrace();
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
        System.out.println("IS DELIVERY: " + order.getDelivery());
        if (order.getDelivery()) {
            if (company.getDeliveryCost() != null) {
                if (order.getAddress().getAuto() != null) {
                    if (order != null && order.getDelivery()) {
                        validarTaxaServico();
                    } else {
                        order.setDeliveryCost(BigDecimal.ZERO);
                    }
                    System.out.println("ORDER: " + order.getDeliveryCost());
                }
            } else {

            }
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
        if (item.getQuantity().doubleValue() <= 0 || item.getQuantity() == null) {
            item.setQuantity(BigDecimal.ZERO);
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

    private void calcularDescontoCupom(BigDecimal desconto) {
        BigDecimal discount = order.getTotal().multiply(desconto).divide(BigDecimal.valueOf(100));
        System.out.println("desconto no valor de " + discount);
        order.setDiscountValue(discount);
        order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()).subtract(discount));
    }

    public void removeCart(Item i) {
        order.getProducts().remove(i);
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
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
            if (order.getProducts().size() == 0 || order.getProducts().isEmpty()) {
                return;
            }

            order.setNum_order(genCodigo());
            if (order.getNum_order().length() <= 3) {
                PrimeFaces.current().executeScript("alerta('Algo deu errado, refaça o pedido!')");
                return;
            }
            if (order.getClientInfo() == null || order.getClientInfo().getName() == null || order.getClientInfo().getName().isEmpty()) {
                PrimeFaces.current().executeScript("alerta('Preencha os dados da entrega!')");
                PrimeFaces.current().executeScript("PF('wizardWidget').loadStep('personal', false)");
                return;
            }
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

            if (order.getDelivery() == null) {
                if (order.getAddress().getStreet() != null) {
                    order.setDelivery(Boolean.TRUE);
                } else {
                    order.setDelivery(Boolean.FALSE);
                }
            }

            if (order.getDelivery()) {
                validarTaxaServico();
            } else {
                order.setDeliveryCost(BigDecimal.ZERO);
            }

            if (order.getTroco() != null) {
                if (order.getTroco()) {
                    if (order.getTrocoPara() == null) {
                        order.setTroco(Boolean.FALSE);
                    }
                }
            }
            if (couponValid) {
                for (CouponCode c : company.getCoupons()) {
                    if (c.getSlug().equalsIgnoreCase(couponCode)) {
                        c.setCount(c.getCount() + 1);
                        companyService.saveCompany(company);
                    }
                }
            }
            if (!company.getFreeVersion()) {
                categoriaService.sendOrder(order, idCompany);
            }
            montarMensagemFinalizar();
            PrimefacesUtil.Update("grpScrips");
            System.out.println("CHEGOU AQUI");
            PrimeFaces.current().executeScript("finalizarPedido();");
            System.out.println("FINALIZOU");
            PrimeFaces.current().executeScript("PF('ldg').hide()");
            PrimeFaces.current().executeScript("PF('wizardWidget').loadStep('personal', false)");

            order = new Order();
            if (company.getAddress() != null && company.getAddress().getCity() != null) {
                order.getAddress().setCity(company.getAddress().getCity());
            }
            PrimeFaces.current().executeScript("finalizarPedido();");
            PrimeFaces.current().executeScript("PF('ldg').hide()");
            PrimeFaces.current().executeScript("PF('wizardWidget').loadStep('personal', false)");
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
        System.out.println(item.getTotalAds());
        if (item.getTotalAds() != null) {
            item.setTotal(item.getPrice().multiply(item.getQuantity()).add(item.getTotalAds()));
        } else {
            item.setTotal(item.getPrice().multiply(item.getQuantity()));
        }

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
        this.bairros = new ArrayList();
        if (company == null || company.getId() == null) {
            return new ArrayList();
        }
        if (company.getUniqueDeliveryCost()) {
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
            for (Bairro bairrosParam : company.getBairros()) {
                this.bairros.add(new Bairro(bairrosParam.getBairro(), bairrosParam.getTaxa()));
            }

        }
        System.out.println("TEM " + bairros.size());
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

        if (order.getAddress().getAuto() != null) {
            Optional<Bairro> find = bairros.stream().filter(c -> c.getBairro().equalsIgnoreCase(order.getAddress().getAuto())).findAny();
            if (find.isPresent()) {
                Bairro found = find.get();
                System.out.println("Bairro: " + found);
                order.setDeliveryCost(found.getTaxa());
                calcularTotal();
            }
        }
    }

    public List<EnderecoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoDTO> enderecos) {
        this.enderecos = enderecos;
    }

    public EnderecoDTO getEnderecoFiltro() {
        return enderecoFiltro;
    }

    public void setEnderecoFiltro(EnderecoDTO enderecoFiltro) {
        this.enderecoFiltro = enderecoFiltro;
    }

    public List<EnderecoDTO> pesquisarEndereco(String end) {
        PrimeFaces.current().executeScript("PF('ldg').show()");
        try {
            enderecos = categoriaService.buscarEndereco(end, company.getAddress().getState(), company.getAddress().getCity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrimeFaces.current().executeScript("PF('ldg').hide()");
        return enderecos;
    }

    public void selectEndereco() {
        order.getAddress().setStreet(enderecoFiltro.getTipo_logradouro() + " " + enderecoFiltro.getLogradouro());
        order.getAddress().setAuto(enderecoFiltro.getBairro());
        //PrimeFaces.current().executeScript("document.getElementById('input_frmFechar:numeroEnd').focus();");
        enderecoFiltro.getBairro();
        PrimeFaces.current().ajax().update("frmFechar:endereco");

        adicionarRemoverTaxa();
        validarTaxaServico();
        PrimeFaces.current().executeScript("$('.numberHome').focus()");
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getNewStep().equals("entrega") || event.getOldStep().equals("entrega")) {
            adicionarRemoverTaxa();
        }
        return event.getNewStep();
    }

    public void setarParaRetirada() {
        adicionarRemoverTaxa();
        order.setDelivery(false);

    }

    public String iconHome() {
        return "<i class='fa fa-home' style='color: #95c70d'></i>";
    }

    public String iconStore() {
        return "<i class='fa fa-store' style='color: #95c70d'></i>";
    }

    public String cardHome(Boolean bol) {
        return "<div class='card " + (bol == false ? "" : "box-selected") + "'  style='" + (bol == true ? "" : "    border: 1px solid #95c70d !important;\n"
                + "    background: white !important;\n"
                + "    color: #95c70d !important;\n"
                + "    font-weight: 800;") + "' >\n"
                + "                                                        <center>\n"
                + "                                                            <i class=\"fa fa-motorcycle fa-2x\"></i> \n"
                + "                                                        </center>\n"
                + "                                                        <div class=\"card-body\">\n"
                + "                                                            <center>\n"
                + "                                                                <p class=\"card-text\">Receber em casa</p>\n"
                + "                                                            </center>\n"
                + "                                                        </div>\n"
                + "                                                    </div>";
    }

    public String cardStore(Boolean bol) {
        return "<div class='card " + (bol == true ? "" : "box-selected") + "'  style='" + (bol == false ? "" : "border: 1px solid #95c70d !important;\n"
                + "    background: white !important;\n"
                + "    color: #95c70d !important;\n"
                + "    font-weight: 800;") + "' >\n"
                + "                                                        <center>\n"
                + "                                                            <i class=\"fa fa-store-alt fa-2x\"></i> \n"
                + "                                                        </center>\n"
                + "                                                        <div class=\"card-body\">\n"
                + "                                                            <center>\n"
                + "                                                                <p class=\"card-text\">Retirar na loja</p>\n"
                + "                                                            </center>\n"
                + "                                                        </div>\n"
                + "                                                    </div>";
    }

    public String cardDinheiro(Boolean bol) {
        return "<div class=\"col-xs-4\" style='    width: 100%;padding: 0margin: 0;'><div class='card2 " + (bol == true ? "box-selected" : "") + "'  style='" + (bol == false ? "" : "border: 1px solid #95c70d !important;\n"
                + "    background: white !important;\n"
                + "    color: #95c70d !important;\n"
                + "    font-weight: 800;") + "' >\n"
                + "                                                        <center>\n"
                + "                                                            <i class=\"far fa-money-bill-alt fa-2x\" style='margin-top: 5px;'></i> \n"
                + "                                                        </center>\n"
                + "                                                        <div class=\"card-body\">\n"
                + "                                                            <center>\n"
                + "                                                                <p class=\"card-text\">Dinheiro</p>\n"
                + "                                                            </center>\n"
                + "                                                        </div>\n"
                + "                                                    </div></div>";
    }

    public String cardCartaoCredito(Boolean bol, String type) {
        return " <div class=\"col-xs-4\" style='    width: 100%;padding: 0margin: 0;'><div class='card2 " + (bol == true ? "box-selected" : "") + "'  style='" + (bol == false ? "" : "border: 1px solid #95c70d !important;\n"
                + "    background: white !important;\n"
                + "    color: #95c70d !important;\n"
                + "    font-weight: 800;") + "' >\n"
                + "                                                        <center>\n"
                + "                                                            <i class=\"far fa-credit-card fa-2x\" style='margin-top: 5px;'></i> \n"
                + "                                                        </center>\n"
                + "                                                        <div class=\"card-body\">\n"
                + "                                                            <center>\n"
                + "                                                                <p class=\"card-text\">" + type + "</p>\n"
                + "                                                            </center>\n"
                + "                                                        </div>\n"
                + "                                                    </div></div>";
    }

    public String formatarMoedaTaxa(Bairro bairro) {
        StringBuilder content = new StringBuilder();
        content.append("<div class='row'>");
        content.append("<div class='col-xs-12'><strong>" + bairro.getBairro() + "</strong> </div>");
        if (bairro.getTaxa().doubleValue() == 0.00 || bairro.getTaxa() == null) {
            content.append("<div class='col-xs-12'><small style='    font-weight: bolder;color: #95c70d;'>Entrega Grátis</small> </div>");
        } else {
            content.append("<div class='col-xs-12'><small style='    font-weight: bolder;'>Taxa: " + OUtils.formatarMoeda(bairro.getTaxa().doubleValue()) + "</small></div>");
        }
        content.append("<div class='col-xs-12'><hr style='margin-top: 0;margin-bottom: 0;border-color: #95c70d;' /></div>");
        content.append("</div>");
        return content.toString();
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Boolean getCouponValid() {
        return couponValid;
    }

    public void setCouponValid(Boolean couponValid) {
        this.couponValid = couponValid;
    }

    public void applyCoupon() throws Exception {
        List<CouponCode> couponsCompany = new ArrayList();
        couponsCompany = couponService.couponsByCompany(company.getId());
        if (lastOrder != null) {
            if (couponMB.couponValid(couponsCompany, couponCode, lastOrder).equals("true")) {
                for (CouponCode c : company.getCoupons()) {
                    if (c.getSlug().equalsIgnoreCase(couponCode)) {
                        System.out.println(company.getCoupons().toString());
                        order.setCoupon(couponCode);
                        calcularDescontoCupom(c.getDiscount());
                        couponValid = true;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cupom aplicado!", "Cupom aplicado!"));
                        PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                        System.out.println("apliquei o cupom");
                    }
                }
            } else if (couponMB.couponValid(couponsCompany, couponCode, lastOrder).equals("onetime")) {
                couponValid = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom já foi utilizado hoje!", "Cupom já foi utilizado hoje!"));
                PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                couponCode = "";
            } else if (couponMB.couponValid(couponsCompany, couponCode, lastOrder).equals("expired")) {
                couponValid = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom atingiu o limite máximo de utilizações!", "Cupom atingiu o limite máximo de utilizações!"));
                PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                couponCode = "";
            } else {
                couponValid = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom inválido!", "Cupom inválido!"));
                PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                couponCode = "";
            }
        } else {
            if (couponMB.checkCouponNoOrder(couponsCompany, couponCode).equalsIgnoreCase("true")) {
                for (CouponCode c : company.getCoupons()) {
                    if (c.getSlug().equalsIgnoreCase(couponCode)) {
                        System.out.println(company.getCoupons().toString());
                        order.setCoupon(couponCode);
                        calcularDescontoCupom(c.getDiscount());
                        couponValid = true;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cupom aplicado!", "Cupom aplicado!"));
                        PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    }
                }
            } else {
                couponValid = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom inválido!", "Cupom inválido!"));
                PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                couponCode = "";
            }
        }
    }

    public List<CouponCode> getCoupons() {
        if (coupons == null) {
            coupons = new ArrayList();
        }
        return coupons;
    }

    public void setCoupons(List<CouponCode> coupons) {
        this.coupons = coupons;
    }

    private String msg = "";

    public void montarMensagemFinalizar() {
        StringBuilder sb = new StringBuilder();
        sb.append(imprimirOrderControle(order));
        msg = sb.toString();
        System.out.println("MSG MONTADA:: " + msg);
    }

    public String getMsg() {
        return msg;
    }

    public static final String LS = "------------------------------------------ linebr";
    public static final String LSW = "------------------------------------------";
    public static final String LD = "========================================== linebr";
    public static final String formatQntity = "%1$-3s %2$-24s linebr";
    public static final String format = "%1$-10s %2$-24s linebr";

    public static String imprimirOrderControle(Order or) {

        StringBuilder sb = new StringBuilder();
        sb.append("====================DETALHE==================== linebr linebr");
        sb.append("PEDIDO : ").append(or.getNum_order()).append("linebr");
        sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("linebr");
        sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("linebr");
        sb.append("DATA HORA: ").append(or.getDtRegister()).append("linebr");

        sb.append("====================ITENS==================== linebr");
        or.getProducts().forEach(pp -> {
            sb.append(String.format(formatQntity, pp.getQuantity() + " x ", pp.getName().toUpperCase()));
            if (pp.getFlavors() != null && pp.getFlavors().size() > 0) {
                sb.append(pp.getFlavors().stream().map(m -> "   1/" + pp.getFlavors().size() + " " + m.getFlavor()).collect(Collectors.joining("linebr"))).append("linebr");
            }

            if (pp.getAttributes() != null) {
                for (Attribute at : pp.getAttributes()) {
                    sb.append("   ").append(at.getDescription());
                    for (AttributeValue va : at.getValues()) {
                        sb.append(" linebr      ").append(va.getQuantity()).append(" x ").append(va.getName());
                    }
                    sb.append("linebr");
                }
            }
            if (pp.getObs().length() > 0) {
                sb.append("\t").append(pp.getObs()).append("linebr");

            }
        });
        sb.append("====================TOTAIS==================== linebr");
        sb.append("PRODUTOS: ").append(OUtils.formatarMoeda(or.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue())).append("linebr");
        sb.append("TAXA ENTREGA: ").append(OUtils.formatarMoeda(or.getDeliveryCost().doubleValue())).append("linebr");
        sb.append("TOTAL: ").append(OUtils.formatarMoeda(or.getTotal().doubleValue())).append("linebr");
        sb.append("FORMA: ").append(or.getForma()).append("linebr");

        if (or.getDelivery()) {
            sb.append("====================ENDERECO================== linebr").append(or.getAddress().getStreet() + " - " + or.getAddress().getStreetNumber()).append("linebr");
            if (!or.getAddress().getSuburb().isEmpty()) {
                sb.append(or.getAddress().getSuburb()).append("linebr");
            }
            sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("linebr");
        } else {
            sb.append("RETIRADA EM BALCAO").append(" linebr linebr linebr");
        }
        if (or.getForma().equalsIgnoreCase("Dinheiro")) {
            if (or.getTroco()) {
                sb.append(" linebrLEVAR TROCO PARA ").append(OUtils.formatarMoeda(or.getTrocoPara())).append(" linebr linebr");
            }
        } else {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            sb.append(" linebr LEVAR MARQUINA DE CARTAO!").append(" linebr linebr");
        }
        return sb.toString();

    }

}
