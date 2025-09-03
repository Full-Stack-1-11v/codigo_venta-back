package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.PromocionModelAssembler;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.service.PromocionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@Tag(name = "promociones", description = "Operaciones relacionadas con promociones")
@RestController
@RequestMapping("/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;
    private final PromocionModelAssembler promocionModelAssembler;

    @Operation(
        summary = "Obtener todas las promociones",
        description = "Retorna una lista de todas las promociones registradas"
    )
    @GetMapping
    public CollectionModel<EntityModel<Promocion>> getTodasLasPromociones() {
        log.info("Solicitando todas las promociones...");
        List<Promocion> promociones = promocionService.obtenerTodas();
        List<EntityModel<Promocion>> modelos = promociones.stream()
                .map(promocionModelAssembler::toModel)
                .collect(Collectors.toList());

        log.info("Se encontraron {} promociones", modelos.size());
        return CollectionModel.of(modelos,
                linkTo(methodOn(PromocionController.class).getTodasLasPromociones()).withSelfRel());
    }

    @Operation(
        summary = "Obtener una promoción por ID",
        description = "Busca una promoción específica por su identificador.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Promoción encontrada"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Promocion>> getPromocionPorId(@PathVariable Long id) {
        log.info("Buscando promoción con ID {}", id);
        Promocion promocion = promocionService.obtenerPorId(id)
                .orElseThrow(() -> {
                    log.error("Promoción con ID {} no encontrada", id);
                    return new RuntimeException("Promoción no encontrada");
                });

        log.info("Promoción con ID {} encontrada", id);
        return ResponseEntity.ok(promocionModelAssembler.toModel(promocion));
    }

    @Operation(
        summary = "Crear una promoción",
        description = "Crea una nueva promoción con los datos proporcionados.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Promoción creada exitosamente")
        }
    )
    @PostMapping
    public ResponseEntity<EntityModel<Promocion>> crearPromocion(@RequestBody Promocion promocion) {
        log.info("Creando nueva promoción con código '{}'", promocion.getCodigo());
        Promocion creada = promocionService.crear(promocion);
        log.info("Promoción creada con ID {}", creada.getId());
        return ResponseEntity
                .created(linkTo(methodOn(PromocionController.class).getPromocionPorId(creada.getId())).toUri())
                .body(promocionModelAssembler.toModel(creada));
    }

    @Operation(
        summary = "Eliminar una promoción",
        description = "Elimina una promoción por su ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Promoción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        log.warn("Eliminando promoción con ID {}", id);
        promocionService.eliminar(id);
        log.info("Promoción con ID {} eliminada", id);
        return ResponseEntity.noContent().build();
    }
}
