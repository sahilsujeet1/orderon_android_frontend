package com.projects.orderon.models;

public class MenuItem {
    String itemName;
    String description, storeId;
    String itemId, imageURL;
    int price, qty;

    public MenuItem(String id, String name, String desc, int p, String url) {
        itemId = id;
        itemName = name;
        description = desc;
        price = p;
        imageURL = url;
        qty = 0;
    }

    public String getItemId() {
        return itemId;
    }

    public String getStoreId() { return storeId; }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
