/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
import com.popsales.model.Bairro;
import com.popsales.model.Company;
import com.popsales.model.CouponCode;
import com.popsales.model.Item;
import com.popsales.model.Merchant;
import com.popsales.model.Order;
import com.popsales.model.Product;
import com.popsales.model.dto.EnderecoDTO;
import com.popsales.services.CompanyService;
import com.popsales.services.CouponService;
import com.popsales.services.OrderService;
import com.popsales.util.OUtils;
import com.popsales.util.PrimefacesUtil;
import static com.popsales.view.OrderMB.imprimirOrderControle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author Renato
 */
@ManagedBean
@SessionScoped
public class PedidoMB implements Serializable {

    private OrderService orderService = new OrderService();
    private CompanyService compService = new CompanyService();
    CouponService couponService = new CouponService();
    private CouponMB couponMB = new CouponMB();

    private Order lastOrder = new Order();
    private Order order;
    private Company company = new Company();

    private Product product = new Product();
    private Item item = new Item();
    private List<Item> products = new ArrayList<>();
    private List<EnderecoDTO> enderecos = new ArrayList();
    private EnderecoDTO enderecoFiltro = new EnderecoDTO();

    private Boolean couponValid = false;
    private String couponCode = "";

    private String telefone = "";

    private Boolean finalizado = false;

    public PedidoMB() {
        order = new Order();
    }

    private void carregarUltimoPedidoNumero() {
        if (telefone != null) {
            try {
                lastOrder = orderService.lastOrderByPhone(company.getId(), telefone);
                if (lastOrder != null && lastOrder.getId() != null) {
                    order.setClientInfo(lastOrder.getClientInfo());
                    order.setAddress(lastOrder.getAddress());
                }
            } catch (Exception e) {
                System.err.println("Erro ao capturar o ultimo pedido");
                e.printStackTrace();
                System.out.println(OUtils.formataNinePhone(telefone));
                order.getClientInfo().setPhone(OUtils.formataNinePhone(telefone));
            }

        }
    }

    public void setProduct(Product product, Company comp) {
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
        item.setPrice(product.getPrice());
        item.setTotal(product.getPrice());
        this.company = comp;
    }

