package com.thynk.liteapi.client;

import com.thynk.liteapi.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class LiteApiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public LiteApiClient(RestTemplate restTemplate,
                         @Value("${liteapi.base-url}") String baseUrl,
                         @Value("${liteapi.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        System.out.println("✅ LiteApiClient initialized");
        System.out.println("   Base URL: " + baseUrl);
        System.out.println("   API Key (first 8 chars): " + (apiKey != null && apiKey.length() > 8 ? apiKey.substring(0, 8) + "..." : "[MISSING]"));
    }

    // Flow 1: Search Hotels by City (CORRECTED endpoint + parameters)
    public List<Hotel> searchHotels(String cityName, String countryCode) {
        String url = baseUrl + "/data/hotels?cityName=" + cityName + "&countryCode=" + countryCode;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);     // ← Official header name
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("🔍 Calling: " + url);   // debug
        System.out.println("🔑 DEBUG - Using header X-Api-Key");

        ResponseEntity<HotelSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                HotelSearchResponse.class
        );

        HotelSearchResponse body = response.getBody();
        return (body != null && body.getData() != null) ? body.getData() : List.of();
    }

    // Flow 2: Get Hotel Rates (already correct)
    public List<HotelRate> getHotelRates(String hotelId) {
        String url = baseUrl + "/hotels/rates";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        String requestBody = """
        {
          "checkin": "2026-04-10",
          "checkout": "2026-04-12",
          "currency": "USD",
          "guestNationality": "US",
          "occupancies": [{"adults": 2, "children": []}],
          "hotelIds": ["%s"],
          "timeout": 10
        }
        """.formatted(hotelId);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        System.out.println("📤 Requesting rates for Hotel ID: " + hotelId);

        ResponseEntity<HotelRateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                HotelRateResponse.class
        );

        HotelRateResponse body = response.getBody();
//        System.out.println("=== RAW JSON RESPONSE ===");
//        ResponseEntity<String> raw = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        System.out.println(raw.getBody());

        if (body == null || body.getData() == null || body.getData().isEmpty()) {
            System.out.println("No rates data in response.");
            return List.of();
        }

        // Clean & safe way to flatten + set roomName
        List<HotelRate> allRates = new ArrayList<>();

        for (HotelRateItem item : body.getData()) {
            if (item.getRoomTypes() == null) continue;

            for (RoomType roomType : item.getRoomTypes()) {
                //String roomName = roomType.getName();

                if (roomType.getRates() != null) {
//                    for (HotelRate rate : roomType.getRates()) {
//                        if (roomName != null) {
//                            rate.setRoomName(roomName);   // Safe setter call
//                        }
//                        allRates.add(rate);
//                    }
                    allRates.addAll(roomType.getRates());
                }
            }
        }

        return allRates;

//        if (body != null && body.getData() != null && !body.getData().isEmpty()) {
//            return body.getData().stream()
//                    .flatMap(item -> item.getRoomTypes() != null
//                            ? item.getRoomTypes().stream()
//                            : java.util.stream.Stream.empty())
//                    .flatMap(roomType -> {
//                        String roomName = roomType.getName();
//                        return (roomType.getRates() != null
//                                ? roomType.getRates().stream()
//                                : java.util.stream.Stream.empty());
////                                .peek(rate -> {
////                                    if (roomName != null) {
////                                        rate.setRoomName(roomName);   // This line was causing the compile error
////                                    }
////                                });
//                    })
//                    .toList();
//        }
//
//        return List.of();
    }
}