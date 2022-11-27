package com.example.models;

public class OrdersDetails {
    String orderid, shoeid, image, name, price, color, size;
    Long quantity;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getShoeid() {
        return shoeid;
    }

    public void setShoeid(String shoeid) {
        this.shoeid = shoeid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrdersDetails(String orderid, String shoeid, String image, String name, String price, String color, String size, Long quantity) {
        this.orderid = orderid;
        this.shoeid = shoeid;
        this.image = image;
        this.name = name;
        this.price = price;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
    }
}
