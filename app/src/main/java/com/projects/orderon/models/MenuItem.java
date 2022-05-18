package com.projects.orderon.models;

import java.util.HashMap;
import java.util.Map;

public class MenuItem {
    String item, storeType, storeName;
    String description, storeId;
    String itemId, imgURL;
    int price, quantity, netPrice;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public MenuItem(String id, String name, String desc, int p, int netP, int q, String url, String stId, String stType, String stName) {
        itemId = id;
        item = name;
        description = desc;
        price = p;
        netPrice = netP;
        imgURL = url;
        quantity = q;
        storeId = stId;
        storeType = stType;
        storeName = stName;
    }

    public Map<String, Object> getMenuItem() {
        Map<String, Object> item = new HashMap<>();

        item.put("id", itemId);
        item.put("item", this.item);
        item.put("description", description);
        item.put("price", price);
        item.put("netPrice", netPrice);
        item.put("storeType", storeType);
        item.put("storeName", storeName);
        item.put("quantity", quantity);
        item.put("storeId", storeId);
        item.put("imgURL", imgURL);
        item.put("rating", 4.5);

        return item;
    }

    public String getItemId() {
        return itemId;
    }

    public String getStoreId() { return storeId; }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setNetPrice(int np) { this.netPrice = np; }

    public int getNetPrice() { return this.netPrice; }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
