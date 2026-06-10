package com.club.ms_instructores.controller;

import com.club.ms_instructores.model.dto.InstructorRequestDTO;
import com.club.ms_instructores.model.dto.InstructorResponseDTO;
import com.club.ms_instructores.service.InstructorService;
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
@RequestMapping("/api/instructores")
@Tag(name = "Instructores", description = "Operaciones CRUD y consultas de instructores del club")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Crear instructor", description = "Crea un nuevo instructor con RUT y email unicos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Instructor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "RUT o email duplicado")
    })
    @PostMapping
    public ResponseEntity<InstructorResponseDTO> crearInstructor(@Valid @RequestBody InstructorRequestDTO dto) {
        InstructorResponseDTO nuevo = instructorService.crearInstructor(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los instructores")
    @GetMapping
    public ResponseEntity<List<InstructorResponseDTO>> listarTodos() {
        List<InstructorResponseDTO> lista = instructorService.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Listar instructores activos")
    @GetMapping("/activos")
    public ResponseEntity<List<InstructorResponseDTO>> listarActivos() {
        List<InstructorResponseDTO> lista = instructorService.listarActivos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(summary = "Obtener instructor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponseDTO> buscarPorId(@PathVariable Long id) {
        InstructorResponseDTO instructor = instructorService.buscarPorId(id);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar instructor")
    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InstructorRequestDTO dto) {
        InstructorResponseDTO actualizado = instructorService.actualizar(id, dto);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @Operation(summary = "Desactivar instructor", description = "Soft delete: marca activo=false")
    @DeleteMapping("/{id}")
    public ResponseEntity<InstructorResponseDTO> desactivar(@PathVariable Long id) {
        InstructorResponseDTO desactivado = instructorService.desactivar(id);
        return new ResponseEntity<>(desactivado, HttpStatus.OK);
    }
}
