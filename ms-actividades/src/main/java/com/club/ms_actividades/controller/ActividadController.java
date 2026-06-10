package com.club.ms_actividades.controller;

import com.club.ms_actividades.model.dto.ActividadRequestDTO;
import com.club.ms_actividades.model.dto.ActividadResponseDTO;
import com.club.ms_actividades.service.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@Tag(name = "Actividades", description = "Operaciones CRUD y consultas de actividades del club")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @Operation(summary = "Crear actividad", description = "Crea una nueva actividad con nombre unico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actividad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Nombre duplicado")
    })
    @PostMapping
    public ResponseEntity<ActividadResponseDTO> crearActividad(@Valid @RequestBody ActividadRequestDTO dto) {
        ActividadResponseDTO nueva = actividadService.crearActividad(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las actividades")
    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> listarTodas() {
        List<ActividadResponseDTO> lista = actividadService.listarTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar actividades activas")
    @GetMapping("/activas")
    public ResponseEntity<List<ActividadResponseDTO>> listarActivas() {
        List<ActividadResponseDTO> lista = actividadService.listarActivas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Obtener actividad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> buscarPorId(@PathVariable Long id) {
        ActividadResponseDTO actividad = actividadService.buscarPorId(id);
        return new ResponseEntity<>(actividad, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar actividad")
    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ActividadRequestDTO dto) {
        ActividadResponseDTO actualizada = actividadService.actualizar(id, dto);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    @Operation(summary = "Desactivar actividad", description = "Soft delete: marca activo=false")
    @DeleteMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> desactivar(@PathVariable Long id) {
        ActividadResponseDTO desactivada = actividadService.desactivar(id);
        return new ResponseEntity<>(desactivada, HttpStatus.OK);
    }
}
