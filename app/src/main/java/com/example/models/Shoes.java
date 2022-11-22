package com.example.models;

public class Shoes {
    private String name;
    private String brand;
    private String image;
    private String details;
    private String size;
    private String color;
    private Integer quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private Long price;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Shoes(String id, String name, String brand, Long price, String image, String details, String size, String color) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.image=image;
        this.details=details;
        this.id=id;
        this.size=size;
        this.color=color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Shoes(String id, String name, Long price, String image, String size, String color, Integer quantity) {
        this.name = name;
        this.price = price;
        this.image=image;
        this.id=id;
        this.size=size;
        this.color=color;
        this.quantity=quantity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
