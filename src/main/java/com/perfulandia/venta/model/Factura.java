package com.perfulandia.venta.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroDocumento;

    private LocalDateTime fechaEmision;

    private String tipoDocumento; // FACTURA o BOLETA

    private Long ventaId;

    @Lob
    private String xmlData;
}
