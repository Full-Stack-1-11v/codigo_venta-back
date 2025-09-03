package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.repository.FacturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Slf4j
@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;

    public FacturaService(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    public Factura emitirFactura(Factura factura) {
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumeroDocumento("F001-000123"); // Número simulado
        Factura guardada = facturaRepository.save(factura);
        log.info("Factura emitida con ID {} y número {}", guardada.getId(), guardada.getNumeroDocumento());
        return guardada;
    }

    public Factura obtenerFacturaPorId(Long id) {
        log.info("Buscando factura con ID {}", id);
        return facturaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Factura no encontrada con ID {}", id);
                return new RuntimeException("Factura no encontrada con id: " + id);
            });
    }

    public List<Factura> obtenerTodasLasFacturas() {
        List<Factura> facturas = facturaRepository.findAll();
        log.info("Total de facturas encontradas: {}", facturas.size());
        return facturas;
    }

    public boolean eliminarFactura(Long id) {
        log.info("Intentando eliminar factura con ID {}", id);
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            log.info("Factura con ID {} eliminada", id);
            return true;
        } else {
            log.warn("No se encontró factura con ID {} para eliminar", id);
            return false;
        }
    }
}
