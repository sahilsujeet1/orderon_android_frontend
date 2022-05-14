package com.projects.orderon.models;

public class Store {
    int imageURL;
    String storeName;
    String storeAddress;
    String storeType;

    public Store() {
        storeAddress = "XYZ";
        storeName = "ABC";
        storeType = "restaurant";
        imageURL = 0;
    }

    public Store(String name, String address, String type, int url) {
        storeAddress = address;
        storeName = name;
        storeType = type;
        imageURL = url;
    }

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

    public int getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }

}
