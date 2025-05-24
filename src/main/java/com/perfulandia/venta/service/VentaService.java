package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> obtenerPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta guardar(Venta venta) {
        venta.setFecha(LocalDate.now());
        return ventaRepository.save(venta);
    }

    public Optional<Venta> actualizar(Long id, Venta nuevaVenta) {
        return ventaRepository.findById(id).map(venta -> {
            venta.setCliente(nuevaVenta.getCliente());
            venta.setTotal(nuevaVenta.getTotal());
            return ventaRepository.save(venta);
        });
    }

    public boolean eliminar(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}