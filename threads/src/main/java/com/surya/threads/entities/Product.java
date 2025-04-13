package com.surya.threads.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "product", schema = "store")
public class Product {
    @Id
    private Long id;

    private String name;

    private String category;

    private double price;

    @Column(name = "isofferapplied")
    private boolean isOfferApplied;

    @Column(name = "discountpercentage")
    private double discountPercentage;

    @Column(name = "priceafterdiscount")
    private double priceAfterDiscount;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOfferApplied() {
        return isOfferApplied;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOfferApplied(boolean offerApplied) {
        isOfferApplied = offerApplied;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }
}