package com.perfulandia.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalDTO {

        private long sucursalId;
    
    private String nombre;
    private String direccion;
    private boolean activa;

}
