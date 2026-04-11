package com.thynk.liteapi.model;

import java.util.List;

public class RoomType {
     //private String name;           // ← must have this
     private List<HotelRate> rates;

//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }

     public List<HotelRate> getRates() { return rates; }
     public void setRates(List<HotelRate> rates) { this.rates = rates; }
}