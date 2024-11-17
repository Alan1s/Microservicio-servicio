package com.servicio.microservicio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private static final String STATIC_MAPS_API_URL = "https://maps.googleapis.com/maps/api/staticmap";
    private static final String DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";

    // Obtener mapa estático para una ubicación
    public String obtenerMapa(String ubicacion) {
        return UriComponentsBuilder.fromHttpUrl(STATIC_MAPS_API_URL)
                .queryParam("center", ubicacion)
                .queryParam("zoom", 15)
                .queryParam("size", "600x400")
                .queryParam("markers", "color:red|" + ubicacion)
                .queryParam("key", apiKey)
                .toUriString();
    }

    // Obtener una ruta entre dos ubicaciones
    public String obtenerRuta(String origen, String destino) {
        return UriComponentsBuilder.fromHttpUrl(DIRECTIONS_API_URL)
                .queryParam("origin", origen)
                .queryParam("destination", destino)
                .queryParam("key", apiKey)
                .toUriString();
    }
}
