package com.club.ms_socios.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // genera Getters y Setters automáticamente
public class SocioRequestDTO {

    // @NotBlank asegura que no venga nulo ni vacío
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 8, max = 15, message = "El RUT debe tener entre 8 y 15 caracteres")
    private String rut;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    // no pusimos id, ni fechaInscripcion, ni activo.
    // Esos datos los maneja el sistema internamente, no el usuario.
}