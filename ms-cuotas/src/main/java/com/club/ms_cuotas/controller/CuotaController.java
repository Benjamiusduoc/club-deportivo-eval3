package com.club.ms_cuotas.controller;

import com.club.ms_cuotas.model.dto.CuotaRequestDTO;
import com.club.ms_cuotas.model.dto.CuotaResponseDTO;
import com.club.ms_cuotas.model.dto.PagoRequestDTO;
import com.club.ms_cuotas.model.entity.Cuota;
import com.club.ms_cuotas.service.CuotaService;
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
@RequestMapping("/api/cuotas")
@Tag(name = "Cuotas", description = "Operaciones CRUD y gestion de cuotas del club")
public class CuotaController {

    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @Operation(summary = "Crear cuota", description = "Crea una cuota para un socio en un periodo especifico. Valida que el socio exista y este activo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuota creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El socio ya tiene una cuota en ese periodo")
    })
    @PostMapping
    public ResponseEntity<CuotaResponseDTO> crearCuota(@Valid @RequestBody CuotaRequestDTO dto) {
        CuotaResponseDTO cuota = cuotaService.crearCuota(dto);
        return new ResponseEntity<>(cuota, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las cuotas")
    @GetMapping
    public ResponseEntity<List<CuotaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(cuotaService.listarTodas());
    }

    @Operation(summary = "Obtener cuota por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CuotaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuotaService.buscarPorId(id));
    }

    @Operation(summary = "Listar cuotas de un socio")
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<CuotaResponseDTO>> listarPorSocio(@PathVariable Long idSocio) {
        return ResponseEntity.ok(cuotaService.listarPorSocio(idSocio));
    }

    @Operation(summary = "Listar cuotas por estado", description = "Filtrar cuotas por estado: PENDIENTE, PAGADA o VENCIDA")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CuotaResponseDTO>> listarPorEstado(@PathVariable String estado) {
        Cuota.EstadoCuota estadoEnum = Cuota.EstadoCuota.valueOf(estado.toUpperCase());
        return ResponseEntity.ok(cuotaService.listarPorEstado(estadoEnum));
    }

    @Operation(summary = "Pagar cuota", description = "Registra el pago de una cuota pendiente o vencida")
    @PostMapping("/{id}/pagar")
    public ResponseEntity<CuotaResponseDTO> pagarCuota(@PathVariable Long id, @Valid @RequestBody PagoRequestDTO dto) {
        CuotaResponseDTO cuota = cuotaService.pagarCuota(id, dto);
        return ResponseEntity.ok(cuota);
    }
}
