package com.dmb.quicktrade.model;

import java.io.Serializable;

/**
 * Created by davidmari on 21/1/18.
 */

public class Product implements Serializable{

    String name;
    String description;
    String category;
    String price;
    String userUID;
    String key;
    Boolean favorite = false;

    public Product(String name, String description, String category, String price, String userUID, String key, Boolean favorite) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.userUID = userUID;
        this.key = key;
        this.favorite = favorite;
    }

    public Product(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}