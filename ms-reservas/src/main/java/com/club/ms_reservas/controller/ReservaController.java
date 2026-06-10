package com.club.ms_reservas.controller;

import com.club.ms_reservas.model.dto.ReservaRequestDTO;
import com.club.ms_reservas.model.dto.ReservaResponseDTO;
import com.club.ms_reservas.service.ReservaService;
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
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Operaciones de reservas de actividades del club")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(summary = "Crear reserva", description = "Crea una nueva reserva validando socio activo y actividad existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Socio o actividad no encontrados"),
            @ApiResponse(responseCode = "409", description = "Regla de negocio violada")
    })
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO nueva = reservaService.crearReserva(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        List<ReservaResponseDTO> lista = reservaService.listarTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Obtener reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        ReservaResponseDTO reserva = reservaService.buscarPorId(id);
        return new ResponseEntity<>(reserva, HttpStatus.OK);
    }

    @Operation(summary = "Listar reservas por socio")
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorSocio(@PathVariable Long idSocio) {
        List<ReservaResponseDTO> lista = reservaService.listarPorSocio(idSocio);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar reservas por fecha")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorFecha(@PathVariable String fecha) {
        List<ReservaResponseDTO> lista = reservaService.listarPorFecha(LocalDate.parse(fecha));
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Cancelar reserva", description = "Cambia el estado de la reserva a CANCELADA")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        ReservaResponseDTO cancelada = reservaService.cancelarReserva(id);
        return new ResponseEntity<>(cancelada, HttpStatus.OK);
    }
}
