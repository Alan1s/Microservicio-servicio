package com.servicio.microservicio.dto;

import java.time.LocalDateTime;

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

    // Campos específicos para transporte
    private String tipoTransporte;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private String trayecto;

    private String paisDestino;   
    private String informacionPais;
}
