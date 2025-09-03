package com.perfulandia.venta.feign;

import com.perfulandia.venta.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventario-service", url = "https://codigo-inventario-back.onrender.com") // ajusta el puerto si es distinto
public interface InventarioClient {

    @GetMapping("/productos/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable("id") Long id);
}
