package com.thynk.liteapi.model;

import java.util.List;

public class HotelSearchResponse {
    private List<Hotel> data;

    public List<Hotel> getData() {
        return data;
    }

    public void setData(List<Hotel> data) {
        this.data = data;
    }
}
