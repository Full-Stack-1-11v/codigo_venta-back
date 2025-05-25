package com.perfulandia.venta.repository;

import com.perfulandia.venta.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> { }