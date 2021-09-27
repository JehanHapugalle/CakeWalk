package com.example.thecakewalk.Models;

public class CartAddedItems {

    String id;
    String name;
    String price;
    String qty;
    String image;

    public CartAddedItems() {
    }

    public CartAddedItems(String id, String name, String price, String qty, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
