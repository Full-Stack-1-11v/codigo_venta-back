package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public Factura obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Factura no encontrada con id: " + id));
    }
    
    public List<Factura> obtenerTodasLasFacturas() {
        return facturaRepository.findAll();
    }
    
    public boolean eliminarFactura(Long id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
}