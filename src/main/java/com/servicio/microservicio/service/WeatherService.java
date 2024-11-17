package com.servicio.microservicio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    @Value("${weatherapi.api.key}")
    private String apiKey;

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    public String obtenerClima(String ciudad) {
        // Construir la URL para la solicitud al API
        String url = UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                .queryParam("q", ciudad)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric") // Métricas (Celsius)
                .queryParam("lang", "es") // Idioma en español
                .toUriString();

        // Hacer la solicitud al API
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody(); // Devuelve la respuesta JSON del clima
            } else {
                throw new RuntimeException("Error al consultar la API del clima: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el clima: " + e.getMessage());
        }
    }
}
