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

     // Campos específicos del país de destino
    private String paisDestino;   
    private String informacionPais;

    // Campos para mapas
    private String ubicacion; // Para alojamientos
    private String origen;    // Para transporte
    private String destino;   // Para transporte
    private String informacionMapa; // URL del mapa o ruta

    private String informeClima; // Informe del clima
}

