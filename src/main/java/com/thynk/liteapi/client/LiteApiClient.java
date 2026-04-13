package com.thynk.liteapi.client;

import com.thynk.liteapi.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

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

        System.out.println("LiteApiClient initialized");
        System.out.println("   Base URL: " + baseUrl);
   }

    // Flow 1: Search Hotels by City
    public List<Hotel> searchHotels(String cityName, String countryCode) {
        String url = baseUrl + "/data/hotels?cityName=" + cityName + "&countryCode=" + countryCode;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("Calling: " + url);

        ResponseEntity<HotelSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                HotelSearchResponse.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            HotelSearchResponse body = response.getBody();
            return (body != null && body.getData() != null) ? body.getData() : List.of();
        } else {
            System.err.println("Search failed with status: " + response.getStatusCode() + " - " + response.getBody());
            return List.of();
        }
    }

    // Flow 2: Get Hotel Rates
    public List<HotelRate> getHotelRates(String hotelId) {
        String url = baseUrl + "/hotels/rates";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        String requestBody = """
        {
          "checkin": "2026-04-14",
          "checkout": "2026-04-20",
          "currency": "USD",
          "guestNationality": "US",
          "occupancies": [{"adults": 2, "children": []}],
          "hotelIds": ["%s"],
          "timeout": 10
        }
        """.formatted(hotelId);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        System.out.println("Requesting rates for Hotel ID: " + hotelId);

        ResponseEntity<HotelRateResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                HotelRateResponse.class
        );

        if(response.getStatusCode().is2xxSuccessful()) {
            HotelRateResponse body = response.getBody();

            if (body == null || body.getData() == null || body.getData().isEmpty()) {
                return List.of();
            }

            return body.getData().stream()
                    .filter(item -> item.getRoomTypes() != null)
                    .flatMap(item -> item.getRoomTypes().stream())
                    .filter(roomType -> roomType.getRates() != null)
                    .flatMap(roomType -> roomType.getRates().stream())
                    .collect(Collectors.toList());
        } else {
            System.err.println("xRates request failed with status: " + response.getStatusCode());
            if (response.getStatusCode().value() == 429) {
                System.err.println("Rate limit exceeded. Try again later.");
            }
            return List.of();
        }
    }
}