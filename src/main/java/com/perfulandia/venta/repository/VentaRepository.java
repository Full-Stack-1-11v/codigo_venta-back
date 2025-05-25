package com.perfulandia.venta.repository;

import com.perfulandia.venta.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> { }