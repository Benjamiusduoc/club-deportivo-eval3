package com.club.ms_socios.controller;

import com.club.ms_socios.model.dto.EmailUpdateDTO;
import com.club.ms_socios.model.dto.SocioRequestDTO;
import com.club.ms_socios.model.entity.Socio;
import com.club.ms_socios.service.SocioService;
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
@RequestMapping("/api/socios")
@Tag(name = "Socios", description = "Operaciones CRUD y consultas de socios del club")
public class SocioController {

    private final SocioService socioService;

    // Inyección de dependencias
    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @Operation(summary = "Registrar socio", description = "Crea un nuevo socio con RUT y email unicos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Socio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o RUT/email duplicado")
    })
    @PostMapping
    public ResponseEntity<Socio> crearSocio(@Valid @RequestBody SocioRequestDTO dto) {
        Socio nuevoSocio = socioService.registrarSocio(dto);

        // Retornamos el socio creado con un código HTTP 201
        return new ResponseEntity<>(nuevoSocio, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los socios")
    @GetMapping
    public ResponseEntity<List<Socio>> listarSocios() {
        List<Socio> lista = socioService.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK); // 200 OK
    }

    @Operation(summary = "Listar socios activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Socio>> listarActivos() {
        List<Socio> lista = socioService.listarActivos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
    @Operation(summary = "Contar socios activos")
    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Long> contarActivos() {
        long total = socioService.contarSociosActivos();
        return new ResponseEntity<>(total, HttpStatus.OK);
    }
    @Operation(summary = "Buscar socio por RUT")
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Socio> obtenerPorRut(@PathVariable String rut) {
        Socio socio = socioService.buscarPorRut(rut);
        return new ResponseEntity<>(socio, HttpStatus.OK);
    }

    @Operation(summary = "Obtener socio por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerPorId(@PathVariable Long id) {
        Socio socio = socioService.buscarPorId(id);
        return new ResponseEntity<>(socio, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar datos del socio")
    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable Long id, @Valid @RequestBody SocioRequestDTO dto) {
        Socio socioActualizado = socioService.actualizarSocio(id, dto);
        return new ResponseEntity<>(socioActualizado, HttpStatus.OK);
    }


    @Operation(summary = "Reactivar socio desactivado")
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Socio> reactivar(@PathVariable Long id) {
        Socio socio = socioService.reactivarSocio(id);
        return new ResponseEntity<>(socio, HttpStatus.OK);
    }
    @Operation(summary = "Actualizar email del socio")
    @PatchMapping("/{id}/email")
    public ResponseEntity<Socio> actualizarEmail(@PathVariable Long id, @Valid @RequestBody EmailUpdateDTO dto) {
        Socio socio = socioService.actualizarEmail(id, dto);
        return new ResponseEntity<>(socio, HttpStatus.OK);
    }

    @Operation(summary = "Desactivar socio", description = "Soft delete: marca activo=false sin borrar el registro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Socio> eliminar(@PathVariable Long id) {
        Socio socioDesactivado = socioService.desactivarSocio(id);

        // Retornamos el socio con el nuevo estado y un código 200 OK
        return new ResponseEntity<>(socioDesactivado, HttpStatus.OK);
    }
}
