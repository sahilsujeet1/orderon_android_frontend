package com.projects.orderon.models;

import com.google.gson.annotations.SerializedName;

public class Address {
    String street;
    String city;
    String state;
    String pincode;
    String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile;


    public Address(String fullName, String street, String city, String state, String pincode, String mobile) {
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.mobile = mobile;
        this.pincode = pincode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
