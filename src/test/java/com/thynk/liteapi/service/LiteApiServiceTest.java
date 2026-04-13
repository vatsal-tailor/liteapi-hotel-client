package com.thynk.liteapi.service;

import com.thynk.liteapi.client.LiteApiClient;
import com.thynk.liteapi.model.Hotel;
import com.thynk.liteapi.model.HotelRate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiteApiServiceTest {

    @Mock
    private LiteApiClient client;

    @InjectMocks
    private LiteApiService service;

    private final InputStream originalSystemIn = System.in;

    private Hotel testHotel;
    private HotelRate testRate;

    @BeforeEach
    void setUp() {
        testHotel = new Hotel();
        testHotel.setId("lp65571c01");
        testHotel.setName("Motto by Hilton New York City Chelsea");
        testHotel.setAddress("113 West 24th Street");
        testHotel.setStars(4.0);

        testRate = new HotelRate();
        testRate.setRoomName("Deluxe King Room");
        testRate.setRate(245.75);
        testRate.setCurrency("USD");
        testRate.setBoardType("RO");
        testRate.setCancellationPolicy("Free cancellation");
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);   // Restore original input
    }

    // ==================== SUCCESS TEST ====================
    @Test
    @DisplayName("Success: Full Flow 1 + Flow 2")
    void testFullFlowSuccess() {
        when(client.searchHotels("New York", "US")).thenReturn(List.of(testHotel));
        when(client.getHotelRates("lp65571c01")).thenReturn(List.of(testRate));

        // Simulate user input: City and Country are passed to method,
        // then user types "1" to select the first hotel
        String simulatedInput = "1\n";                    // choose hotel number 1
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        service.searchHotelsByCity("New York", "US");

        verify(client, times(1)).searchHotels("New York", "US");
        verify(client, times(1)).getHotelRates("lp65571c01");
    }

    // ==================== FAILURE TESTS ====================
    @Test
    @DisplayName("Failure Flow 1: No hotels found")
    void testNoHotelsFound() {
        when(client.searchHotels("InvalidCity", "XX")).thenReturn(List.of());

        String simulatedInput = "1\n";   // doesn't matter since no hotels
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        service.searchHotelsByCity("InvalidCity", "XX");

        verify(client, times(1)).searchHotels("InvalidCity", "XX");
        verify(client, never()).getHotelRates(anyString());
    }

    @Test
    @DisplayName("Failure Flow 2: No rates available")
    void testNoRatesAvailable() {
        when(client.searchHotels("New York", "US")).thenReturn(List.of(testHotel));
        when(client.getHotelRates("lp65571c01")).thenReturn(List.of());  // empty

        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        service.searchHotelsByCity("New York", "US");

        verify(client, times(1)).searchHotels("New York", "US");
        verify(client, times(1)).getHotelRates("lp65571c01");
    }
}