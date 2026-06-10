package com.club.ms_configuracion.service;

import com.club.ms_configuracion.exception.RecursoNoEncontradoException;
import com.club.ms_configuracion.exception.ReglaNegocioException;
import com.club.ms_configuracion.model.dto.ConfiguracionRequestDTO;
import com.club.ms_configuracion.model.dto.ConfiguracionResponseDTO;
import com.club.ms_configuracion.model.entity.Configuracion;
import com.club.ms_configuracion.repository.ConfiguracionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfiguracionService {

    private static final Logger log = LoggerFactory.getLogger(ConfiguracionService.class);

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    public ConfiguracionResponseDTO crear(ConfiguracionRequestDTO dto) {
        log.info("Iniciando registro de nueva configuracion: {}", dto.getClave());

        Optional<Configuracion> existente = configuracionRepository.findByClave(dto.getClave());
        if (existente.isPresent()) {
            log.error("Validacion fallida: La clave {} ya existe", dto.getClave());
            throw new ReglaNegocioException("La configuracion con la clave '" + dto.getClave() + "' ya existe.");
        }

        Configuracion configuracion = new Configuracion();
        configuracion.setClave(dto.getClave());
        configuracion.setValor(dto.getValor());
        configuracion.setDescripcion(dto.getDescripcion());

        Configuracion guardada = configuracionRepository.save(configuracion);
        log.info("Configuracion registrada exitosamente con ID: {}", guardada.getId());

        return ConfiguracionResponseDTO.fromEntity(guardada);
    }

    public List<ConfiguracionResponseDTO> listarTodas() {
        log.info("Consultando la lista completa de configuraciones");
        return configuracionRepository.findAllByOrderByClaveAsc()
                .stream()
                .map(ConfiguracionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ConfiguracionResponseDTO buscarPorId(Long id) {
        log.info("Buscando configuracion con ID: {}", id);
        Configuracion configuracion = configuracionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la configuracion con ID: {}", id);
                    return new RecursoNoEncontradoException("Configuracion no encontrada con el ID proporcionado.");
                });
        return ConfiguracionResponseDTO.fromEntity(configuracion);
    }

    public ConfiguracionResponseDTO buscarPorClave(String clave) {
        log.info("Buscando configuracion con clave: {}", clave);
        Configuracion configuracion = configuracionRepository.findByClave(clave)
                .orElseThrow(() -> {
                    log.error("No se encontró la configuracion con clave: {}", clave);
                    return new RecursoNoEncontradoException("Configuracion no encontrada con la clave proporcionada.");
                });
        return ConfiguracionResponseDTO.fromEntity(configuracion);
    }

    public ConfiguracionResponseDTO actualizar(Long id, ConfiguracionRequestDTO dto) {
        log.info("Actualizando configuracion con ID: {}", id);

        Configuracion configuracion = configuracionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la configuracion con ID: {}", id);
                    return new RecursoNoEncontradoException("Configuracion no encontrada con el ID proporcionado.");
                });

        Optional<Configuracion> duplicado = configuracionRepository.findByClave(dto.getClave());
        if (duplicado.isPresent() && !duplicado.get().getId().equals(id)) {
            log.error("Validacion fallida: La clave {} ya pertenece a otra configuracion", dto.getClave());
            throw new ReglaNegocioException("La configuracion con la clave '" + dto.getClave() + "' ya existe.");
        }

        configuracion.setClave(dto.getClave());
        configuracion.setValor(dto.getValor());
        configuracion.setDescripcion(dto.getDescripcion());

        Configuracion actualizada = configuracionRepository.save(configuracion);
        log.info("Configuracion con ID: {} actualizada exitosamente", id);

        return ConfiguracionResponseDTO.fromEntity(actualizada);
    }

    public ConfiguracionResponseDTO eliminar(Long id) {
        log.info("Eliminando configuracion con ID: {}", id);

        Configuracion configuracion = configuracionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró la configuracion con ID: {}", id);
                    return new RecursoNoEncontradoException("Configuracion no encontrada con el ID proporcionado.");
                });

        configuracionRepository.delete(configuracion);
        log.info("Configuracion con ID: {} eliminada exitosamente", id);

        return ConfiguracionResponseDTO.fromEntity(configuracion);
    }
}
