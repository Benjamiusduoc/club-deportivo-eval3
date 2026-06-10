package com.club.ms_instructores.service;

import com.club.ms_instructores.exception.RecursoNoEncontradoException;
import com.club.ms_instructores.exception.ReglaNegocioException;
import com.club.ms_instructores.model.dto.InstructorRequestDTO;
import com.club.ms_instructores.model.dto.InstructorResponseDTO;
import com.club.ms_instructores.model.entity.Instructor;
import com.club.ms_instructores.repository.InstructorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private static final Logger log = LoggerFactory.getLogger(InstructorService.class);

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public InstructorResponseDTO crearInstructor(InstructorRequestDTO dto) {
        log.info("Iniciando registro de nuevo instructor con RUT: {}", dto.getRut());

        Optional<Instructor> existenteRut = instructorRepository.findByRut(dto.getRut());
        if (existenteRut.isPresent()) {
            log.error("Validación fallida: El RUT {} ya se encuentra registrado", dto.getRut());
            throw new ReglaNegocioException("El RUT ingresado ya pertenece a un instructor existente.");
        }

        Optional<Instructor> existenteEmail = instructorRepository.findByEmail(dto.getEmail());
        if (existenteEmail.isPresent()) {
            log.error("Validación fallida: El email {} ya se encuentra registrado", dto.getEmail());
            throw new ReglaNegocioException("El email ingresado ya pertenece a un instructor existente.");
        }

        Instructor instructor = new Instructor();
        instructor.setNombre(dto.getNombre());
        instructor.setRut(dto.getRut());
        instructor.setEmail(dto.getEmail());
        instructor.setTelefono(dto.getTelefono());
        instructor.setEspecialidad(dto.getEspecialidad());
        instructor.setActivo(true);

        Instructor guardado = instructorRepository.save(instructor);
        log.info("Instructor registrado exitosamente con ID: {}", guardado.getId());

        return InstructorResponseDTO.fromEntity(guardado);
    }

    public List<InstructorResponseDTO> listarTodos() {
        log.info("Consultando la lista completa de instructores");
        return instructorRepository.findAll()
                .stream()
                .map(InstructorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InstructorResponseDTO> listarActivos() {
        log.info("Consultando instructores activos");
        return instructorRepository.findByActivoTrue()
                .stream()
                .map(InstructorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public InstructorResponseDTO buscarPorId(Long id) {
        log.info("Buscando instructor con ID: {}", id);
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el instructor con ID: {}", id);
                    return new RecursoNoEncontradoException("Instructor no encontrado con el ID proporcionado.");
                });
        return InstructorResponseDTO.fromEntity(instructor);
    }

    public InstructorResponseDTO actualizar(Long id, InstructorRequestDTO dto) {
        log.info("Actualizando instructor con ID: {}", id);

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el instructor con ID: {}", id);
                    return new RecursoNoEncontradoException("Instructor no encontrado con el ID proporcionado.");
                });

        Optional<Instructor> duplicadoRut = instructorRepository.findByRut(dto.getRut());
        if (duplicadoRut.isPresent() && !duplicadoRut.get().getId().equals(id)) {
            log.error("Validación fallida: El RUT {} ya pertenece a otro instructor", dto.getRut());
            throw new ReglaNegocioException("El RUT ingresado ya pertenece a un instructor existente.");
        }

        Optional<Instructor> duplicadoEmail = instructorRepository.findByEmail(dto.getEmail());
        if (duplicadoEmail.isPresent() && !duplicadoEmail.get().getId().equals(id)) {
            log.error("Validación fallida: El email {} ya pertenece a otro instructor", dto.getEmail());
            throw new ReglaNegocioException("El email ingresado ya pertenece a un instructor existente.");
        }

        instructor.setNombre(dto.getNombre());
        instructor.setRut(dto.getRut());
        instructor.setEmail(dto.getEmail());
        instructor.setTelefono(dto.getTelefono());
        instructor.setEspecialidad(dto.getEspecialidad());

        Instructor actualizado = instructorRepository.save(instructor);
        log.info("Instructor con ID: {} actualizado exitosamente", id);

        return InstructorResponseDTO.fromEntity(actualizado);
    }

    public InstructorResponseDTO desactivar(Long id) {
        log.info("Desactivando instructor con ID: {}", id);

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el instructor con ID: {}", id);
                    return new RecursoNoEncontradoException("Instructor no encontrado con el ID proporcionado.");
                });

        instructor.setActivo(false);
        Instructor desactivado = instructorRepository.save(instructor);
        log.info("Instructor con ID: {} desactivado exitosamente", id);

        return InstructorResponseDTO.fromEntity(desactivado);
    }
}
