package com.perfulandia.venta.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.perfulandia.venta.dto.SucursalDTO;

@FeignClient(name = "sucursal", url = "http://localhost:8082")
public interface SucursalClient {

    @GetMapping("/sucursales/{id}")
    SucursalDTO obtenerSucursalPorId(@PathVariable("id") Long id);
}