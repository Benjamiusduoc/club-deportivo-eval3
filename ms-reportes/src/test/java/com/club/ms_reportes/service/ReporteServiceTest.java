package com.club.ms_reportes.service;

import com.club.ms_reportes.client.CuotaClient;
import com.club.ms_reportes.client.ReservaClient;
import com.club.ms_reportes.client.SocioClient;
import com.club.ms_reportes.model.dto.ReporteResponseDTO;
import com.club.ms_reportes.model.entity.Reporte;
import com.club.ms_reportes.repository.ReporteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private SocioClient socioClient;

    @Mock
    private CuotaClient cuotaClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    @DisplayName("Given tipo SOCIOS, when generarReporte, then consulta socios activos y guarda")
    void generarReporte_SOCIOS() {
        when(socioClient.contarSociosActivos()).thenReturn(10L);
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo("SOCIOS");
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setDatos("{\"sociosActivos\": 10}");
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        ReporteResponseDTO resultado = reporteService.generarReporte("SOCIOS");

        assertNotNull(resultado);
        assertEquals("SOCIOS", resultado.getTipo());
        assertTrue(resultado.getDatos().contains("sociosActivos"));
        verify(socioClient).contarSociosActivos();
        ArgumentCaptor<Reporte> captor = ArgumentCaptor.forClass(Reporte.class);
        verify(reporteRepository).save(captor.capture());
        assertEquals("SOCIOS", captor.getValue().getTipo());
    }

    @Test
    @DisplayName("Given tipo CUOTAS, when generarReporte, then consulta cuotas y guarda")
    void generarReporte_CUOTAS() {
        when(cuotaClient.contarCuotasPendientes()).thenReturn(5L);
        when(cuotaClient.contarCuotasVencidas()).thenReturn(3L);
        Reporte reporte = new Reporte();
        reporte.setId(2L);
        reporte.setTipo("CUOTAS");
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setDatos("{\"cuotasPendientes\": 5, \"cuotasVencidas\": 3}");
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        ReporteResponseDTO resultado = reporteService.generarReporte("CUOTAS");

        assertNotNull(resultado);
        assertEquals("CUOTAS", resultado.getTipo());
        assertTrue(resultado.getDatos().contains("cuotasPendientes"));
        verify(cuotaClient).contarCuotasPendientes();
        verify(cuotaClient).contarCuotasVencidas();
    }
}
