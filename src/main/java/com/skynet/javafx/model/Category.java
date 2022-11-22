package com.skynet.javafx.model;

import javax.persistence.Entity;

@Entity
public class Category extends SimpleEntity {

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
