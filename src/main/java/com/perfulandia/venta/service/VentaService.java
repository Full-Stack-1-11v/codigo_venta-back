package com.perfulandia.venta.service;

import com.perfulandia.venta.dto.SucursalDTO;
import com.perfulandia.venta.feign.SucursalClient;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.PromocionRepository;
import com.perfulandia.venta.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final PromocionRepository promocionRepository;
    private final SucursalClient sucursalClient;

    public VentaService(VentaRepository ventaRepository, PromocionRepository promocionRepository, SucursalClient sucursalClient) {
        this.ventaRepository = ventaRepository;
        this.promocionRepository = promocionRepository;
        this.sucursalClient = sucursalClient;
    }

    public Venta crearVenta(Venta venta, List<String> codigosPromocion) {
        // ✅ Validar sucursal
        validarSucursal(venta.getSucursalId());

        venta.setFecha(LocalDateTime.now());
        venta.setEstado("PENDIENTE");

        List<Promocion> promocionesAplicadas = new ArrayList<>();
        double totalDescuento = 0;

        for (String codigo : codigosPromocion) {
            promocionRepository.findByCodigoAndActivaTrue(codigo).ifPresent(promocion -> {
                promocionesAplicadas.add(promocion);
            });
        }

        double totalBase = venta.getTotal() != null ? venta.getTotal() : 100.0;

        for (Promocion promo : promocionesAplicadas) {
            if ("PORCENTAJE".equalsIgnoreCase(promo.getTipo())) {
                totalDescuento += totalBase * (promo.getValor() / 100.0);
            } else if ("MONTO_FIJO".equalsIgnoreCase(promo.getTipo())) {
                totalDescuento += promo.getValor();
            }
        }

        venta.setTotal(totalBase - totalDescuento);
        venta.setPromocionesAplicadas(promocionesAplicadas);

        return ventaRepository.save(venta);
    }

    private void validarSucursal(Long sucursalId) {
        SucursalDTO sucursal = sucursalClient.obtenerSucursalPorId(sucursalId);
        if (sucursal == null || !sucursal.isActiva()) {
            throw new RuntimeException("Sucursal inválida o inactiva");
        }
    }
}