package com.example.basiclogintoapp.Model;

// OrderItem.java
public class OrderItem {

    private String title;
    private String description;
    private String price;
    private String imageUrl;

    public OrderItem(String title, String description, String price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
