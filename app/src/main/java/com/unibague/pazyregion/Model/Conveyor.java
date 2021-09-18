package com.unibague.pazyregion.Model;

import com.google.android.material.textfield.TextInputLayout;

public class Conveyor {

    private String email;
    private String password;
    private String name;
    private String surName;
    private String direction;
    private String phone;
    private String vehicle;
    private String description;
    private String userType;

    public Conveyor(){}

    public Conveyor(String email, String password, String name, String surName, String direction, String phone, String vehicle, String description, String userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surName = surName;
        this.direction = direction;
        this.phone = phone;
        this.vehicle = vehicle;
        this.description = description;
        this.userType = userType;
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

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
