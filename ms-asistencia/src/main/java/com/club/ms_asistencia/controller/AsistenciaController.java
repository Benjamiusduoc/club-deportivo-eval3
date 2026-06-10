package com.club.ms_asistencia.controller;

import com.club.ms_asistencia.model.dto.AsistenciaRequestDTO;
import com.club.ms_asistencia.model.dto.AsistenciaResponseDTO;
import com.club.ms_asistencia.service.AsistenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
@Tag(name = "Asistencia", description = "Operaciones de asistencia del club")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @Operation(summary = "Registrar asistencia", description = "Registra una nueva asistencia validando reserva y socio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asistencia registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Reserva o socio no encontrados")
    })
    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crearAsistencia(@Valid @RequestBody AsistenciaRequestDTO dto) {
        AsistenciaResponseDTO nueva = asistenciaService.crearAsistencia(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las asistencias")
    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> listarTodas() {
        List<AsistenciaResponseDTO> lista = asistenciaService.listarTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Obtener asistencia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        AsistenciaResponseDTO asistencia = asistenciaService.buscarPorId(id);
        return new ResponseEntity<>(asistencia, HttpStatus.OK);
    }

    @Operation(summary = "Listar asistencias por reserva")
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorReserva(@PathVariable Long idReserva) {
        List<AsistenciaResponseDTO> lista = asistenciaService.listarPorReserva(idReserva);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar asistencias por socio")
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorSocio(@PathVariable Long idSocio) {
        List<AsistenciaResponseDTO> lista = asistenciaService.listarPorSocio(idSocio);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar asistencias por fecha")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorFecha(@PathVariable String fecha) {
        List<AsistenciaResponseDTO> lista = asistenciaService.listarPorFecha(LocalDate.parse(fecha));
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
}
