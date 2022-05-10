package models;

import java.util.ArrayList;

public class Order {
    ArrayList<OrderItem> items;
    String date;
    String orderId;
    String paymentMode;
    String address;

    public Order(String oId, String dt, String mode, String add, ArrayList<OrderItem> i) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
