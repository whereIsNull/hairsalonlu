package com.skynet.javafx.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Product extends SimpleEntity {

    private String category;

    private String productName;

    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return productName;
    }
}
