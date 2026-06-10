package com.club.ms_actividades.service;

import com.club.ms_actividades.exception.RecursoNoEncontradoException;
import com.club.ms_actividades.exception.ReglaNegocioException;
import com.club.ms_actividades.model.dto.ActividadRequestDTO;
import com.club.ms_actividades.model.dto.ActividadResponseDTO;
import com.club.ms_actividades.model.entity.Actividad;
import com.club.ms_actividades.repository.ActividadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    private ActividadRequestDTO crearDtoValido() {
        ActividadRequestDTO dto = new ActividadRequestDTO();
        dto.setNombre("Yoga");
        dto.setDescripcion("Clase de yoga matutina");
        dto.setCupoMaximo(20);
        dto.setPrecio(new BigDecimal("15.00"));
        return dto;
    }

    private Actividad crearActividadExistente() {
        Actividad actividad = new Actividad();
        actividad.setId(1L);
        actividad.setNombre("Yoga");
        actividad.setDescripcion("Clase de yoga matutina");
        actividad.setCupoMaximo(20);
        actividad.setPrecio(new BigDecimal("15.00"));
        actividad.setActivo(true);
        return actividad;
    }

    @Test
    @DisplayName("Given datos validos, when crearActividad, then crea actividad activa")
    void crearActividad_exitoso() {
        ActividadRequestDTO dto = crearDtoValido();
        when(actividadRepository.findByNombre(dto.getNombre())).thenReturn(Optional.empty());
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(invocation -> {
            Actividad a = invocation.getArgument(0);
            a.setId(1L);
            return a;
        });

        ActividadResponseDTO resultado = actividadService.crearActividad(dto);

        assertNotNull(resultado.getId());
        assertEquals("Yoga", resultado.getNombre());
        assertTrue(resultado.getActivo());

        ArgumentCaptor<Actividad> captor = ArgumentCaptor.forClass(Actividad.class);
        verify(actividadRepository).save(captor.capture());
        assertEquals(20, captor.getValue().getCupoMaximo());
    }

    @Test
    @DisplayName("Given nombre duplicado, when crearActividad, then lanza ReglaNegocioException")
    void crearActividad_nombreDuplicado() {
        ActividadRequestDTO dto = crearDtoValido();
        when(actividadRepository.findByNombre(dto.getNombre())).thenReturn(Optional.of(crearActividadExistente()));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> actividadService.crearActividad(dto));
        assertTrue(ex.getMessage().contains("ya existe"));
        verify(actividadRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(actividadRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> actividadService.buscarPorId(99L));
        assertEquals("Actividad no encontrada con el ID proporcionado.", ex.getMessage());
    }

    @Test
    @DisplayName("Given actividades en BD, when listarTodas, then retorna lista")
    void listarTodas_retornaLista() {
        List<Actividad> actividades = List.of(crearActividadExistente());
        when(actividadRepository.findAll()).thenReturn(actividades);

        List<ActividadResponseDTO> resultado = actividadService.listarTodas();

        assertEquals(1, resultado.size());
        verify(actividadRepository).findAll();
    }
}
