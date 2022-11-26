package com.skynet.javafx.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Entity
public class InvoiceLine extends SimpleEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Invoice invoice;
    private String category;
    private String productName;
    private BigDecimal productPrize;
    private Integer amount;
    private BigDecimal calculatedPrice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getProductPrize() {
        return productPrize;
    }

    public void setProductPrice(BigDecimal productPrize) {
        this.productPrize = productPrize;
    }

    public BigDecimal getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

}
