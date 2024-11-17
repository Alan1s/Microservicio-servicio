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

   // Crear un nuevo servicio
   public Servicio_dto crearServicio(Servicio_dto servicioDto) {
    // Mapea el DTO a la entidad
    Servicio servicio = modelMapper.map(servicioDto, Servicio.class);

    // Consulta la información del país (Rest Countries API)
    if (servicioDto.getPaisDestino() != null && !servicioDto.getPaisDestino().isEmpty()) {
        try {
            String informacionPais = restCountriesService.obtenerInformacionPais(servicioDto.getPaisDestino());
            servicio.setInformacionPais(informacionPais);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la información del país: " + e.getMessage());
        }
    }

    // Consulta información del clima (Weather API)
    if (servicioDto.getDestino() != null && !servicioDto.getDestino().isEmpty()) {
        try {
            String informeClima = weatherService.obtenerClima(servicioDto.getDestino());
            servicio.setInformeClima(informeClima);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el clima: " + e.getMessage());
        }
    }

    // Integración con Google Maps (Mapas o Rutas)
    if ("Transporte".equalsIgnoreCase(servicioDto.getFormato())) {
        // Generar la URL de la ruta para transporte
        if (servicioDto.getOrigen() != null && servicioDto.getDestino() != null) {
            String rutaUrl = googleMapsService.obtenerRuta(servicioDto.getOrigen(), servicioDto.getDestino());
            servicio.setInformacionMapa(rutaUrl); // Almacena la URL de la ruta
        }
    } else if ("Alojamiento".equalsIgnoreCase(servicioDto.getFormato())) {
        // Generar la URL del mapa estático para alojamiento
        if (servicioDto.getUbicacion() != null) {
            String mapaUrl = googleMapsService.obtenerMapa(servicioDto.getUbicacion());
            servicio.setInformacionMapa(mapaUrl); // Almacena la URL del mapa
        }
    }

    // Guarda el servicio en la base de datos
    Servicio nuevoServicio = servicio_repository.save(servicio);

    // Mapea la entidad de nuevo al DTO y la devuelve
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

            // Verifica si el país de destino cambió
            if (!servicioExistente.getPaisDestino().equals(servicioDto.getPaisDestino())) {
                servicioExistente.setPaisDestino(servicioDto.getPaisDestino());
                try {
                    String informacionPais = restCountriesService.obtenerInformacionPais(servicioDto.getPaisDestino());
                    servicioExistente.setInformacionPais(informacionPais);
                } catch (Exception e) {
                    throw new RuntimeException("Error al consultar la información del país: " + e.getMessage());
                }
            }

            // Recalcular clima y mapas si cambian los datos relevantes
            if (servicioDto.getDestino() != null && !servicioDto.getDestino().isEmpty()) {
                try {
                    String informeClima = weatherService.obtenerClima(servicioDto.getDestino());
                    servicioExistente.setInformeClima(informeClima);
                } catch (Exception e) {
                    throw new RuntimeException("Error al consultar el clima: " + e.getMessage());
                }
            }

            if ("Transporte".equalsIgnoreCase(servicioDto.getFormato())) {
                if (servicioDto.getOrigen() != null && servicioDto.getDestino() != null) {
                    String rutaUrl = googleMapsService.obtenerRuta(servicioDto.getOrigen(), servicioDto.getDestino());
                    servicioExistente.setInformacionMapa(rutaUrl);
                }
            } else if ("Alojamiento".equalsIgnoreCase(servicioDto.getFormato())) {
                if (servicioDto.getUbicacion() != null) {
                    String mapaUrl = googleMapsService.obtenerMapa(servicioDto.getUbicacion());
                    servicioExistente.setInformacionMapa(mapaUrl);
                }
            }

            // Guarda los cambios en la base de datos
            Servicio servicioActualizado = servicio_repository.save(servicioExistente);
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

