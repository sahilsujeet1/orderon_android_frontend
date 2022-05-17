package com.projects.orderon.models;

import java.util.ArrayList;

public class Order {
    ArrayList<OrderItem> items;
    String date;
    String orderId;
    String paymentMode;
    Address address;

    public Order(String oId, String dt, String mode, Address add, ArrayList<OrderItem> i) {
        orderId = oId;
        date = dt;
        paymentMode = mode;
        address = add;
        items = i;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Address getAddress() {
        return address;
    }

    public String getAddressString() {
        String add = address.getFullName() + ", " + address.getStreet() + ", "
                + address.getCity() + ", " + address.getState() + " - " + address.getPincode();
        return add;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
