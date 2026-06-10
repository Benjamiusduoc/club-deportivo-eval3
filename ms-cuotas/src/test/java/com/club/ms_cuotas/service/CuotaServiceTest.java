package com.club.ms_cuotas.service;

import com.club.ms_cuotas.client.SocioClient;
import com.club.ms_cuotas.exception.RecursoNoEncontradoException;
import com.club.ms_cuotas.exception.ReglaNegocioException;
import com.club.ms_cuotas.model.dto.CuotaRequestDTO;
import com.club.ms_cuotas.model.dto.CuotaResponseDTO;
import com.club.ms_cuotas.model.dto.PagoRequestDTO;
import com.club.ms_cuotas.model.entity.Cuota;
import com.club.ms_cuotas.repository.CuotaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuotaServiceTest {

    @Mock
    private CuotaRepository cuotaRepository;

    @Mock
    private SocioClient socioClient;

    @InjectMocks
    private CuotaService cuotaService;

    private CuotaRequestDTO crearDtoValido() {
        CuotaRequestDTO dto = new CuotaRequestDTO();
        dto.setIdSocio(1L);
        dto.setMonto(BigDecimal.valueOf(30000));
        dto.setPeriodo("2025-01");
        dto.setFechaVencimiento(LocalDate.of(2025, 1, 31));
        return dto;
    }

    private Cuota crearCuotaPendiente() {
        Cuota cuota = new Cuota();
        cuota.setId(1L);
        cuota.setIdSocio(1L);
        cuota.setMonto(BigDecimal.valueOf(30000));
        cuota.setPeriodo("2025-01");
        cuota.setFechaEmision(LocalDate.of(2025, 1, 1));
        cuota.setFechaVencimiento(LocalDate.of(2025, 1, 31));
        cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);
        return cuota;
    }

    @Test
    @DisplayName("Given datos validos, when crearCuota, then retorna CuotaResponseDTO con estado PENDIENTE")
    void crearCuota_exitoso() {
        CuotaRequestDTO dto = crearDtoValido();
        doNothing().when(socioClient).validarSocioActivo(1L);
        when(cuotaRepository.findByIdSocioAndPeriodo(1L, "2025-01")).thenReturn(Optional.empty());
        when(cuotaRepository.save(any(Cuota.class))).thenAnswer(inv -> {
            Cuota c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        CuotaResponseDTO resultado = cuotaService.crearCuota(dto);

        assertNotNull(resultado.getId());
        assertEquals(1L, resultado.getIdSocio());
        assertEquals(BigDecimal.valueOf(30000), resultado.getMonto());
        assertEquals("PENDIENTE", resultado.getEstado());

        ArgumentCaptor<Cuota> captor = ArgumentCaptor.forClass(Cuota.class);
        verify(cuotaRepository).save(captor.capture());
        assertEquals("2025-01", captor.getValue().getPeriodo());
    }

    @Test
    @DisplayName("Given socio ya tiene cuota en el periodo, when crearCuota, then lanza ReglaNegocioException")
    void crearCuota_periodoDuplicado() {
        CuotaRequestDTO dto = crearDtoValido();
        doNothing().when(socioClient).validarSocioActivo(1L);
        when(cuotaRepository.findByIdSocioAndPeriodo(1L, "2025-01"))
                .thenReturn(Optional.of(crearCuotaPendiente()));

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> cuotaService.crearCuota(dto));
        assertTrue(ex.getMessage().contains("ya tiene una cuota"));
        verify(cuotaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given cuota existe, when pagarCuota, then cambia estado a PAGADA")
    void pagarCuota_exitoso() {
        Cuota cuota = crearCuotaPendiente();
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));
        when(cuotaRepository.save(any(Cuota.class))).thenAnswer(inv -> inv.getArgument(0));

        PagoRequestDTO pagoDto = new PagoRequestDTO();
        pagoDto.setMetodoPago(Cuota.MetodoPago.TRANSFERENCIA);

        CuotaResponseDTO resultado = cuotaService.pagarCuota(1L, pagoDto);

        assertEquals("PAGADA", resultado.getEstado());
        assertEquals("TRANSFERENCIA", resultado.getMetodoPago());
        assertEquals(LocalDate.now(), resultado.getFechaPago());
    }

    @Test
    @DisplayName("Given cuota ya pagada, when pagarCuota, then lanza ReglaNegocioException")
    void pagarCuota_yaPagada() {
        Cuota cuota = crearCuotaPendiente();
        cuota.setEstado(Cuota.EstadoCuota.PAGADA);
        when(cuotaRepository.findById(1L)).thenReturn(Optional.of(cuota));

        PagoRequestDTO pagoDto = new PagoRequestDTO();
        pagoDto.setMetodoPago(Cuota.MetodoPago.EFECTIVO);

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> cuotaService.pagarCuota(1L, pagoDto));
        assertTrue(ex.getMessage().contains("ya se encuentra pagada"));
        verify(cuotaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Given cuota no existe, when buscarPorId, then lanza RecursoNoEncontradoException")
    void buscarPorId_noEncontrado() {
        when(cuotaRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> cuotaService.buscarPorId(99L));
        assertTrue(ex.getMessage().contains("no encontrada"));
    }

    @Test
    @DisplayName("Given cuotas en BD, when listarTodas, then retorna lista")
    void listarTodas_retornaLista() {
        when(cuotaRepository.findAll()).thenReturn(List.of(crearCuotaPendiente()));

        List<CuotaResponseDTO> resultado = cuotaService.listarTodas();

        assertEquals(1, resultado.size());
        verify(cuotaRepository).findAll();
    }

    @Test
    @DisplayName("Given cuotas de un socio, when listarPorSocio, then retorna filtradas")
    void listarPorSocio_retornaFiltradas() {
        when(cuotaRepository.findByIdSocio(1L)).thenReturn(List.of(crearCuotaPendiente()));

        List<CuotaResponseDTO> resultado = cuotaService.listarPorSocio(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdSocio());
    }

    @Test
    @DisplayName("Given cuotas vencidas, when actualizarCuotasVencidas, then marca como VENCIDA")
    void actualizarCuotasVencidas_marcaVencidas() {
        Cuota vencida = crearCuotaPendiente();
        vencida.setFechaVencimiento(LocalDate.now().minusDays(1));
        when(cuotaRepository.findAll()).thenReturn(List.of(vencida));

        cuotaService.actualizarCuotasVencidas();

        verify(cuotaRepository).saveAll(anyList());
    }
}
