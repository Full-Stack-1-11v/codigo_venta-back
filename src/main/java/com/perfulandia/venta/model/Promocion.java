package com.perfulandia.venta.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private String tipo; // PORCENTAJE, MONTO_FIJO

    private Double valor;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private boolean activa;

    @ManyToMany(mappedBy = "promocionesAplicadas")
    private List<Venta> ventas;
}
