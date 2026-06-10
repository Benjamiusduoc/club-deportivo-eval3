package com.club.ms_actividades.service;

import com.club.ms_actividades.exception.RecursoNoEncontradoException;
import com.club.ms_actividades.exception.ReglaNegocioException;
import com.club.ms_actividades.model.dto.ActividadRequestDTO;
import com.club.ms_actividades.model.dto.ActividadResponseDTO;
import com.club.ms_actividades.model.entity.Actividad;
import com.club.ms_actividades.repository.ActividadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    private static final Logger log = LoggerFactory.getLogger(ActividadService.class);

    private final ActividadRepository actividadRepository;

    public ActividadService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    public ActividadResponseDTO crearActividad(ActividadRequestDTO dto) {
        log.info("Iniciando registro de nueva actividad: {}", dto.getNombre());

        Optional<Actividad> existente = actividadRepository.findByNombre(dto.getNombre());
        if (existente.isPresent()) {
            log.error("Validación fallida: La actividad {} ya existe", dto.getNombre());
            throw new ReglaNegocioException("La actividad con el nombre '" + dto.getNombre() + "' ya existe.");
        }

        Actividad actividad = new Actividad();
        actividad.setNombre(dto.getNombre());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setCupoMaximo(dto.getCupoMaximo());
        actividad.setPrecio(dto.getPrecio());
        actividad.setActivo(true);

        Actividad guardada = actividadRepository.save(actividad);
        log.info("Actividad registrada exitosamente con ID: {}", guardada.getId());

        return ActividadResponseDTO.fromEntity(guardada);
    }

    public List<ActividadResponseDTO> listarTodas() {
        log.info("Consultando la lista completa de actividades");
        return actividadRepository.findAll()
                .stream()
                .map(ActividadResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ActividadResponseDTO> listarActivas() {
        log.info("Consultando actividades activas");
        return actividadRepository.findByActivoTrue()
                .stream()
                .map(ActividadResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ActividadResponseDTO buscarPorId(Long id) {
        log.info("Buscando actividad con ID: {}", id);
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la actividad con ID: {}", id);
                    return new RecursoNoEncontradoException("Actividad no encontrada con el ID proporcionado.");
                });
        return ActividadResponseDTO.fromEntity(actividad);
    }

    public ActividadResponseDTO actualizar(Long id, ActividadRequestDTO dto) {
        log.info("Actualizando actividad con ID: {}", id);

        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la actividad con ID: {}", id);
                    return new RecursoNoEncontradoException("Actividad no encontrada con el ID proporcionado.");
                });

        Optional<Actividad> duplicado = actividadRepository.findByNombre(dto.getNombre());
        if (duplicado.isPresent() && !duplicado.get().getId().equals(id)) {
            log.error("Validación fallida: El nombre {} ya pertenece a otra actividad", dto.getNombre());
            throw new ReglaNegocioException("La actividad con el nombre '" + dto.getNombre() + "' ya existe.");
        }

        actividad.setNombre(dto.getNombre());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setCupoMaximo(dto.getCupoMaximo());
        actividad.setPrecio(dto.getPrecio());

        Actividad actualizada = actividadRepository.save(actividad);
        log.info("Actividad con ID: {} actualizada exitosamente", id);

        return ActividadResponseDTO.fromEntity(actualizada);
    }

    public ActividadResponseDTO desactivar(Long id) {
        log.info("Desactivando actividad con ID: {}", id);

        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la actividad con ID: {}", id);
                    return new RecursoNoEncontradoException("Actividad no encontrada con el ID proporcionado.");
                });

        actividad.setActivo(false);
        Actividad desactivada = actividadRepository.save(actividad);
        log.info("Actividad con ID: {} desactivada exitosamente", id);

        return ActividadResponseDTO.fromEntity(desactivada);
    }
}
