/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.view;

import com.popsales.model.dto.EnderecoDTO;
import com.popsales.services.CategoryServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Renato
 */
@ManagedBean
@javax.faces.bean.ViewScoped
public class HtmlMB implements Serializable {

    private CategoryServices categoryService = new CategoryServices();

    public HtmlMB() {
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
        return "<div class=\"col-xs-4\" style='    width: 100%;padding: 0;margin: 0;'><div class='card2 " + (bol == true ? "box-selected" : "") + "'  style='" + (bol == false ? "" : "border: 1px solid #95c70d !important;\n"
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
        return " <div class=\"col-xs-4\" style='    width: 100%;padding: 0;margin: 0;'><div class='card2 " + (bol == true ? "box-selected" : "") + "'  style='" + (bol == false ? "" : "border: 1px solid #95c70d !important;\n"
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

   
}
