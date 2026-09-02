package com.sithu.courtease.models;

import java.io.Serializable;

public class Court implements Serializable {

    private String id;
    private String name;
    private String sport;
    private String location;
    private String address;
    private String description;
    private double pricePerHour;
    private double rating;
    private String imageName;
    private boolean available;

    public Court() {
        // Required by Firestore
    }

    public Court(
            String id,
            String name,
            String sport,
            String location,
            String address,
            String description,
            double pricePerHour,
            double rating,
            String imageName) {

        this(id, name, sport, location, address, description, pricePerHour, rating, imageName, true);
    }

    public Court(
            String id,
            String name,
            String sport,
            String location,
            String address,
            String description,
            double pricePerHour,
            double rating,
            String imageName,
            boolean available) {

        this.id = id;
        this.name = name;
        this.sport = sport;
        this.location = location;
        this.address = address;
        this.description = description;
        this.pricePerHour = pricePerHour;
        this.rating = rating;
        this.imageName = imageName;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}