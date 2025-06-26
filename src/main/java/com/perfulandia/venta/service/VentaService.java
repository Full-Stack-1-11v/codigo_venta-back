package com.perfulandia.venta.service;

import com.perfulandia.venta.dto.ProductoDTO;
import com.perfulandia.venta.dto.SucursalDTO;
import com.perfulandia.venta.feign.InventarioClient;
import com.perfulandia.venta.feign.SucursalClient;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.PromocionRepository;
import com.perfulandia.venta.repository.VentaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final PromocionRepository promocionRepository;
    private final SucursalClient sucursalClient;
    private final InventarioClient inventarioClient;

    public VentaService(
        VentaRepository ventaRepository,
        PromocionRepository promocionRepository,
        SucursalClient sucursalClient,
        InventarioClient inventarioClient
    ) {
        this.ventaRepository = ventaRepository;
        this.promocionRepository = promocionRepository;
        this.sucursalClient = sucursalClient;
        this.inventarioClient = inventarioClient;
    }

    public Venta crearVenta(Venta venta, List<String> codigosPromocion) {
        log.info("Iniciando creación de venta para cliente ID: {}", venta.getClienteId());

        if (venta.getSucursalId() != null) {
            try {
                validarSucursal(venta.getSucursalId());
            } catch (Exception e) {
                log.error("Error al validar sucursal con ID {}: {}", venta.getSucursalId(), e.getMessage());
                throw new RuntimeException("No se pudo validar la sucursal. Detalles: " + e.getMessage());
            }
        }

        venta.setFecha(LocalDateTime.now());
        venta.setEstado("PENDIENTE");

        List<Promocion> promocionesAplicadas = new ArrayList<>();
        double totalDescuento = 0;

        for (String codigo : codigosPromocion) {
            promocionRepository.findByCodigoAndActivaTrue(codigo).ifPresent(promocion -> {
                log.info("Promoción aplicada: {} ({} - {})", promocion.getCodigo(), promocion.getTipo(), promocion.getValor());
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

        Venta guardada = ventaRepository.save(venta);
        log.info("Venta creada con ID: {}, total final: {}", guardada.getId(), guardada.getTotal());

        return guardada;
    }

    public boolean eliminarVenta(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
            log.info("Venta eliminada con ID: {}", id);
            return true;
        } else {
            log.warn("Intento de eliminar venta no existente. ID: {}", id);
            return false;
        }
    }

    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        log.info("Buscando ventas del cliente con ID: {}", clienteId);
        return ventaRepository.findByClienteId(clienteId);
    }

    public List<Venta> obtenerTodasLasVentas() {
        log.info("Obteniendo todas las ventas");
        return ventaRepository.findAll();
    }

    public Venta obtenerVentaPorId(Long id) {
        log.info("Buscando venta por ID: {}", id);
        return ventaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Venta no encontrada con ID: {}", id);
                return new RuntimeException("Venta no encontrada con id: " + id);
            });
    }

    public void validarProducto(Long productoId) {
        log.info("Validando producto con ID: {}", productoId);
        try {
            ProductoDTO producto = inventarioClient.obtenerProductoPorId(productoId);
            log.debug("Producto recibido: {}", producto);

            if (producto == null || !producto.isActivo()) {
                log.warn("Producto con ID {} no es válido o está inactivo", productoId);
                throw new RuntimeException("Producto inactivo o no válido");
            }

            log.info("Producto con ID {} validado correctamente", productoId);

        } catch (feign.FeignException.NotFound nf) {
            log.error("Producto no encontrado con ID {}", productoId);
            throw new RuntimeException("Producto no encontrado con ID: " + productoId);
        } catch (Exception e) {
            log.error("Error general al validar producto: {}", e.getMessage());
            throw new RuntimeException("Error al validar producto: " + e.getMessage());
        }
    }

    public void validarSucursal(Long sucursalId) {
        log.info("Validando sucursal con ID: {}", sucursalId);
        try {
            SucursalDTO sucursal = sucursalClient.obtenerSucursalPorId(sucursalId);
            log.debug("Sucursal recibida: {}", sucursal);
    
            if (sucursal == null || !sucursal.isActiva()) {
                log.warn("Sucursal con ID {} no es válida o está inactiva", sucursalId);
                throw new RuntimeException("Sucursal inválida o inactiva");
            }
    
            log.info("Sucursal con ID {} validada correctamente", sucursalId);
    
        } catch (feign.FeignException.NotFound nf) {
            log.error("Sucursal no encontrada con ID {}", sucursalId);
            throw new RuntimeException("Sucursal no encontrada");
        } catch (Exception e) {
            log.error("Error general al validar sucursal: {}", e.getMessage());
            throw new RuntimeException("Error al validar sucursal");
        }
    }
    
}

