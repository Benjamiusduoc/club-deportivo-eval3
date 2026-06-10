package com.club.ms_configuracion.service;

import com.club.ms_configuracion.exception.RecursoNoEncontradoException;
import com.club.ms_configuracion.exception.ReglaNegocioException;
import com.club.ms_configuracion.model.dto.ConfiguracionRequestDTO;
import com.club.ms_configuracion.model.dto.ConfiguracionResponseDTO;
import com.club.ms_configuracion.model.entity.Configuracion;
import com.club.ms_configuracion.repository.ConfiguracionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguracionServiceTest {

    @Mock
    private ConfiguracionRepository configuracionRepository;

    @InjectMocks
    private ConfiguracionService configuracionService;

    private ConfiguracionRequestDTO crearDtoValido() {
        ConfiguracionRequestDTO dto = new ConfiguracionRequestDTO();
        dto.setClave("MAX_SOCIOS");
        dto.setValor("500");
        dto.setDescripcion("Maximo de socios permitidos");
        return dto;
    }

    private Configuracion crearConfiguracionExistente() {
        Configuracion configuracion = new Configuracion();
        configuracion.setId(1L);
        configuracion.setClave("MAX_SOCIOS");
        configuracion.setValor("500");
        configuracion.setDescripcion("Maximo de socios permitidos");
        return configuracion;
    }

    @Test
    @DisplayName("Given datos validos, when crear, then crea configuracion exitosamente")
    void crear_exitoso() {
        ConfiguracionRequestDTO dto = crearDtoValido();
        when(configuracionRepository.findByClave(dto.getClave())).thenReturn(Optional.empty());
        when(configuracionRepository.save(any(Configuracion.class))).thenAnswer(invocation -> {
            Configuracion c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        ConfiguracionResponseDTO resultado = configuracionService.crear(dto);

        assertNotNull(resultado.getId());
        assertEquals("MAX_SOCIOS", resultado.getClave());
        assertEquals("500", resultado.getValor());

        ArgumentCaptor<Configuracion> captor = ArgumentCaptor.forClass(Configuracion.class);
        verify(configuracionRepository).save(captor.capture());
        assertEquals("Maximo de socios permitidos", captor.getValue().getDescripcion());
    }

    @Test
    @DisplayName("Given clave duplicada, when crear, then lanza ReglaNegocioException")
    void crear_claveDuplicada() {
        ConfiguracionRequestDTO dto = crearDtoValido();
        when(configuracionRepository.findByClave(dto.getClave())).thenReturn(Optional.of(crearConfiguracionExistente()));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> configuracionService.crear(dto));
        assertTrue(ex.getMessage().contains("ya existe"));
        verify(configuracionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(configuracionRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class, () -> configuracionService.buscarPorId(99L));
        assertEquals("Configuracion no encontrada con el ID proporcionado.", ex.getMessage());
    }
}
