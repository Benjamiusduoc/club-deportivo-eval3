package com.club.ms_reportes.controller;

import com.club.ms_reportes.model.dto.ReporteResponseDTO;
import com.club.ms_reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Operaciones de reportes y estadisticas")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Generar reporte por tipo")
    @PostMapping("/generar/{tipo}")
    public ResponseEntity<ReporteResponseDTO> generarReporte(@PathVariable String tipo) {
        ReporteResponseDTO response = reporteService.generarReporte(tipo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar historial de reportes")
    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listarHistorial() {
        List<ReporteResponseDTO> lista = reporteService.listarHistorial();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> buscarPorId(@PathVariable Long id) {
        ReporteResponseDTO response = reporteService.buscarPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
