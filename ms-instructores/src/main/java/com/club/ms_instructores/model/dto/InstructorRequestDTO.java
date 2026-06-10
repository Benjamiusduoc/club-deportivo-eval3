package com.club.ms_instructores.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InstructorRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 8, max = 15, message = "El RUT debe tener entre 8 y 15 caracteres")
    private String rut;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    private String telefono;

    private String especialidad;
}
