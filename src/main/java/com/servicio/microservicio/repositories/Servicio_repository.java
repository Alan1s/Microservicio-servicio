package com.servicio.microservicio.repositories;

import org.springframework.data.repository.CrudRepository;
import com.servicio.microservicio.entities.Servicio;

public interface Servicio_repository extends CrudRepository <Servicio, Long>{
}
