package com.club.ms_inventario.service;

import com.club.ms_inventario.exception.RecursoNoEncontradoException;
import com.club.ms_inventario.model.dto.ItemRequestDTO;
import com.club.ms_inventario.model.dto.ItemResponseDTO;
import com.club.ms_inventario.model.entity.Item;
import com.club.ms_inventario.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponseDTO crear(ItemRequestDTO dto) {
        log.info("Iniciando registro de nuevo item: {}", dto.getNombre());

        Item item = new Item();
        item.setNombre(dto.getNombre());
        item.setCantidadTotal(dto.getCantidadTotal());
        item.setCantidadDisponible(dto.getCantidadTotal());
        item.setIdActividad(dto.getIdActividad());
        item.setEstado(dto.getEstado());

        Item guardado = itemRepository.save(item);
        log.info("Item registrado exitosamente con ID: {}", guardado.getId());

        return ItemResponseDTO.fromEntity(guardado);
    }

    public List<ItemResponseDTO> listarTodos() {
        log.info("Consultando la lista completa de items");
        return itemRepository.findAll()
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ItemResponseDTO buscarPorId(Long id) {
        log.info("Buscando item con ID: {}", id);
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el item con ID: {}", id);
                    return new RecursoNoEncontradoException("Item no encontrado con el ID proporcionado.");
                });
        return ItemResponseDTO.fromEntity(item);
    }

    public List<ItemResponseDTO> listarPorActividad(Long idActividad) {
        log.info("Consultando items por actividad ID: {}", idActividad);
        return itemRepository.findByIdActividad(idActividad)
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDTO> listarPorEstado(String estado) {
        log.info("Consultando items por estado: {}", estado);
        return itemRepository.findByEstado(estado)
                .stream()
                .map(ItemResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ItemResponseDTO actualizar(Long id, ItemRequestDTO dto) {
        log.info("Actualizando item con ID: {}", id);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el item con ID: {}", id);
                    return new RecursoNoEncontradoException("Item no encontrado con el ID proporcionado.");
                });

        item.setNombre(dto.getNombre());
        item.setCantidadTotal(dto.getCantidadTotal());
        item.setIdActividad(dto.getIdActividad());
        item.setEstado(dto.getEstado());

        Item actualizado = itemRepository.save(item);
        log.info("Item con ID: {} actualizado exitosamente", id);

        return ItemResponseDTO.fromEntity(actualizado);
    }

    public ItemResponseDTO eliminar(Long id) {
        log.info("Eliminando (baja logica) item con ID: {}", id);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el item con ID: {}", id);
                    return new RecursoNoEncontradoException("Item no encontrado con el ID proporcionado.");
                });

        item.setEstado("BAJA");
        Item desactivado = itemRepository.save(item);
        log.info("Item con ID: {} dado de baja exitosamente", id);

        return ItemResponseDTO.fromEntity(desactivado);
    }
}
