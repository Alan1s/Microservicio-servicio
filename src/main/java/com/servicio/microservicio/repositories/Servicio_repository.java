package com.servicio.microservicio.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.servicio.microservicio.entities.Servicio;

@Repository
public interface Servicio_repository extends CrudRepository <Servicio, Long>{
    List<Servicio> findByNombre(String nombre);
}
