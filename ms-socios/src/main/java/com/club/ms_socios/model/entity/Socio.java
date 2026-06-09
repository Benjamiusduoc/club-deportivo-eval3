package com.club.ms_socios.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "socios")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, unique = true, length = 15)
    private String rut;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @Column(nullable = false)
    private Boolean activo;
}
