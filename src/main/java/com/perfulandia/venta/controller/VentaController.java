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

    @PostMapping
    public ResponseEntity<Venta> crearVenta(@RequestBody Map<String, Object> payload) {
        Venta venta = new Venta();
        venta.setCliente((String) payload.get("cliente"));
        venta.setTotal(payload.get("total") != null ? Double.valueOf(payload.get("total").toString()) : 0.0);

        // ✅ Asignar sucursalId si está presente en el payload
        if (payload.get("sucursalId") != null) {
            venta.setSucursalId(Long.valueOf(payload.get("sucursalId").toString()));
        }

        // ✅ Convertir lista de promociones de forma segura
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
}