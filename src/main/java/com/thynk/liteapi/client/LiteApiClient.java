package com.thynk.liteapi.client;

import com.thynk.liteapi.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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
        try {
            System.out.println("Calling: " + url);

            ResponseEntity<HotelSearchResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, HotelSearchResponse.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                System.err.println("Unexpected status code: " + response.getStatusCode());
            }

            HotelSearchResponse body = response.getBody();
            return (body != null && body.getData() != null) ? body.getData() : List.of();

        } catch (HttpClientErrorException e) {   // 4xx errors
            System.err.println("Client Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                System.err.println("Check if your API key is correct and has proper permissions.");
            }
            return List.of();
        } catch (HttpServerErrorException e) {   // 5xx errors
            System.err.println("Server Error (" + e.getStatusCode() + "): LiteAPI service is temporarily unavailable.");
            return List.of();
        } catch (RestClientException e) {
            System.err.println("Failed to connect to LiteAPI: " + e.getMessage());
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
        try {
            System.out.println("Requesting rates for Hotel ID: " + hotelId);

            ResponseEntity<HotelRateResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, HotelRateResponse.class);

            HotelRateResponse body = response.getBody();

            if (body != null && body.getData() != null && !body.getData().isEmpty()) {
                return body.getData().stream()
                        .flatMap(item -> item.getRoomTypes() != null ? item.getRoomTypes().stream() : java.util.stream.Stream.empty())
                        .flatMap(roomType -> roomType.getRates() != null ? roomType.getRates().stream() : java.util.stream.Stream.empty())
                        .toList();
            }
            return List.of();

        } catch (HttpClientErrorException e) {   // 4xx
            System.err.println("Client Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 400) {
                System.err.println("   → Invalid request. Check hotel ID or dates.");
            }
            return List.of();
        } catch (HttpServerErrorException e) {   // 5xx
            System.err.println("Server Error (" + e.getStatusCode() + "): LiteAPI is experiencing issues.");
            return List.of();
        } catch (RestClientException e) {
            System.err.println("Failed to fetch rates: " + e.getMessage());
            return List.of();
        }
    }
}