package com.projects.orderon.models;

import java.util.HashMap;
import java.util.Map;

public class MenuItem {
    String itemName;
    String description, storeId;
    String itemId, imageURL;
    int price, qty;

    public MenuItem(String id, String name, String desc, int p, int q, String url, String stId) {
        itemId = id;
        itemName = name;
        description = desc;
        price = p;
        imageURL = url;
        qty = q;
        storeId = stId;
    }

    public Map<String, Object> getMenuItem() {
        Map<String, Object> item = new HashMap<>();

        item.put("id", itemId);
        item.put("item", itemName);
        item.put("description", description);
        item.put("netPrice", price);
        item.put("quantity", qty);
        item.put("storeId", storeId);
        item.put("imgURL", imageURL);
        item.put("rating", 4.5);

        return item;
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
