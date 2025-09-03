package com.perfulandia.venta.repository;

import com.perfulandia.venta.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    Optional<Promocion> findByCodigoAndActivaTrue(String codigo);
}