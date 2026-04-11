package com.thynk.liteapi.model;

import java.util.List;

public class HotelRateResponse {
    private List<HotelRateItem> data;

    public List<HotelRateItem> getData() {
        return data;
    }

    public void setData(List<HotelRateItem> data) {
        this.data = data;
    }
}
