package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.repository.FacturaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public Factura emitirFactura(Factura factura) {
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumeroDocumento("F001-000123"); // Número simulado
        return facturaRepository.save(factura);
    }
}