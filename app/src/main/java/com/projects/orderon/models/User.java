package com.projects.orderon.models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class User {

    String name, email, accountType, photoUrl, uid;
    Timestamp createdOn;
    Map<String, Object> user;

    public User(String name, String email, String accountType, String photoUrl, String uid, Timestamp createdOn) {
        this.name = name;
        this.email = email;
        this.accountType = accountType;
        this.photoUrl = photoUrl;
        this.uid = uid;
        this.createdOn = createdOn;

        this.user = new HashMap<String, Object>();
        this.user.put("name", name);
        this.user.put("email", email);
        this.user.put("accountType", accountType);
        this.user.put("photoUrl", photoUrl);
        this.user.put("uid", uid);
        this.user.put("createdOn", createdOn);

    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getUser() {
        return this.user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }
}