    public void addQnt(AttributeValue av, Attribute at) {
        av.setQuantity(av.getQuantity().add(BigDecimal.ONE));
        av.setTotal(av.getQuantity().multiply(av.getPrice()));
        System.out.println(av.getTotal());
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

    public void diminuir(Item item) {
        if (item.getQuantity().doubleValue() > 1) {
            item.setQuantity(item.getQuantity().subtract(BigDecimal.ONE));
            item.setTotal(item.getTotal().subtract(item.getPrice().add(item.getTotalAds())));
        }
        calcularTotal();
    }

    public void adicionar(Item item) {
        item.setQuantity(item.getQuantity().add(BigDecimal.ONE));
        item.setTotal(item.getTotal().add(item.getPrice().add(item.getTotalAds())));
        calcularTotal();
    }

    public void removerItem(Item item) {
        order.getProducts().remove(item);
        calcularTotal();
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
        if (item.getTotalAds() != null) {
            item.setTotal(item.getPrice().multiply(item.getQuantity()).add(item.getTotalAds()));
        } else {
            item.setTotal(item.getPrice().multiply(item.getQuantity()));
        }

    }

    public void change() {
        processarTotaisAdcs();
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
                            value.setQuantity(value.getQuantity().multiply(item.getQuantity()));
                            value.setTotal(value.getPrice().multiply(value.getQuantity()));
                            totalAdicionais = totalAdicionais.add(value.getTotal());
                        }
                    }
                    atr.setValues(novos);
                    if (atr.getValues().size() > 0) {

                        atrs.add(atr);
                    }
                }
            }
            item.setAttributes(atrs);
            System.out.println(totalAdicionais);
            item.setTotalAds(totalAdicionais);
            System.out.println(item.getTotalAds());
            order.getProducts().add(item);
            order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
            calcularTotal();
            totalAdicionais = BigDecimal.ZERO;
            PrimeFaces.current().executeScript("closeModal('mdlPedir')");
            item = new Item();
        } else {
            PrimeFaces.current().executeScript("alerta('Selecione as opções!')");
        }

    }

    private String msg = "";

    public void montarMensagemFinalizar() {
        StringBuilder sb = new StringBuilder();
        sb.append(imprimirOrderControle(order));
        msg = sb.toString();
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
            if (order.getTotal().doubleValue() < company.getMinimalValue().doubleValue()) {
                PrimeFaces.current().executeScript("alerta('O valor do pedido não atingiu o mínimo de " + OUtils.formatarMoeda(company.getMinimalValue().doubleValue()) + "')");
                return;
            }

            if (order.getClientInfo().getPhone().length() < 12) {
                PrimeFaces.current().executeScript("alerta('O telefone informado está inválido!')");
                return;
            }

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
            } else if (order.getDelivery() && order.getAddress().getStreet() == null) {
                order.setDelivery(Boolean.FALSE);
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
                        compService.saveCompany(company);
                    }
                }
            }
            if (!company.getFreeVersion()) {
                orderService.sendOrder(order, company.getId());
            }
            
            montarMensagemFinalizar();
            PrimefacesUtil.Update("grpScrips");
            if (company.getFreeVersion()) {
                PrimeFaces.current().executeScript("finalizarPedidoFree();");
            } else {
                PrimeFaces.current().executeScript("finalizarPedido();");
            }
            PrimeFaces.current().executeScript("PF('ldg').hide()");
            PrimeFaces.current().executeScript("PF('wizardWidget').loadStep('personal', false)");
            PrimeFaces.current().executeScript("endCom()");

            order = new Order();
            couponValid = false;
            couponCode = "";
            if (company.getAddress() != null && company.getAddress().getCity() != null) {
                order.getAddress().setCity(company.getAddress().getCity());
            }
            if (company.getFreeVersion()) {
                PrimeFaces.current().executeScript("finalizarPedidoFree();");
            } else {
                PrimeFaces.current().executeScript("finalizarPedido();");
            }
            PrimeFaces.current().executeScript("PF('ldg').hide()");
            PrimeFaces.current().executeScript("PF('wizardWidget').loadStep('personal', false)");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void calcularTotal() {
        order.setSubtotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
        if (order.getDiscountValue() != null) {
            order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()).subtract(order.getDiscountValue()));
        } else {
            order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()));
        }
        validarTaxaServico();
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getNewStep().equals("entrega") || event.getOldStep().equals("entrega")) {
            adicionarRemoverTaxa();
        }
        return event.getNewStep();
    }

    public void adicionarRemoverTaxa() {
        if (order.getDelivery()) {
            if (order.getAddress().getAuto() != null) {
                if (order != null && order.getDelivery()) {
                    validarTaxaServico();
                } else {
                    order.setDeliveryCost(BigDecimal.ZERO);
                }
            }
        } else {
            order.setDeliveryCost(BigDecimal.ZERO);
        }

        calcularTotal();
    }

    public void validarTaxaServico() {
        try {
            System.out.println("TOTAL: " + order.getTotal());
            if (company.getUniqueDeliveryCost()) {
                order.setDeliveryCost(company.getDeliveryCost());
            } else {
                List<Bairro> b = company.getBairros().stream().filter(bb -> bb.getBairro().equalsIgnoreCase(order.getAddress().getAuto())).collect(Collectors.toList());
                order.setDeliveryCost(b.get(0).getTaxa());
            }
            if (company.getValueMaxPromoDelivery() != null && company.getValueMaxPromoDelivery().doubleValue() > 0
                    && company.getValuePromoDelivery() != null) {
                if (order.getTotal().doubleValue() >= company.getValueMaxPromoDelivery().doubleValue()) {
                    order.setDeliveryCost(company.getValuePromoDelivery());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            order.setDeliveryCost(BigDecimal.ZERO);
        }

    }

    public List<EnderecoDTO> pesquisarEndereco(String end) {
        PrimeFaces.current().executeScript("PF('ldg').show()");
        try {
            enderecos = compService.buscarEndereco(end, company.getAddress().getState(), company.getAddress().getCity());
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
//        PrimeFaces.current().ajax().update("frmFechar:bair");
        PrimeFaces.current().executeScript("$('.numberHome').focus();");

        adicionarRemoverTaxa();
        validarTaxaServico();
        PrimeFaces.current().executeScript("$('.numberHome').focus()");
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Item> getProducts() {
        return products;
    }

    public void setProducts(List<Item> products) {
        this.products = products;
    }

    public Product getProduct() {
        return product;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Company getCompany() {
        return company;
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

    public Boolean getCouponValid() {
        return couponValid;
    }

    public void setCouponValid(Boolean couponValid) {
        this.couponValid = couponValid;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void checkDeliveryCost(String phone) {
        finalizado = false;
        if (order.getDelivery()) {
            if (company.getUniqueDeliveryCost()) {
                order.setDeliveryCost(company.getDeliveryCost());
                calcularTotal();
            } else {
                if (order.getAddress().getAuto() != null) {
                    Optional<Bairro> find = company.getBairros().stream().filter(c -> c.getBairro().equalsIgnoreCase(order.getAddress().getAuto())).findAny();
                    if (find.isPresent()) {
                        Bairro found = find.get();
                        System.out.println(found.getTaxa());
                        order.setDeliveryCost(found.getTaxa());
                        calcularTotal();
                    }
                }
            }
        } else {
            order.setDeliveryCost(BigDecimal.ZERO);
            calcularTotal();
        }
        String newPhone = OUtils.formataNinePhone(phone);
        System.out.println(newPhone);
        order.getClientInfo().setPhone(newPhone);
        PrimefacesUtil.Update("grpPrincipal");
    }

    public void applyCoupon() throws Exception {
        List<CouponCode> couponsCompany = new ArrayList();
        couponsCompany = couponService.couponsByCompany(company.getId());
        System.out.println("LISTA DE CUPONS:" + couponsCompany.size());
        BigDecimal totalSemTaxa = order.getTotal().subtract(order.getDeliveryCost());
        System.out.println(totalSemTaxa);
        if (lastOrder != null) {
            String checkCoupon = couponMB.couponValid(couponsCompany, couponCode, lastOrder, order.getTotal(), totalSemTaxa);
            System.out.println("VALIDADE: " + checkCoupon);
            switch (checkCoupon) {
                case "true":
                    for (CouponCode c : company.getCoupons()) {
                        if (c.getSlug().equalsIgnoreCase(couponCode)) {
                            order.setCoupon(couponCode);
                            calcularDescontoCupom(c.getDiscount(), c.getDeliveryCost(), c.getPercentual());
                            couponValid = true;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cupom aplicado!", "Cupom aplicado!"));
                            PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                        }
                    }
                    break;
                case "onetime":
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom já foi utilizado hoje!", "Cupom já foi utilizado hoje!"));
                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    couponCode = "";
                    break;
                case "expired":
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom atingiu o limite máximo de utilizações!", "Cupom atingiu o limite máximo de utilizações!"));
                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    couponCode = "";
                    break;
                case "minimal":
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O valor minimo para utilizar esse cupom não foi atingido!", "O valor minimo para utilizar esse cupom não foi atingido!"));
                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    couponCode = "";
                    break;
                default:
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom inválido!", "Cupom inválido!"));
                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    couponCode = "";
                    break;
            }
        } else {
            String checkCoupon = couponMB.checkCouponNoOrder(couponsCompany, couponCode, order.getTotal(), totalSemTaxa);
            System.out.println("VALIDADE ELSE: " + checkCoupon);
            switch (checkCoupon) {
                case "true":
                    for (CouponCode c : company.getCoupons()) {
                        if (c.getSlug() != null && c.getSlug().equalsIgnoreCase(couponCode)) {
                            order.setCoupon(couponCode);
                            calcularDescontoCupom(c.getDiscount(), c.getDeliveryCost(), c.getPercentual());
                            couponValid = true;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cupom aplicado!", "Cupom aplicado!"));
//                            PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                            addDetailMessage("Cupom aplicado!", FacesMessage.SEVERITY_INFO);
                        }
                    }
                    break;
                case "minimal":
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O valor minimo para utilizar esse cupom não foi atingido!", "O valor minimo para utilizar esse cupom não foi atingido!"));
//                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    addDetailMessage("O valor minimo para utilizar esse cupom não foi atingido!", FacesMessage.SEVERITY_ERROR);
                    couponCode = "";
                    break;
                default:
                    couponValid = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cupom inválido!", "Cupom inválido!"));
//                    PrimefacesUtil.Update(":frmFechar:msgsCoupon");
                    addDetailMessage("Cupom inválido!", FacesMessage.SEVERITY_ERROR);
                    couponCode = "";
                    break;
            }
        }
    }

    private void calcularDescontoCupom(BigDecimal desconto, Boolean deliveryCost, Boolean percentual) {
        if (percentual) {
            BigDecimal discount = order.getTotal().multiply(desconto).divide(BigDecimal.valueOf(100));
            if (deliveryCost) {
                order.setDiscountValue(discount);
                order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()).subtract(discount));
            } else {
                order.setDiscountValue(discount);
                order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).subtract(discount).add(order.getDeliveryCost()));
            }
        } else {
            if (deliveryCost) {
                order.setDiscountValue(desconto);
                order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).add(order.getDeliveryCost()).subtract(desconto));
            } else {
                order.setDiscountValue(desconto);
                order.setTotal(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add).subtract(desconto).add(order.getDeliveryCost()));
            }
        }
    }

    public void setCompany(Company company) {
        if ((this.company == null) || (this.company != null && !this.company.getId().equals(company.getId()))) {
            this.company = company;
            order = new Order(this.company);
            order.setProducts(new ArrayList());
        }

    }

    public Company setar(Company company, String telefone) {
        if ((this.company.getId() == null) || (this.company.getId() != null && !this.company.getId().equals(company.getId()))) {
            finalizado = false;
            this.company = company;
            order = new Order(this.company);
            products = new ArrayList();
            this.telefone = telefone;
            carregarUltimoPedidoNumero();
        }
        return this.company;

    }

    public String formatarMoedaTaxa(Bairro bairro) {
        StringBuilder content = new StringBuilder();
        content.append("<div class='row'>");
        content.append("<div class='col-xs-12' style='    line-height: 1.1;'><strong>" + bairro.getBairro() + "</strong> </div>");
//        if (company.getUniqueDeliveryCost()) {
//            content.append("<div class='col-xs-12'><small style='    font-weight: bolder;     line-height: 1.1;'>Taxa: " + OUtils.formatarMoeda(company.getDeliveryCost().doubleValue()) + "</small></div>");
//        } else {
//            if (bairro.getTaxa().doubleValue() == 0.00 || bairro.getTaxa() == null) {
//                content.append("<div class='col-xs-12'><small style='    font-weight: bolder;color: #95c70d;     line-height: 1.1;'>Entrega Grátis</small> </div>");
//            } else {
//                content.append("<div class='col-xs-12'><small style='    font-weight: bolder;     line-height: 1.1;'>Taxa: " + OUtils.formatarMoeda(bairro.getTaxa().doubleValue()) + "</small></div>");
//            }
//        }
//        content.append("<div class='col-xs-12'><hr style='margin-top: 0;margin-bottom: 0;border-color: #95c70d;' /></div>");
        content.append("</div>");
        return content.toString();
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public static void addDetailMessage(String message, FacesMessage.Severity severity) {

        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
        Messages.add(null, facesMessage);
    }

}
