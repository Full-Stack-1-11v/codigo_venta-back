package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.FacturaModelAssembler;
import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.service.FacturaService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaModelAssembler assembler;

    public FacturaController(FacturaService facturaService, FacturaModelAssembler assembler) {
        this.facturaService = facturaService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public EntityModel<Factura> getFacturaPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerFacturaPorId(id);
        return assembler.toModel(factura);
    }

    @GetMapping
    public CollectionModel<EntityModel<Factura>> getTodasLasFacturas() {
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

    @PostMapping
    public ResponseEntity<EntityModel<Factura>> crearFactura(@RequestBody Factura factura) {
        Factura nueva = facturaService.emitirFactura(factura);
        return ResponseEntity.ok(assembler.toModel(nueva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFactura(@PathVariable Long id) {
        boolean eliminado = facturaService.eliminarFactura(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


