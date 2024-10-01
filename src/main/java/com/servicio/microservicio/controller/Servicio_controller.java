package com.servicio.microservicio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.servicio.microservicio.dto.Servicio_dto;
import com.servicio.microservicio.service.Servicio_service;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/servicio")
public class Servicio_controller {
    Servicio_service servicio_service;

    @Autowired
    public Servicio_controller(Servicio_service servicio_service) {
        this.servicio_service = servicio_service;
    }

    // Crear un nuevo servicio
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Servicio_dto> crearServicio(@RequestBody Servicio_dto servicioDto) {
        Servicio_dto nuevoServicio = servicio_service.crearServicio(servicioDto);
        return new ResponseEntity<>(nuevoServicio, HttpStatus.CREATED);
    }

    // Obtener todos los servicios
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Servicio_dto>> obtenerTodosLosServicios() {
        List<Servicio_dto> servicios = servicio_service.obtenerTodosLosServicios();
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Servicio_dto> obtenerServicioPorId(@PathVariable("id") long id) {
        Servicio_dto servicio = servicio_service.obtenerServicioPorId(id);
        
        if (servicio != null) {
            return new ResponseEntity<>(servicio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Servicio_dto> actualizarServicio(@PathVariable("id") long id, @RequestBody Servicio_dto servicioDto) {
        Servicio_dto servicioActualizado = servicio_service.actualizarServicio(id, servicioDto);

        if (servicioActualizado != null) {
            return new ResponseEntity<>(servicioActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un servicio por ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable("id") long id) {
        boolean eliminado = servicio_service.eliminarServicio(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para buscar servicios por nombre
    @GetMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Servicio_dto>> buscarServicioPorNombre(@RequestParam("nombre") String nombre) {
        List<Servicio_dto> servicios = servicio_service.buscarServicioPorNombre(nombre);
        if (servicios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(servicios, HttpStatus.OK);
        }
    }
    
}
