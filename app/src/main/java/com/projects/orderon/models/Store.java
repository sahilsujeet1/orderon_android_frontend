package com.projects.orderon.models;

public class Store {
    String imageURL;
    String storeName;
    String storeAddress;
    String storeType;
    String storeId;

    public Store() {
        storeAddress = "XYZ";
        storeName = "ABC";
        storeType = "restaurant";
        imageURL = "";
    }

    public Store(String id, String name, String address, String type, String url) {
        storeId = id;
        storeAddress = address;
        storeName = name;
        storeType = type;
        imageURL = url;
    }

    public String getStoreId() { return storeId; }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
