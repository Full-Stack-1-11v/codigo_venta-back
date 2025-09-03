package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.VentaModelAssembler;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@Tag(name = "ventas", description = "Operaciones relacionadas con las ventas")
@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaModelAssembler assembler;

    public VentaController(VentaService ventaService, VentaModelAssembler assembler) {
        this.ventaService = ventaService;
        this.assembler = assembler;
    }

    @Operation(
        summary = "Crear una venta",
        description = "Crea una nueva venta a partir del clienteId, total, sucursalId y promociones (códigos).",
        responses = {
            @ApiResponse(responseCode = "200", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
        }
    )
    @PostMapping
    public ResponseEntity<EntityModel<Venta>> crearVenta(@RequestBody Map<String, Object> payload) {
        log.info("Solicitud para crear una venta: {}", payload);

        Venta venta = new Venta();

        if (payload.get("clienteId") != null) {
            venta.setClienteId(Long.valueOf(payload.get("clienteId").toString()));
        } else {
            log.warn("clienteId es obligatorio y no fue proporcionado");
            throw new IllegalArgumentException("clienteId es obligatorio");
        }

        venta.setTotal(payload.get("total") != null ? Double.valueOf(payload.get("total").toString()) : 0.0);

        if (payload.get("sucursalId") != null) {
            venta.setSucursalId(Long.valueOf(payload.get("sucursalId").toString()));
        }

        List<String> codigosPromocion = List.of();
        Object promocionesObj = payload.get("promociones");
        if (promocionesObj instanceof List<?>) {
            codigosPromocion = ((List<?>) promocionesObj).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .collect(Collectors.toList());
        }

        log.info("Promociones aplicadas: {}", codigosPromocion);

        Venta ventaCreada = ventaService.crearVenta(venta, codigosPromocion);
        log.info("Venta creada con ID {}", ventaCreada.getId());
        return ResponseEntity.ok(assembler.toModel(ventaCreada));
    }

    @Operation(
        summary = "Obtener una venta por ID",
        description = "Busca una venta específica por su identificador.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
        }
    )
    @GetMapping("/{id}")
    public EntityModel<Venta> getVentaPorId(@PathVariable Long id) {
        log.info("Buscando venta con ID {}", id);
        Venta venta = ventaService.obtenerVentaPorId(id);
        log.info("Venta encontrada: {}", venta);
        return assembler.toModel(venta);
    }

    @Operation(
        summary = "Obtener todas las ventas",
        description = "Lista todas las ventas registradas en el sistema"
    )
    @GetMapping
    public CollectionModel<EntityModel<Venta>> getTodasLasVentas() {
        log.info("Solicitando todas las ventas...");
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();

        log.info("Total de ventas encontradas: {}", ventas.size());

        List<EntityModel<Venta>> recursos = ventas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                recursos,
                linkTo(methodOn(VentaController.class).getTodasLasVentas()).withSelfRel(),
                linkTo(methodOn(VentaController.class).crearVenta(null)).withRel("crear")
        );
    }

    @Operation(
        summary = "Eliminar una venta",
        description = "Elimina una venta específica por su ID.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Venta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        log.warn("Eliminando venta con ID {}", id);
        boolean eliminado = ventaService.eliminarVenta(id);
        if (eliminado) {
            log.info("Venta con ID {} eliminada", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("Venta con ID {} no encontrada para eliminar", id);
            return ResponseEntity.notFound().build();
        }
    }
}
