package com.hanu.a2_1801040015.models;

import java.io.Serializable;

public class Product implements Serializable {
    private long id;
    private String thumbnail;
    private String name;
    private String category;
    private double unitPrice, total;
    private int quantity;

    public Product() {

    }

    public Product(int quantity) {
        this.quantity = quantity;
    }

    public Product(String thumbnail, String name, double unitPrice, double total) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public Product(long id, String thumbnail, String name, double unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public Product(long id, String thumbnail, String name, double unitPrice, int quantity) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Product(String thumbnail, String name, double unitPrice, int quantity) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Product(long id, String thumbnail, String name, String category, double unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public Product(String thumbnail, String name, String category, double unitPrice) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }


    public Product(String thumbnail, String name, double unitPrice) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
    }


    public Product(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product(String name, double unitPrice) {
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product(String thumbnail, String name) {
        this.thumbnail = thumbnail;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
