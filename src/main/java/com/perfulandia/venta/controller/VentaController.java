package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.VentaModelAssembler;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.service.VentaService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaModelAssembler assembler;

    public VentaController(VentaService ventaService, VentaModelAssembler assembler) {
        this.ventaService = ventaService;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<Venta>> crearVenta(@RequestBody Map<String, Object> payload) {
        Venta venta = new Venta();

        if (payload.get("clienteId") != null) {
            venta.setClienteId(Long.valueOf(payload.get("clienteId").toString()));
        } else {
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

        Venta ventaCreada = ventaService.crearVenta(venta, codigosPromocion);
        return ResponseEntity.ok(assembler.toModel(ventaCreada));
    }

    @GetMapping("/{id}")
    public EntityModel<Venta> getVentaPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        return assembler.toModel(venta);
    }

    @GetMapping
    public CollectionModel<EntityModel<Venta>> getTodasLasVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();

        List<EntityModel<Venta>> recursos = ventas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                recursos,
                linkTo(methodOn(VentaController.class).getTodasLasVentas()).withSelfRel(),
                linkTo(methodOn(VentaController.class).crearVenta(null)).withRel("crear")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        boolean eliminado = ventaService.eliminarVenta(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
