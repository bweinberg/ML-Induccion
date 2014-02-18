package com.mercadolibre.dto;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Item implements Serializable {

    private String id;
    private String title;
    private String price;
    private String picture;
    private String available;
    private String endDate;


    public Item(String id, String title, String price, String picture, String available, String endDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.picture = picture;
        this.available = available;
        this.endDate = endDate;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
