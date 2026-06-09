package com.club.ms_socios.service;

import com.club.ms_socios.model.dto.EmailUpdateDTO;
import com.club.ms_socios.model.dto.SocioRequestDTO;
import com.club.ms_socios.model.entity.Socio;
import com.club.ms_socios.repository.SocioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @InjectMocks
    private SocioService socioService;

    private SocioRequestDTO crearDtoValido() {
        SocioRequestDTO dto = new SocioRequestDTO();
        dto.setNombre("Ana Perez");
        dto.setRut("12345678-9");
        dto.setEmail("ana@ejemplo.cl");
        return dto;
    }

    private Socio crearSocioExistente() {
        Socio socio = new Socio();
        socio.setId(1L);
        socio.setNombre("Ana Perez");
        socio.setRut("12345678-9");
        socio.setEmail("ana@ejemplo.cl");
        socio.setFechaInscripcion(LocalDate.of(2025, 1, 15));
        socio.setActivo(true);
        return socio;
    }

    @Test
    @DisplayName("Given datos validos, when registrarSocio, then crea socio activo con fecha de hoy")
    void registrarSocio_exitoso() {
        // Given
        SocioRequestDTO dto = crearDtoValido();
        when(socioRepository.findByRut(dto.getRut())).thenReturn(Optional.empty());
        when(socioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(socioRepository.save(any(Socio.class))).thenAnswer(invocation -> {
            Socio s = invocation.getArgument(0);
            s.setId(1L);
            return s;
        });

        // When
        Socio resultado = socioService.registrarSocio(dto);

        // Then
        assertNotNull(resultado.getId());
        assertEquals("Ana Perez", resultado.getNombre());
        assertTrue(resultado.getActivo());
        assertEquals(LocalDate.now(), resultado.getFechaInscripcion());

        ArgumentCaptor<Socio> captor = ArgumentCaptor.forClass(Socio.class);
        verify(socioRepository).save(captor.capture());
        assertEquals("12345678-9", captor.getValue().getRut());
    }

    @Test
    @DisplayName("Given RUT duplicado, when registrarSocio, then lanza excepcion")
    void registrarSocio_rutDuplicado() {
        // Given
        SocioRequestDTO dto = crearDtoValido();
        when(socioRepository.findByRut(dto.getRut())).thenReturn(Optional.of(crearSocioExistente()));

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> socioService.registrarSocio(dto));
        assertEquals("El RUT ingresado ya pertenece a un socio existente.", ex.getMessage());
        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given email duplicado, when registrarSocio, then lanza excepcion")
    void registrarSocio_emailDuplicado() {
        // Given
        SocioRequestDTO dto = crearDtoValido();
        when(socioRepository.findByRut(dto.getRut())).thenReturn(Optional.empty());
        when(socioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(crearSocioExistente()));

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> socioService.registrarSocio(dto));
        assertEquals("El correo ingresado ya pertenece a un socio existente.", ex.getMessage());
        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given socios en BD, when listarTodos, then retorna la lista completa")
    void listarTodos_retornaLista() {
        // Given
        List<Socio> socios = List.of(crearSocioExistente());
        when(socioRepository.findAll()).thenReturn(socios);

        // When
        List<Socio> resultado = socioService.listarTodos();

        // Then
        assertEquals(1, resultado.size());
        verify(socioRepository).findAll();
    }

    @Test
    @DisplayName("Given ID existente, when buscarPorId, then retorna el socio")
    void buscarPorId_encontrado() {
        // Given
        Socio socio = crearSocioExistente();
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));

        // When
        Socio resultado = socioService.buscarPorId(1L);

        // Then
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Given ID inexistente, when buscarPorId, then lanza excepcion")
    void buscarPorId_noEncontrado() {
        // Given
        when(socioRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> socioService.buscarPorId(99L));
        assertEquals("Socio no encontrado con el ID proporcionado.", ex.getMessage());
    }

    @Test
    @DisplayName("Given socio existente, when actualizarSocio, then persiste los nuevos datos")
    void actualizarSocio_exitoso() {
        // Given
        Socio socio = crearSocioExistente();
        SocioRequestDTO dto = crearDtoValido();
        dto.setNombre("Ana Perez Lopez");
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.save(socio)).thenReturn(socio);

        // When
        Socio resultado = socioService.actualizarSocio(1L, dto);

        // Then
        assertEquals("Ana Perez Lopez", resultado.getNombre());
        verify(socioRepository).save(socio);
    }

    @Test
    @DisplayName("Given socio activo, when desactivarSocio, then marca activo=false")
    void desactivarSocio_exitoso() {
        // Given
        Socio socio = crearSocioExistente();
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.save(socio)).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Socio resultado = socioService.desactivarSocio(1L);

        // Then
        assertFalse(resultado.getActivo());
    }

    @Test
    @DisplayName("Given RUT inexistente, when buscarPorRut, then lanza excepcion")
    void buscarPorRut_noEncontrado() {
        // Given
        when(socioRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> socioService.buscarPorRut("99999999-9"));
        assertEquals("Socio no encontrado con el RUT proporcionado.", ex.getMessage());
    }

    @Test
    @DisplayName("Given RUT existente, when buscarPorRut, then retorna el socio")
    void buscarPorRut_encontrado() {
        // Given
        Socio socio = crearSocioExistente();
        when(socioRepository.findByRut("12345678-9")).thenReturn(Optional.of(socio));

        // When
        Socio resultado = socioService.buscarPorRut("12345678-9");

        // Then
        assertEquals("12345678-9", resultado.getRut());
    }

    @Test
    @DisplayName("Given socio inactivo, when reactivarSocio, then marca activo=true")
    void reactivarSocio_exitoso() {
        // Given
        Socio socio = crearSocioExistente();
        socio.setActivo(false);
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.save(socio)).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Socio resultado = socioService.reactivarSocio(1L);

        // Then
        assertTrue(resultado.getActivo());
    }

    @Test
    @DisplayName("Given socios activos, when listarActivos, then retorna solo activos")
    void listarActivos_retornaLista() {
        // Given
        when(socioRepository.findByActivoTrue()).thenReturn(List.of(crearSocioExistente()));

        // When
        List<Socio> resultado = socioService.listarActivos();

        // Then
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    @DisplayName("Given email disponible, when actualizarEmail, then actualiza el correo")
    void actualizarEmail_exitoso() {
        // Given
        Socio socio = crearSocioExistente();
        EmailUpdateDTO dto = new EmailUpdateDTO();
        dto.setEmail("nuevo@ejemplo.cl");
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.findByEmail("nuevo@ejemplo.cl")).thenReturn(Optional.empty());
        when(socioRepository.save(socio)).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Socio resultado = socioService.actualizarEmail(1L, dto);

        // Then
        assertEquals("nuevo@ejemplo.cl", resultado.getEmail());
    }

    @Test
    @DisplayName("Given email de otro socio, when actualizarEmail, then lanza excepcion")
    void actualizarEmail_duplicado() {
        // Given
        Socio socio = crearSocioExistente();
        Socio otro = crearSocioExistente();
        otro.setId(2L);
        EmailUpdateDTO dto = new EmailUpdateDTO();
        dto.setEmail("otro@ejemplo.cl");
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.findByEmail("otro@ejemplo.cl")).thenReturn(Optional.of(otro));

        // When / Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> socioService.actualizarEmail(1L, dto));
        assertEquals("El correo ingresado ya pertenece a un socio existente.", ex.getMessage());
    }

    @Test
    @DisplayName("Given socios activos en BD, when contarSociosActivos, then retorna el total")
    void contarSociosActivos_retornaTotal() {
        // Given
        when(socioRepository.countByActivoTrue()).thenReturn(5L);

        // When
        long total = socioService.contarSociosActivos();

        // Then
        assertEquals(5L, total);
    }
}
