package com.servicio.microservicio.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servicio.microservicio.dto.Servicio_dto;
import com.servicio.microservicio.entities.Servicio;
import com.servicio.microservicio.repositories.Servicio_repository;


@Service
public class Servicio_service {
    @Autowired
    private Servicio_repository servicio_repository;
    ModelMapper modelMapper;

    @Autowired
    private GoogleMapsService googleMapsService;

    @Autowired
    private RestCountriesService restCountriesService;

    @Autowired
    private WeatherService weatherService;

    public Servicio_service(Servicio_repository servicio_repository_repository, ModelMapper modelMapper) {
        this.servicio_repository = servicio_repository;
        this.modelMapper = modelMapper;
    }

    public Servicio crearServicio(Servicio servicio) {
        return servicio_repository.save(servicio);
    }

   // Crear un nuevo servicio con las tres APIs
    public Servicio_dto crearServicio(Servicio_dto servicioDto) {
        // Mapea el DTO a la entidad
        Servicio servicio = modelMapper.map(servicioDto, Servicio.class);

        // 1. Llamada a la API de Rest Countries
        if (servicioDto.getPaisDestino() != null && !servicioDto.getPaisDestino().isEmpty()) {
            try {
                String informacionPais = restCountriesService.obtenerInformacionPais(servicioDto.getPaisDestino());
                servicio.setInformacionPais(informacionPais);
            } catch (Exception e) {
                throw new RuntimeException("Error al consultar la información del país: " + e.getMessage());
            }
        }

        // 2. Llamada a la API de Google Maps
        if (servicioDto.getFormato().equalsIgnoreCase("Alojamiento") && servicioDto.getUbicacion() != null) {
            try {
                String mapaUrl = googleMapsService.obtenerMapa(servicioDto.getUbicacion());
                servicio.setInformacionMapa(mapaUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error al generar el mapa: " + e.getMessage());
            }
        } else if (servicioDto.getFormato().equalsIgnoreCase("Transporte")
                && servicioDto.getOrigen() != null && servicioDto.getDestino() != null) {
            try {
                String rutaMapaUrl = googleMapsService.obtenerRuta(servicioDto.getOrigen(), servicioDto.getDestino());
                servicio.setInformacionMapa(rutaMapaUrl);
            } catch (Exception e) {
                throw new RuntimeException("Error al generar la ruta del mapa: " + e.getMessage());
            }
        }

        // 3. Llamada a la API del clima si es un servicio de transporte
    if (servicioDto.getFormato().equalsIgnoreCase("Transporte") && servicioDto.getDestino() != null) {
        try {
            String latitud = "4.60971";  // Latitud de ejemplo
            String longitud = "-74.08175"; // Longitud de ejemplo

            // Llama al servicio del clima
            String informeClima = weatherService.obtenerClima(latitud, longitud);
            servicio.setInformeClima(informeClima);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el clima: " + e.getMessage());
        }
    }
        // Guarda el servicio en la base de datos
        Servicio nuevoServicio = servicio_repository.save(servicio);

        // Mapea la entidad de vuelta al DTO y retorna
        return modelMapper.map(nuevoServicio, Servicio_dto.class);
    }

    // Obtener todos los servicios
    public List<Servicio_dto> obtenerTodosLosServicios() {
        List<Servicio> servicios = (List<Servicio>) servicio_repository.findAll();
        return servicios.stream()
                .map(servicio -> modelMapper.map(servicio, Servicio_dto.class))
                .collect(Collectors.toList());
    }

    // Obtener un servicio por ID
    public Servicio_dto obtenerServicioPorId(long id) {
        Servicio servicio = servicio_repository.findById(id).orElse(null);
        if (servicio != null) {
            return modelMapper.map(servicio, Servicio_dto.class);
        }
        return null; // Devuelve null si no se encuentra el servicio
    }
    

    // Actualizar un servicio por ID
    public Servicio_dto actualizarServicio(long id, Servicio_dto servicioDto) {
        Servicio servicioExistente = servicio_repository.findById(id).orElse(null);
    
        if (servicioExistente != null) {
            // Actualiza los campos básicos
            servicioExistente.setNombre(servicioDto.getNombre());
            servicioExistente.setDescripcion(servicioDto.getDescripcion());
            servicioExistente.setPrecio(servicioDto.getPrecio());
            servicioExistente.setFormato(servicioDto.getFormato());
            servicioExistente.setFechaSalida(servicioDto.getFechaSalida());
            servicioExistente.setFechaLlegada(servicioDto.getFechaLlegada());
    
            // 1. Actualización con Rest Countries
            if (!servicioExistente.getPaisDestino().equals(servicioDto.getPaisDestino())) {
                servicioExistente.setPaisDestino(servicioDto.getPaisDestino());
                try {
                    String informacionPais = restCountriesService.obtenerInformacionPais(servicioDto.getPaisDestino());
                    servicioExistente.setInformacionPais(informacionPais);
                } catch (Exception e) {
                    throw new RuntimeException("Error al consultar la información del país: " + e.getMessage());
                }
            }
    
            // 2. Actualización con Google Maps
            if (servicioDto.getFormato().equalsIgnoreCase("Alojamiento") && servicioDto.getUbicacion() != null) {
                try {
                    String mapaUrl = googleMapsService.obtenerMapa(servicioDto.getUbicacion());
                    servicioExistente.setInformacionMapa(mapaUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Error al generar el mapa: " + e.getMessage());
                }
            } else if (servicioDto.getFormato().equalsIgnoreCase("Transporte")
                    && servicioDto.getOrigen() != null && servicioDto.getDestino() != null) {
                try {
                    String rutaMapaUrl = googleMapsService.obtenerRuta(servicioDto.getOrigen(), servicioDto.getDestino());
                    servicioExistente.setInformacionMapa(rutaMapaUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Error al generar la ruta del mapa: " + e.getMessage());
                }
            }
    
            // Llama a OpenWeatherMap API para obtener el clima
        if (servicioDto.getFormato().equalsIgnoreCase("Transporte") && servicioDto.getDestino() != null) {
            try {
                String latitud = "4.60971";  // Latitud de ejemplo
                String longitud = "-74.08175"; // Longitud de ejemplo
                String informeClima = weatherService.obtenerClima(latitud, longitud);
                servicioExistente.setInformeClima(informeClima);
            } catch (Exception e) {
                throw new RuntimeException("Error al consultar el clima: " + e.getMessage());
            }
        }
    
            // Guarda los cambios en la base de datos
            Servicio servicioActualizado = servicio_repository.save(servicioExistente);
    
            // Mapea la entidad actualizada al DTO y la retorna
            return modelMapper.map(servicioActualizado, Servicio_dto.class);
        }
    
        return null; // Devuelve null si el servicio no fue encontrado
    }
    
    
    // Eliminar un servicio por ID
    public boolean eliminarServicio(long id) {
        if (servicio_repository.existsById(id)) {
            servicio_repository.deleteById(id);
            return true;
        }
        return false;
    }
}

