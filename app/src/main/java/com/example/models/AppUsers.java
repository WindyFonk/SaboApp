package com.example.models;

public class AppUsers {
    private String email, password, name, address, phonenumb;
    private Long role;
    private String id, image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public AppUsers(String id,String email, String password, String name, String address, String phonenumb, Long role, String image) {
        this.id=id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phonenumb = phonenumb;
        this.role = role;
        this.image=image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumb() {
        return phonenumb;
    }

    public void setPhonenumb(String phonenumb) {
        this.phonenumb = phonenumb;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }
}
