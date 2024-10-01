package com.servicio.microservicio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class Servicio_dto {
    private long id_servicio;
    private String nombre;
    private String descripcion;
    private double precio;
    private String formato;
}
