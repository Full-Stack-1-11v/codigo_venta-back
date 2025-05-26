package com.perfulandia.venta.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private boolean activo; // ✅ Este campo es necesario para validar si el usuario está activo
    private List<RolDTO> roles;
}
