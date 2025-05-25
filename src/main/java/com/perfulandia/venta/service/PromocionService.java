package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public Promocion crearPromocion(Promocion promocion) {
        promocion.setActiva(true);
        return promocionRepository.save(promocion);
    }

    public List<Promocion> listarPromociones() {
        return promocionRepository.findAll();
    }
}
