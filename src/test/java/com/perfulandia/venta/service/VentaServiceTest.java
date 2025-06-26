package com.perfulandia.venta.service;

import com.perfulandia.venta.dto.ProductoDTO;
import com.perfulandia.venta.dto.SucursalDTO;
import com.perfulandia.venta.feign.InventarioClient;
import com.perfulandia.venta.feign.SucursalClient;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.PromocionRepository;
import com.perfulandia.venta.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private PromocionRepository promocionRepository;

    @Mock
    private SucursalClient sucursalClient;

    @Mock
    private InventarioClient inventarioClient;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearVenta_SinPromociones() {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        venta.setTotal(100.0);

        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta result = ventaService.crearVenta(venta, List.of());

        assertNotNull(result);
        assertEquals(100.0, result.getTotal());
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    public void testEliminarVentaExistente() {
        when(ventaRepository.existsById(1L)).thenReturn(true);

        boolean result = ventaService.eliminarVenta(1L);

        assertTrue(result);
        verify(ventaRepository).deleteById(1L);
    }

    @Test
    public void testEliminarVentaNoExistente() {
        when(ventaRepository.existsById(1L)).thenReturn(false);

        boolean result = ventaService.eliminarVenta(1L);

        assertFalse(result);
    }

    @Test
    public void testObtenerVentasPorCliente() {
        ventaService.obtenerVentasPorCliente(1L);
        verify(ventaRepository).findByClienteId(1L);
    }

    @Test
    public void testObtenerTodasLasVentas() {
        ventaService.obtenerTodasLasVentas();
        verify(ventaRepository).findAll();
    }

    @Test
    public void testObtenerVentaPorId_Existente() {
        Venta venta = new Venta();
        venta.setId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta result = ventaService.obtenerVentaPorId(1L);

        assertNotNull(result);
    }

    @Test
    public void testObtenerVentaPorId_NoExistente() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> ventaService.obtenerVentaPorId(1L));
        assertTrue(exception.getMessage().contains("Venta no encontrada"));
    }

    @Test
    public void testValidarProducto_Activo() {
        ProductoDTO producto = new ProductoDTO();
        producto.setActivo(true);
        when(inventarioClient.obtenerProductoPorId(1L)).thenReturn(producto);

        assertDoesNotThrow(() -> ventaService.validarProducto(1L));
    }

    @Test
    public void testValidarProducto_Inactivo() {
        ProductoDTO producto = new ProductoDTO();
        producto.setActivo(false);
        when(inventarioClient.obtenerProductoPorId(1L)).thenReturn(producto);

        assertThrows(RuntimeException.class, () -> ventaService.validarProducto(1L));
    }

    @Test
    public void testValidarSucursal_Activa() {
        SucursalDTO sucursal = new SucursalDTO();
        sucursal.setActiva(true);
        when(sucursalClient.obtenerSucursalPorId(1L)).thenReturn(sucursal);

        assertDoesNotThrow(() -> ventaService.validarSucursal(1L));
    }

    @Test
    void testValidarSucursal_Inactiva() {
        Long sucursalId = 1L;
        SucursalDTO sucursal = new SucursalDTO();
        sucursal.setSucursalId(sucursalId); // ✅ Usar el nombre correcto del campo
        sucursal.setNombre("Sucursal X");
        sucursal.setActiva(false); // ✅ Esto ya está bien


        when(sucursalClient.obtenerSucursalPorId(sucursalId)).thenReturn(sucursal);

        assertThrows(RuntimeException.class, () -> ventaService.validarSucursal(sucursalId));
    }

    @Test
    void testCrearVentaConSucursalActiva() {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        venta.setSucursalId(1L);
        venta.setTotal(100.0);

        SucursalDTO sucursal = new SucursalDTO();
        sucursal.setActiva(true);

        when(sucursalClient.obtenerSucursalPorId(1L)).thenReturn(sucursal);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId(10L);
            return v;
        });

        Venta resultado = ventaService.crearVenta(venta, List.of());

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(sucursalClient).obtenerSucursalPorId(1L);
    }

    @Test
    void testCrearVentaConPromocionPorcentaje() {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        venta.setTotal(200.0);

        Promocion promo = new Promocion();
        promo.setCodigo("DESC10");
        promo.setTipo("PORCENTAJE");
        promo.setValor(10.0); // 10%

        when(promocionRepository.findByCodigoAndActivaTrue("DESC10")).thenReturn(Optional.of(promo));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId(20L);
            return v;
        });

        Venta resultado = ventaService.crearVenta(venta, List.of("DESC10"));

        assertNotNull(resultado);
        assertEquals(180.0, resultado.getTotal()); // 200 - 10%
        assertEquals(1, resultado.getPromocionesAplicadas().size());
    }





}