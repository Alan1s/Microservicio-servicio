package com.servicio.microservicio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String obtenerClima(String latitud, String longitud) {
    String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", latitud, longitud, apiKey);

    try {
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            // Extraer solo los campos necesarios
            ObjectNode result = objectMapper.createObjectNode();
            result.set("coord", root.get("coord"));
            result.set("weather", root.get("weather"));

            return objectMapper.writeValueAsString(result);
        } else {
            throw new RuntimeException("Error al obtener el clima: " + response.getStatusCode() + " - " + response.getBody());
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al consultar el clima: " + e.getMessage(), e);
    }
}

}
