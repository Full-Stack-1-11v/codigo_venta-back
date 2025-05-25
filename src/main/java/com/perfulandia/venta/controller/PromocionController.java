package com.perfulandia.venta.controller;

import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.service.PromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @PostMapping
    public ResponseEntity<Promocion> crearPromocion(@RequestBody Promocion promocion) {
        Promocion creada = promocionService.crearPromocion(promocion);
        return ResponseEntity.ok(creada);
    }

    @GetMapping
    public ResponseEntity<List<Promocion>> listarPromociones() {
        return ResponseEntity.ok(promocionService.listarPromociones());
    }
}