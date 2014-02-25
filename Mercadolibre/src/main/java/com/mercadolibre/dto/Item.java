package com.mercadolibre.dto;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {

    private String id;
    private String title;
    private float price;
    private String thumbnail;
    private int available_quantity;
    private String stop_time;
    private List<Picture> pictures;


    public Item(String id, String title, float price, String thumbnail, int available_quantity, String stop_time, List<Picture> pictures) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.available_quantity = available_quantity;
        this.stop_time = stop_time;
        this.pictures = pictures;
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
        return "$ " + price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public float getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(int available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }


    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
