package com.unibague.pazyregion.Model;

public class Product {
    private String title;
    private String date;
    private String availability;
    private String description;
    private String price;
    private String email;
    private String phone;
    private String uri;
    private String nameUser;

    public Product(){

    }

    public Product(String title, String date, String availability, String description, String price, String email, String phone, String uri, String nameUser) {
        this.title = title;
        this.date = date;
        this.availability = availability;
        this.description = description;
        this.price = price;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
        this.nameUser = nameUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
