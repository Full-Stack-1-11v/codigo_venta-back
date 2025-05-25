package com.perfulandia.venta.dto;

import lombok.Data;

@Data
public class SucursalDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private boolean activa;
}
