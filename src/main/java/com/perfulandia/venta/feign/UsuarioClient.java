package com.perfulandia.venta.feign;

import com.perfulandia.venta.dto.UsuarioDTO;
import com.perfulandia.venta.dto.RolDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "usuario-service", url = "https://codigo-usuario-back-1.onrender.com")
public interface UsuarioClient {

    @GetMapping("/usuarios/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);

    @GetMapping("/usuarios/{id}/roles")
    List<RolDTO> obtenerRolesPorUsuario(@PathVariable("id") Long id);

}

