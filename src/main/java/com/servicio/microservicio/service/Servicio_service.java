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

public class Servicio_service {

    Servicio_repository servicio_repository;
    ModelMapper modelMapper;

    @Autowired
    public Servicio_service(Servicio_repository servicio_repository_repository, ModelMapper modelMapper) {
        this.servicio_repository = servicio_repository;
        this.modelMapper = modelMapper;
    }

    // Crear un nuevo servicio
    public Servicio_dto crearServicio(Servicio_dto servicioDto) {
        Servicio servicio = modelMapper.map(servicioDto, Servicio.class);
        Servicio nuevoServicio = servicio_repository.save(servicio);
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
            servicioExistente.setNombre(servicioDto.getNombre());
            servicioExistente.setDescripcion(servicioDto.getDescripcion());
            servicioExistente.setPrecio(servicioDto.getPrecio());
            servicioExistente.setFormato(servicioDto.getFormato());
    
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

    // Buscar un servicio por nombre
    public List<Servicio_dto> buscarServicioPorNombre(String nombre) {
        List<Servicio> servicios = servicio_repository.findByNombre(nombre);
        return servicios.stream()
                .map(servicio -> modelMapper.map(servicio, Servicio_dto.class))
                .collect(Collectors.toList());
    }
}
