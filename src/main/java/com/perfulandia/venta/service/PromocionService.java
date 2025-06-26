package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.repository.PromocionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public List<Promocion> obtenerTodas() {
        List<Promocion> promociones = promocionRepository.findAll();
        log.info("Total de promociones encontradas: {}", promociones.size());
        return promociones;
    }

    public Optional<Promocion> obtenerPorId(Long id) {
        log.info("Buscando promoción con ID {}", id);
        Optional<Promocion> promocion = promocionRepository.findById(id);
        if (promocion.isPresent()) {
            log.info("Promoción encontrada: {}", promocion.get().getCodigo());
        } else {
            log.warn("Promoción con ID {} no encontrada", id);
        }
        return promocion;
    }

    public Promocion crear(Promocion promocion) {
        Promocion creada = promocionRepository.save(promocion);
        log.info("Promoción creada con ID {} y código {}", creada.getId(), creada.getCodigo());
        return creada;
    }

    public void eliminar(Long id) {
        log.info("Eliminando promoción con ID {}", id);
        promocionRepository.deleteById(id);
        log.info("Promoción con ID {} eliminada", id);
    }
}
