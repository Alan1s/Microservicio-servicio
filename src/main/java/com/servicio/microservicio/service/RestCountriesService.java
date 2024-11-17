package com.servicio.microservicio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestCountriesService {

    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/name/%s";

    public String obtenerInformacionPais(String pais) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(REST_COUNTRIES_API_URL, pais);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Procesa el JSON para guardar solo datos relevantes
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode countryNode = rootNode.get(0); // Toma el primer país en el resultado
                String capital = countryNode.path("capital").get(0).asText();
                long population = countryNode.path("population").asLong();
                String region = countryNode.path("region").asText();

                return String.format("Capital: %s, Población: %d, Región: %s", capital, population, region);
            } else {
                throw new RuntimeException("Error al consultar la API Rest Countries");
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener información del país: " + e.getMessage());
        }
    }
}
