package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.FacturaModelAssembler;
import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.service.FacturaService;
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

@Slf4j // 👈 Aquí se activa el logger
@Tag(name = "facturas", description = "Operaciones relacionadas con las facturas")
@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaModelAssembler assembler;

    public FacturaController(FacturaService facturaService, FacturaModelAssembler assembler) {
        this.facturaService = facturaService;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener una factura por ID", responses = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public EntityModel<Factura> getFacturaPorId(@PathVariable Long id) {
        log.info("Buscando factura con ID {}", id); // 🔍
        Factura factura = facturaService.obtenerFacturaPorId(id);
        return assembler.toModel(factura);
    }

    @Operation(summary = "Obtener todas las facturas")
    @GetMapping
    public CollectionModel<EntityModel<Factura>> getTodasLasFacturas() {
        log.info("Obteniendo todas las facturas");
        List<Factura> facturas = facturaService.obtenerTodasLasFacturas();

        List<EntityModel<Factura>> recursos = facturas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                recursos,
                linkTo(methodOn(FacturaController.class).getTodasLasFacturas()).withSelfRel(),
                linkTo(methodOn(FacturaController.class).crearFactura(null)).withRel("crear")
        );
    }

    @Operation(summary = "Crear una nueva factura")
    @PostMapping
    public ResponseEntity<EntityModel<Factura>> crearFactura(@RequestBody Factura factura) {
        log.info("Creando factura con datos: {}", factura); // 🧾
        Factura nueva = facturaService.emitirFactura(factura);
        return ResponseEntity.ok(assembler.toModel(nueva));
    }

    @Operation(summary = "Eliminar una factura", responses = {
            @ApiResponse(responseCode = "204", description = "Factura eliminada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFactura(@PathVariable Long id) {
        log.warn("Intentando eliminar factura con ID {}", id); // 🗑️
        boolean eliminado = facturaService.eliminarFactura(id);
        if (eliminado) {
            log.info("Factura con ID {} eliminada correctamente", id);
            return ResponseEntity.noContent().build();
        } else {
            log.error("No se encontró factura con ID {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
