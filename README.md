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

Below are the images for the 2 flows in the application (as mentioned above):
<img width="734" height="332" alt="image" src="https://github.com/user-attachments/assets/bbe31717-6416-4d25-9f23-5cf476ffb093" />
<img width="710" height="336" alt="image" src="https://github.com/user-attachments/assets/1cc1ff61-32b4-4bf5-aa5a-0828aebc046e" />
<img width="860" height="427" alt="image" src="https://github.com/user-attachments/assets/dac90cdf-b3ce-4047-bf24-75878eabd1de" />



