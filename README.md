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
