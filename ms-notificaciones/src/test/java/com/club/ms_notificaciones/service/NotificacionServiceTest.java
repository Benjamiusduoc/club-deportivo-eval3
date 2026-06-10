package com.club.ms_notificaciones.service;

import com.club.ms_notificaciones.client.SocioClient;
import com.club.ms_notificaciones.exception.RecursoNoEncontradoException;
import com.club.ms_notificaciones.model.dto.NotificacionRequestDTO;
import com.club.ms_notificaciones.model.dto.NotificacionResponseDTO;
import com.club.ms_notificaciones.model.entity.Notificacion;
import com.club.ms_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private SocioClient socioClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private NotificacionRequestDTO crearDtoValido() {
        NotificacionRequestDTO dto = new NotificacionRequestDTO();
        dto.setIdSocio(1L);
        dto.setTipo("EMAIL");
        dto.setAsunto("Bienvenido");
        dto.setMensaje("Bienvenido al club deportivo");
        return dto;
    }

    private Notificacion crearNotificacion() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setIdSocio(1L);
        n.setTipo("EMAIL");
        n.setAsunto("Bienvenido");
        n.setMensaje("Bienvenido al club deportivo");
        n.setLeido(false);
        n.setFechaCreacion(LocalDateTime.now());
        return n;
    }

    @Test
    @DisplayName("Given datos validos, when crear, then crea notificacion exitosamente")
    void crear_exitoso() {
        NotificacionRequestDTO dto = crearDtoValido();
        Notificacion notificacion = crearNotificacion();
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        NotificacionResponseDTO resultado = notificacionService.crear(dto);

        assertNotNull(resultado);
        assertEquals("Bienvenido", resultado.getAsunto());
        verify(socioClient).validarSocioExiste(1L);
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(captor.capture());
        assertFalse(captor.getValue().getLeido());
        assertNotNull(captor.getValue().getFechaCreacion());
    }

    @Test
    @DisplayName("Given socio no existe, when crear, then lanza excepcion")
    void crear_socioNoExiste() {
        NotificacionRequestDTO dto = crearDtoValido();
        doThrow(new RecursoNoEncontradoException("Socio no encontrado: 1"))
                .when(socioClient).validarSocioExiste(1L);

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> notificacionService.crear(dto));
        assertEquals("Socio no encontrado: 1", ex.getMessage());
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given socio con notificaciones, when listarPorSocio, then retorna lista")
    void listarPorSocio_retornaLista() {
        Notificacion notificacion = crearNotificacion();
        when(notificacionRepository.findByIdSocio(1L)).thenReturn(List.of(notificacion));

        List<NotificacionResponseDTO> resultado = notificacionService.listarPorSocio(1L);

        assertEquals(1, resultado.size());
        assertEquals("Bienvenido", resultado.get(0).getAsunto());
    }
}
