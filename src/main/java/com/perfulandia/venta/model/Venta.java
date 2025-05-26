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
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private Double total;

    private String estado; // PENDIENTE, CONFIRMADO, CANCELADO

    private String cliente;

    private Long sucursalId; // ✅ Campo necesario para validar con Feign

    private Long clienteId;

    @ManyToMany
    @JoinTable(
        name = "venta_promocion",
        joinColumns = @JoinColumn(name = "venta_id"),
        inverseJoinColumns = @JoinColumn(name = "promocion_id")
    )
    private List<Promocion> promocionesAplicadas;
}