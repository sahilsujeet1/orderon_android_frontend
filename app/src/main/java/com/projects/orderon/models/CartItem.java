package com.projects.orderon.models;

public class CartItem {

    String cartItemName, cartItemDesc;
    int cartItemPrice, cartItemQty, cartItemImgUrl;

    public CartItem(String name, String desc, int price, int qty, int img) {
        cartItemName = name;
        cartItemDesc = desc;
        cartItemPrice = price;
        cartItemQty = qty;
        cartItemImgUrl = img;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public String getCartItemDesc() {
        return cartItemDesc;
    }

    public void setCartItemDesc(String cartItemDesc) {
        this.cartItemDesc = cartItemDesc;
    }

    public int getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(int cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public int getCartItemQty() {
        return cartItemQty;
    }

    public void setCartItemQty(int cartItemQty) {
        this.cartItemQty = cartItemQty;
    }

    public int getCartItemImgUrl() {
        return cartItemImgUrl;
    }

    public void setCartItemImgUrl(int cartItemImgUrl) {
        this.cartItemImgUrl = cartItemImgUrl;
    }
}
