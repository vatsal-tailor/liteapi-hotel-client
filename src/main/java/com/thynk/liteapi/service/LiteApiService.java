package com.thynk.liteapi.service;

import com.thynk.liteapi.client.LiteApiClient;
import com.thynk.liteapi.model.Hotel;
import com.thynk.liteapi.model.HotelRate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class LiteApiService {

    private final LiteApiClient client;

    public LiteApiService(LiteApiClient client) {
        this.client = client;
    }

    public void searchHotelsByCity(String cityName, String countryCode) {
        System.out.println("🔍 Searching hotels in " + cityName + ", " + countryCode + "...\n");

        List<Hotel> hotels = client.searchHotels(cityName, countryCode);

        if (hotels == null || hotels.isEmpty()) {
            System.out.println("No hotels found.");
            return;
        }

        System.out.println("✅ Found " + hotels.size() + " hotels:\n");
        for (int i = 0; i < Math.min(10, hotels.size()); i++) {  // show first 10
            System.out.println((i+1) + ". " + hotels.get(i));
        }

        // Ask user to pick a hotel for Flow 2
        System.out.print("\nEnter the number of the hotel to see rates (1-" + Math.min(10, hotels.size()) + "): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;

        if (choice >= 0 && choice < hotels.size()) {
            String selectedHotelId = hotels.get(choice).getId();
            System.out.println("\n📌 Getting rates for Hotel ID: " + selectedHotelId);
            getHotelRates(selectedHotelId);
        }
    }

    private void getHotelRates(String hotelId) {
        List<HotelRate> rates = client.getHotelRates(hotelId);

        if (rates == null || rates.isEmpty()) {
            System.out.println("No rates available for this hotel (try different dates).");
            return;
        }

        System.out.println("\n✅ Available Room Rates:\n");
        for (HotelRate rate : rates) {
            System.out.println(rate);
        }
    }
}