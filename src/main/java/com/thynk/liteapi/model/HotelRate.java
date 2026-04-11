package com.thynk.liteapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelRate {

    @JsonProperty("name")
    private String roomName;

    private Double rate;

    private String currency;

    @JsonProperty("boardType")
    private String boardType;

    private String cancellationPolicy;

//    @JsonProperty("offerRetailRate")
//    private void unpackRate(Map<String, Object> offerRetailRate) {
//        this.rate = (Double) offerRetailRate.get("amount");
//        this.currency = (String) offerRetailRate.get("currency");
//    }

    @JsonProperty("retailRate")
    private void unpackRetailRate(Map<String, Object> retailRate) {
        List<Map<String, Object>> totals = (List<Map<String, Object>>) retailRate.get("total");
        if (totals != null && !totals.isEmpty()) {
            Object amountObj = totals.getFirst().get("amount");
            if (amountObj instanceof Number) {
                this.rate = ((Number) amountObj).doubleValue();
            }
            this.currency = (String) totals.getFirst().get("currency");
        }
    }


    @JsonProperty("cancellationPolicies")
    private void unpackCancellation(Map<String, Object> cancellationPolicies) {
        this.cancellationPolicy = (String) cancellationPolicies.get("refundableTag");
    }

    // Getters and Setters
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }

    public String getCancellationPolicy() { return cancellationPolicy; }
    public void setCancellationPolicy(String cancellationPolicy) { this.cancellationPolicy = cancellationPolicy; }

    @Override
    public String toString() {
        return String.format("Room: %s | Rate: %.2f %s | Board: %s | Cancellation: %s",
                roomName != null ? roomName : "N/A",
                rate != null ? rate : 0.0,
                currency != null ? currency : "USD",
                boardType != null ? boardType : "N/A",
                cancellationPolicy != null ? cancellationPolicy : "N/A");
    }
}