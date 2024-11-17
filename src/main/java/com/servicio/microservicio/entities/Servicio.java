package com.servicio.microservicio.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_servicio;
    private String nombre;
    private String descripcion;
    private double precio;
    private String formato; // Alojamiento, Transporte, etc.

    // Campos específicos para transporte
    private String tipoTransporte; // Terrestre, Aéreo, etc.
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private String trayecto; // Ruta del transporte

    // Campos específicos del país de destino
    private String paisDestino;   // Nombre del país
    private String informacionPais; // Información recibida de la API (capital, población, etc.)
}
