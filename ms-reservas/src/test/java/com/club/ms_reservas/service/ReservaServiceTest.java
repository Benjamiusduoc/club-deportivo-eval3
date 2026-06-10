package com.club.ms_reservas.service;

import com.club.ms_reservas.client.ActividadClient;
import com.club.ms_reservas.client.InstructorClient;
import com.club.ms_reservas.client.SocioClient;
import com.club.ms_reservas.exception.RecursoNoEncontradoException;
import com.club.ms_reservas.exception.ReglaNegocioException;
import com.club.ms_reservas.model.dto.ReservaRequestDTO;
import com.club.ms_reservas.model.dto.ReservaResponseDTO;
import com.club.ms_reservas.model.entity.Reserva;
import com.club.ms_reservas.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private SocioClient socioClient;

    @Mock
    private ActividadClient actividadClient;

    @Mock
    private InstructorClient instructorClient;

    @InjectMocks
    private ReservaService reservaService;

    private ReservaRequestDTO crearDtoValido() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setIdSocio(1L);
        dto.setIdActividad(1L);
        dto.setIdInstructor(1L);
        dto.setFecha(LocalDate.of(2026, 6, 15));
        dto.setHoraInicio(LocalTime.of(10, 0));
        dto.setHoraFin(LocalTime.of(11, 0));
        return dto;
    }

    @Test
    @DisplayName("Given datos validos, when crearReserva, then crea reserva CONFIRMADA")
    void crearReserva_exitoso() {
        ReservaRequestDTO dto = crearDtoValido();
        doNothing().when(socioClient).validarSocioActivo(dto.getIdSocio());
        doNothing().when(actividadClient).validarActividadExistente(dto.getIdActividad());
        doNothing().when(instructorClient).validarInstructorExistente(dto.getIdInstructor());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        ReservaResponseDTO resultado = reservaService.crearReserva(dto);

        assertNotNull(resultado.getId());
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(dto.getIdSocio(), resultado.getIdSocio());

        ArgumentCaptor<Reserva> captor = ArgumentCaptor.forClass(Reserva.class);
        verify(reservaRepository).save(captor.capture());
        assertEquals(LocalDate.now(), captor.getValue().getFechaCreacion());
    }

    @Test
    @DisplayName("Given socio no activo, when crearReserva, then lanza ReglaNegocioException")
    void crearReserva_socioNoActivo() {
        ReservaRequestDTO dto = crearDtoValido();
        doThrow(new ReglaNegocioException("Socio no activo con id: " + dto.getIdSocio()))
                .when(socioClient).validarSocioActivo(dto.getIdSocio());

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> reservaService.crearReserva(dto));
        assertTrue(ex.getMessage().contains("Socio no activo"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> reservaService.buscarPorId(99L));
        assertEquals("Reserva no encontrada con el ID proporcionado.", ex.getMessage());
    }
}
