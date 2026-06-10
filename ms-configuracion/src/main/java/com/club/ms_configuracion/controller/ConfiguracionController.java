package com.club.ms_configuracion.controller;

import com.club.ms_configuracion.model.dto.ConfiguracionRequestDTO;
import com.club.ms_configuracion.model.dto.ConfiguracionResponseDTO;
import com.club.ms_configuracion.service.ConfiguracionService;
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
@RequestMapping("/api/configuracion")
@Tag(name = "Configuracion", description = "Operaciones CRUD de parametros de configuracion del club")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @Operation(summary = "Crear configuracion", description = "Crea un nuevo parametro de configuracion con clave unica")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Configuracion creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Clave duplicada")
    })
    @PostMapping
    public ResponseEntity<ConfiguracionResponseDTO> crear(@Valid @RequestBody ConfiguracionRequestDTO dto) {
        ConfiguracionResponseDTO nueva = configuracionService.crear(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las configuraciones")
    @GetMapping
    public ResponseEntity<List<ConfiguracionResponseDTO>> listarTodas() {
        List<ConfiguracionResponseDTO> lista = configuracionService.listarTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Obtener configuracion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> buscarPorId(@PathVariable Long id) {
        ConfiguracionResponseDTO configuracion = configuracionService.buscarPorId(id);
        return new ResponseEntity<>(configuracion, HttpStatus.OK);
    }

    @Operation(summary = "Obtener configuracion por clave")
    @GetMapping("/clave/{clave}")
    public ResponseEntity<ConfiguracionResponseDTO> buscarPorClave(@PathVariable String clave) {
        ConfiguracionResponseDTO configuracion = configuracionService.buscarPorClave(clave);
        return new ResponseEntity<>(configuracion, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar configuracion")
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ConfiguracionRequestDTO dto) {
        ConfiguracionResponseDTO actualizada = configuracionService.actualizar(id, dto);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar configuracion", description = "Hard delete del parametro de configuracion")
    @DeleteMapping("/{id}")
    public ResponseEntity<ConfiguracionResponseDTO> eliminar(@PathVariable Long id) {
        ConfiguracionResponseDTO eliminada = configuracionService.eliminar(id);
        return new ResponseEntity<>(eliminada, HttpStatus.OK);
    }
}
