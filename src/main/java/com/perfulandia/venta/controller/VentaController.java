package com.perfulandia.venta.controller;

import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // POST /ventas
    @PostMapping
    public ResponseEntity<Venta> crearVenta(@RequestBody Map<String, Object> payload) {
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

        Object promocionesObj = payload.get("promociones");
        List<String> codigosPromocion;
        if (promocionesObj instanceof List<?>) {
            codigosPromocion = ((List<?>) promocionesObj).stream()
                    .filter(item -> item instanceof String)
                    .map(item -> (String) item)
                    .toList();
        } else {
            codigosPromocion = List.of();
        }

        Venta ventaCreada = ventaService.crearVenta(venta, codigosPromocion);
        return ResponseEntity.ok(ventaCreada);
    }

    // GET /ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    // GET /ventas/usuario/{id}
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Venta>> obtenerVentasPorCliente(@PathVariable Long id) {
        List<Venta> ventas = ventaService.obtenerVentasPorCliente(id);
        if (ventas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ventas);
    }

    // DELETE /ventas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        boolean eliminada = ventaService.eliminarVenta(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
