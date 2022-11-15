package com.example.models;

public class Shoes {
    private String name;
    private String brand;
    private String image;
    private String details;
    private String size;
    private String color;

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

    public Shoes(String id,String name, String brand, Long price, String image, String details, String size, String color) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.image=image;
        this.details=details;
        this.id=id;
        this.size=size;
        this.color=color;
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
