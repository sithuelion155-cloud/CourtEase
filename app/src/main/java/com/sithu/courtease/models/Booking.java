package com.sithu.courtease.models;

public class Booking {

    private String id;
    private String userId;
    private String courtId;
    private String courtName;
    private String sport;
    private String location;
    private String date;
    private String startTime;
    private String endTime;
    private double price;
    private String status;
    private Object createdAt;

    public Booking() {
        // Required by Firestore
    }

    public Booking(
            String id,
            String userId,
            String courtId,
            String courtName,
            String sport,
            String location,
            String date,
            String startTime,
            String endTime,
            double price,
            String status,
            Object createdAt) {

        this.id = id;
        this.userId = userId;
        this.courtId = courtId;
        this.courtName = courtName;
        this.sport = sport;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }
}