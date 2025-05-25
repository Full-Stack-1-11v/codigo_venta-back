package com.perfulandia.venta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.perfulandia.venta.dto.ProductoStockDTO;
import com.perfulandia.venta.dto.ReducirStockDTO;

@FeignClient(name = "inventario", url = "http://localhost:8081")
public interface InventarioClient {

    @GetMapping("/inventario/producto/{id}")
    ProductoStockDTO obtenerStock(@PathVariable("id") Long productoId);

    @PatchMapping("/inventario/reducir-stock")
    void reducirStock(@RequestBody ReducirStockDTO dto);
}