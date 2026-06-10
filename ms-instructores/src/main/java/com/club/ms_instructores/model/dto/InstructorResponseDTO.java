package com.club.ms_instructores.model.dto;

import com.club.ms_instructores.model.entity.Instructor;
import lombok.Data;

@Data
public class InstructorResponseDTO {

    private Long id;
    private String nombre;
    private String rut;
    private String email;
    private String telefono;
    private String especialidad;
    private Boolean activo;

    public static InstructorResponseDTO fromEntity(Instructor instructor) {
        InstructorResponseDTO dto = new InstructorResponseDTO();
        dto.setId(instructor.getId());
        dto.setNombre(instructor.getNombre());
        dto.setRut(instructor.getRut());
        dto.setEmail(instructor.getEmail());
        dto.setTelefono(instructor.getTelefono());
        dto.setEspecialidad(instructor.getEspecialidad());
        dto.setActivo(instructor.getActivo());
        return dto;
    }
}
