package com.club.ms_inventario.service;

import com.club.ms_inventario.exception.RecursoNoEncontradoException;
import com.club.ms_inventario.model.dto.ItemRequestDTO;
import com.club.ms_inventario.model.dto.ItemResponseDTO;
import com.club.ms_inventario.model.entity.Item;
import com.club.ms_inventario.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemRequestDTO crearDtoValido() {
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setNombre("Pelota de futbol");
        dto.setCantidadTotal(20);
        dto.setIdActividad(1L);
        dto.setEstado("DISPONIBLE");
        return dto;
    }

    private Item crearItemExistente() {
        Item item = new Item();
        item.setId(1L);
        item.setNombre("Pelota de futbol");
        item.setCantidadTotal(20);
        item.setCantidadDisponible(20);
        item.setIdActividad(1L);
        item.setEstado("DISPONIBLE");
        return item;
    }

    @Test
    @DisplayName("Given datos validos, when crear, then cantidadDisponible = cantidadTotal")
    void crear_exitoso() {
        ItemRequestDTO dto = crearDtoValido();
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item i = invocation.getArgument(0);
            i.setId(1L);
            return i;
        });

        ItemResponseDTO resultado = itemService.crear(dto);

        assertNotNull(resultado.getId());
        assertEquals("Pelota de futbol", resultado.getNombre());
        assertEquals(resultado.getCantidadTotal(), resultado.getCantidadDisponible());

        ArgumentCaptor<Item> captor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(captor.capture());
        assertEquals(20, captor.getValue().getCantidadDisponible());
        assertEquals(dto.getCantidadTotal(), captor.getValue().getCantidadDisponible());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> itemService.buscarPorId(99L));
        assertEquals("Item no encontrado con el ID proporcionado.", ex.getMessage());
    }

    @Test
    @DisplayName("Given items en BD, when listarTodos, then retorna lista")
    void listarTodos_retornaLista() {
        List<Item> items = List.of(crearItemExistente());
        when(itemRepository.findAll()).thenReturn(items);

        List<ItemResponseDTO> resultado = itemService.listarTodos();

        assertEquals(1, resultado.size());
        verify(itemRepository).findAll();
    }
}
