package com.club.ms_inventario.controller;

import com.club.ms_inventario.model.dto.ItemRequestDTO;
import com.club.ms_inventario.model.dto.ItemResponseDTO;
import com.club.ms_inventario.service.ItemService;
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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones CRUD y consultas de implementos deportivos")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(
            summary = "Crear item",
            description = "Registra un nuevo implemento deportivo en el inventario. "
                    + "Inicializa la cantidad disponible con el stock total recibido en la petición."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<ItemResponseDTO> crear(@Valid @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO nuevo = itemService.crear(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar todos los items",
            description = "Obtiene el inventario completo de implementos registrados, "
                    + "incluyendo stock total, cantidad disponible y estado de cada item."
    )
    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> listarTodos() {
        List<ItemResponseDTO> lista = itemService.listarTodos();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener item por ID",
            description = "Consulta un implemento específico mediante su identificador único. "
                    + "Devuelve los datos de stock y estado asociados al recurso solicitado."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarPorId(@PathVariable Long id) {
        ItemResponseDTO item = itemService.buscarPorId(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @Operation(
            summary = "Listar items por actividad",
            description = "Filtra el inventario por actividad deportiva. "
                    + "Retorna los implementos vinculados al ID de actividad indicado en la ruta."
    )
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<List<ItemResponseDTO>> listarPorActividad(@PathVariable Long idActividad) {
        List<ItemResponseDTO> lista = itemService.listarPorActividad(idActividad);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
            summary = "Listar items por estado",
            description = "Consulta implementos según su estado operativo (por ejemplo, DISPONIBLE o BAJA). "
                    + "Útil para auditoría y control del inventario activo."
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ItemResponseDTO>> listarPorEstado(@PathVariable String estado) {
        List<ItemResponseDTO> lista = itemService.listarPorEstado(estado);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar item",
            description = "Modifica los datos de un implemento existente, incluido el stock total. "
                    + "Valida el DTO de entrada y persiste los cambios si el item se encuentra registrado."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO actualizado = itemService.actualizar(id, dto);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @Operation(
            summary = "Eliminar item",
            description = "Ejecuta una baja lógica del implemento: no elimina el registro de la base de datos, "
                    + "sino que actualiza su estado a BAJA para retirarlo del inventario operativo."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> eliminar(@PathVariable Long id) {
        ItemResponseDTO eliminado = itemService.eliminar(id);
        return new ResponseEntity<>(eliminado, HttpStatus.OK);
    }
}
