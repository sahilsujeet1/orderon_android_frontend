package com.projects.orderon.models;

public class OrderItem {

    String itemName, seller, imgURL;
    int qty, amount;

    public OrderItem(String name, String slr, int q, int amt, String url) {
        itemName = name;
        seller = slr;
        qty = q;
        amount = amt;
        imgURL = url;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
