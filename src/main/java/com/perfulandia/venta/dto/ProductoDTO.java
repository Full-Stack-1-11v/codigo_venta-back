package com.perfulandia.venta.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long productoId;
    private String nombreProducto;
    private String descripcion;
    private String marca;
    private boolean activo;
    private Integer stockMinimo;
    private int stockActual;
    private double precioProducto;
    private long sucursalId;
}