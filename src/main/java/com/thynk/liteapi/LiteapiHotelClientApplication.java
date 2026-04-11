package com.thynk.liteapi;

import com.thynk.liteapi.service.LiteApiService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@SpringBootApplication
public class LiteapiHotelClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiteapiHotelClientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	CommandLineRunner run(LiteApiService service) {
		return args -> {
			System.out.println("=== LiteAPI Hotel Search Client Started ===\n");

			Scanner scanner = new Scanner(System.in);

			System.out.println("Enter a city name: (eg New York)");
			String city = scanner.nextLine().trim();

			System.out.println("Enter a country code: (eg US)");
			String country = scanner.nextLine().trim();

			if (city.isEmpty()) {
				city = "New York";
				System.out.println("→ Using default city: New York");
			}
			if (country.isEmpty()) {
				country = "US";
				System.out.println("→ Using default country: US");
			}
			//String city = args.length > 0 ? args[0] : "Mumbai";
			//String country = args.length > 1 ? args[1] : "IN";

			service.searchHotelsByCity(city, country);
		};
	}
}
