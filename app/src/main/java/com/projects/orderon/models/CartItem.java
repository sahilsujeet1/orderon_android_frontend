package com.projects.orderon.models;

import java.util.HashMap;
import java.util.Map;

public class CartItem {

    String cartItemName, cartItemDesc, cartItemId, cartItemImgUrl, storeId;
    int cartItemPrice, cartItemQty;

    Map<String, Object> item;

    public CartItem(String id, String name, String desc, int price, int qty, String img, String stId) {
        cartItemId = id;
        cartItemName = name;
        cartItemDesc = desc;
        cartItemPrice = price;
        cartItemQty = qty;
        cartItemImgUrl = img;
        storeId = stId;
    }

    public Map<String, Object> getCartItem() {
        item = new HashMap<>();
        item.put("item", cartItemName);
        item.put("netPrice", cartItemPrice * cartItemQty);
        item.put("quantity", cartItemQty);
        item.put("description", cartItemDesc);
        item.put("imgURL", cartItemImgUrl);
        item.put("id", cartItemId);
        item.put("storeId", storeId);

        return item;
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

    public String getCartItemImgUrl() {
        return cartItemImgUrl;
    }

    public void setCartItemImgUrl(String cartItemImgUrl) {
        this.cartItemImgUrl = cartItemImgUrl;
    }
}
