# liteapi-hotel-client
Java application integrating LiteAPI Hotel Search 

# LiteAPI Hotel Search Client

A Spring Boot Java 21 application that integrates with the **LiteAPI Hotel Search API**.  
It allows searching hotels by city/country and retrieving available room rates.

## Features
- **Flow 1**: Search hotels by city name and country code
- **Flow 2**: Get detailed room rates for a selected hotel
- Interactive console input (no need to pass arguments every time)
- Clean separation of concerns (Client, Service, Models)
- Uses RestTemplate + Jackson for API communication

## Tech Stack
- Java 21
- Spring Boot 3.3.5
- Gradle
- RestTemplate (HTTP Client)
- Jackson (JSON parsing)

## Setup Instructions

### 1. Prerequisites
- Java 21 installed
- Gradle (or use Gradle Wrapper `./gradlew`)

### 2. Configure API Key
1. Go to [liteapi.travel](https://liteapi.travel) and create a free account
2. Copy your **Sandbox API Key** from the dashboard
3. Open `src/main/resources/application.properties`
4. Replace the `liteapi.api-key` value with your own key:

```properties
liteapi.api-key=your_sandbox_api_key_here
```

### To run the application
1. Run the file LiteapiHotelClientApplication in any IDE of your choice. 

Below are the images for the 2 flows in the application (as mentioned above):
Flow 1:
<img width="722" height="337" alt="image" src="https://github.com/user-attachments/assets/80e771c3-cd3a-4ac7-832c-4d745b26a57d" />

<img width="824" height="360" alt="image" src="https://github.com/user-attachments/assets/b93aecf3-806b-467b-9024-5d46f151eb6d" />

Flow 2:
<img width="848" height="368" alt="image" src="https://github.com/user-attachments/assets/7ac7c5fc-09cd-4ae5-9289-f2d61ef5720f" />

<img width="857" height="381" alt="image" src="https://github.com/user-attachments/assets/6d35f01c-93a4-4059-849c-c55b1cbc7447" />





