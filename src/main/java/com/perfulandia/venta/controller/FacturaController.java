package com.perfulandia.venta.controller;

import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping
    public ResponseEntity<Factura> emitirFactura(@RequestBody Factura factura) {
        Factura emitida = facturaService.emitirFactura(factura);
        return ResponseEntity.ok(emitida);
    }
}