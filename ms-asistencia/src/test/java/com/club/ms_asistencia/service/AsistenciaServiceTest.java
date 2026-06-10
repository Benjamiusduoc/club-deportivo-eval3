package com.club.ms_asistencia.service;

import com.club.ms_asistencia.client.ReservaClient;
import com.club.ms_asistencia.client.SocioClient;
import com.club.ms_asistencia.exception.RecursoNoEncontradoException;
import com.club.ms_asistencia.model.dto.AsistenciaRequestDTO;
import com.club.ms_asistencia.model.dto.AsistenciaResponseDTO;
import com.club.ms_asistencia.model.entity.Asistencia;
import com.club.ms_asistencia.repository.AsistenciaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private SocioClient socioClient;

    @InjectMocks
    private AsistenciaService asistenciaService;

    private AsistenciaRequestDTO crearDtoValido() {
        AsistenciaRequestDTO dto = new AsistenciaRequestDTO();
        dto.setIdReserva(1L);
        dto.setIdSocio(1L);
        dto.setFecha(LocalDate.of(2026, 6, 15));
        dto.setPresente(true);
        dto.setObservacion("Asistio puntualmente");
        return dto;
    }

    @Test
    @DisplayName("Given datos validos, when crearAsistencia, then registra asistencia")
    void crearAsistencia_exitoso() {
        AsistenciaRequestDTO dto = crearDtoValido();
        doNothing().when(reservaClient).validarReservaExistente(dto.getIdReserva());
        doNothing().when(socioClient).validarSocioActivo(dto.getIdSocio());
        when(asistenciaRepository.save(any(Asistencia.class))).thenAnswer(invocation -> {
            Asistencia a = invocation.getArgument(0);
            a.setId(1L);
            return a;
        });

        AsistenciaResponseDTO resultado = asistenciaService.crearAsistencia(dto);

        assertNotNull(resultado.getId());
        assertTrue(resultado.getPresente());
        assertEquals(dto.getIdReserva(), resultado.getIdReserva());

        ArgumentCaptor<Asistencia> captor = ArgumentCaptor.forClass(Asistencia.class);
        verify(asistenciaRepository).save(captor.capture());
        assertEquals(dto.getObservacion(), captor.getValue().getObservacion());
    }

    @Test
    @DisplayName("Given reserva inexistente, when crearAsistencia, then lanza RecursoNoEncontradoException")
    void crearAsistencia_reservaNoExiste() {
        AsistenciaRequestDTO dto = crearDtoValido();
        doThrow(new RecursoNoEncontradoException("Reserva no encontrada: " + dto.getIdReserva()))
                .when(reservaClient).validarReservaExistente(dto.getIdReserva());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> asistenciaService.crearAsistencia(dto));
        assertTrue(ex.getMessage().contains("Reserva no encontrada"));
        verify(asistenciaRepository, never()).save(any());
    }
}
