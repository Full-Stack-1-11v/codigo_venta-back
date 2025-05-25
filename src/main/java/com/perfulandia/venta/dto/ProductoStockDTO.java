package com.perfulandia.venta.dto;

import lombok.Data;

@Data
public class ProductoStockDTO {
    private Long productoId;
    private Integer stockDisponible;
}