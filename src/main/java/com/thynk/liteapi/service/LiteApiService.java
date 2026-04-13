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
        System.out.println("Searching hotels in " + cityName + ", " + countryCode + "...\n");

        List<Hotel> hotels = client.searchHotels(cityName, countryCode);

        if (hotels == null || hotels.isEmpty()) {
            System.out.println("No hotels found.");
            return;
        }

        System.out.println("Found " + hotels.size() + " hotels:\n");
        for (int i = 0; i < Math.min(10, hotels.size()); i++) {
            System.out.println((i+1) + ". " + hotels.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        int choice = - 1;

        while (choice < 0 || choice >= Math.min(10, hotels.size())) {
            System.out.print("\nEnter the number of the hotel to see rates (1-"
                    + Math.min(10, hotels.size()) + "): ");        // Ask user to pick a hotel for Flow 2

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt() - 1;

                if (choice < 0 || choice >= hotels.size()) {
                    System.out.println("Invalid choice. Please enter a number between 1 and "
                            + Math.min(10, hotels.size()) + ".");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next(); // consume the invalid input
            }
        }

        String selectedHotelId = hotels.get(choice).getId();
        System.out.println("Getting rates for Hotel ID: " + selectedHotelId);
        getHotelRates(selectedHotelId);

    }

    private void getHotelRates(String hotelId) {
        List<HotelRate> rates = client.getHotelRates(hotelId);

        if (rates == null || rates.isEmpty()) {
            System.out.println("No rates available for this hotel (try different dates).");
            return;
        }
        System.out.println("Found " + rates.size() + " hotel rooms:\n");
        System.out.println("\nAvailable Room Rates:\n");
        for (int i = 0; i < Math.min(20, rates.size()); i++) {
            System.out.println((i+1) + ". " + rates.get(i));
        }
    }
}