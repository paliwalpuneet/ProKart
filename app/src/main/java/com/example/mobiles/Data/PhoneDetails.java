package com.example.mobiles.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class PhoneDetails implements Serializable{
    String id;
    String brand;
    String model;
    String price;
    String details;
    List<String> images;

    public PhoneDetails() {
    }

    public PhoneDetails(String id, String brand, String model, String details, List<String> images, String price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.details = details;
        this.images = images;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
