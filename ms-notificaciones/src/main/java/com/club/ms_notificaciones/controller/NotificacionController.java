package com.club.ms_notificaciones.controller;

import com.club.ms_notificaciones.model.dto.NotificacionRequestDTO;
import com.club.ms_notificaciones.model.dto.NotificacionResponseDTO;
import com.club.ms_notificaciones.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Operaciones de notificaciones a socios")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Crear notificacion")
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crear(@Valid @RequestBody NotificacionRequestDTO dto) {
        NotificacionResponseDTO response = notificacionService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listarTodas() {
        List<NotificacionResponseDTO> lista = notificacionService.listarTodas();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Buscar notificacion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(@PathVariable Long id) {
        NotificacionResponseDTO response = notificacionService.buscarPorId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Listar notificaciones de un socio")
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<NotificacionResponseDTO>> listarPorSocio(@PathVariable Long idSocio) {
        List<NotificacionResponseDTO> lista = notificacionService.listarPorSocio(idSocio);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar notificaciones no leidas de un socio")
    @GetMapping("/socio/{idSocio}/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> listarNoLeidas(@PathVariable Long idSocio) {
        List<NotificacionResponseDTO> lista = notificacionService.listarNoLeidasPorSocio(idSocio);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Marcar notificacion como leida")
    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Long id) {
        NotificacionResponseDTO response = notificacionService.marcarComoLeida(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Contar notificaciones no leidas de un socio")
    @GetMapping("/socio/{idSocio}/contar")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long idSocio) {
        long total = notificacionService.contarNoLeidas(idSocio);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }
}
