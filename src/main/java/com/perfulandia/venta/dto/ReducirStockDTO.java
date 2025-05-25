package com.perfulandia.venta.dto;

import lombok.Data;

@Data
public class ReducirStockDTO {
    private Long productoId;
    private Integer cantidad;
}
