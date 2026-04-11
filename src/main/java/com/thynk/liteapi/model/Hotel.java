package com.thynk.liteapi.model;

public class Hotel {
    private String id;
    private String name;
    private String address;
    private Double stars;

    // Getters and Setters
    public String getId() { return id; }
    public void setHotelId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getStars() { return stars; }
    public void setStarRating(Double stars) { this.stars = stars; }

    @Override
    public String toString() {
        return String.format("Hotel: %s | ID: %s | Address: %s | Stars: %.1f",
                name, id, address, stars);
    }
}