package com.perfulandia.venta.service;

import com.perfulandia.venta.dto.SucursalDTO;
import com.perfulandia.venta.feign.SucursalClient;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.PromocionRepository;
import com.perfulandia.venta.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private PromocionRepository promocionRepository;

    @Mock
    private SucursalClient sucursalClient;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearVentaConPromocionesYClienteValido() {
        Venta venta = new Venta();
        venta.setTotal(200.0);
        venta.setSucursalId(1L);

        Promocion promo = new Promocion();
        promo.setTipo("PORCENTAJE");
        promo.setValor(10.0);

        when(promocionRepository.findByCodigoAndActivaTrue("PROMO1")).thenReturn(Optional.of(promo));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(sucursalClient.obtenerSucursalPorId(1L)).thenReturn(new SucursalDTO(1L, "Sucursal", "Calle 123", true));

        Venta result = ventaService.crearVenta(venta, Arrays.asList("PROMO1"));

        assertNotNull(result);
        verify(ventaRepository).save(venta);
        verify(promocionRepository).findByCodigoAndActivaTrue("PROMO1");
        verify(sucursalClient).obtenerSucursalPorId(1L);
    }

    @Test
    void testCrearVentaConSucursalInvalida() {
        Venta venta = new Venta();
        venta.setSucursalId(1L);

        when(sucursalClient.obtenerSucursalPorId(1L)).thenThrow(new RuntimeException("Error de red"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ventaService.crearVenta(venta, Arrays.asList("PROMO1")));

        assertTrue(ex.getMessage().contains("No se pudo validar la sucursal"));
    }

    @Test
    void testValidarSucursalValida() {
        SucursalDTO dto = new SucursalDTO();
        dto.setActiva(true);
        when(sucursalClient.obtenerSucursalPorId(5L)).thenReturn(dto);

        assertDoesNotThrow(() -> ventaService.validarSucursal(5L));
        verify(sucursalClient).obtenerSucursalPorId(5L);
    }

    @Test
    void testEliminarVentaExiste() {
        when(ventaRepository.existsById(1L)).thenReturn(true);

        boolean result = ventaService.eliminarVenta(1L);

        assertTrue(result);
        verify(ventaRepository).deleteById(1L);
    }

    @Test
    void testEliminarVentaNoExiste() {
        when(ventaRepository.existsById(1L)).thenReturn(false);

        boolean result = ventaService.eliminarVenta(1L);

        assertFalse(result);
        verify(ventaRepository, never()).deleteById(1L);
    }

    @Test
    void testObtenerVentasPorCliente() {
        List<Venta> ventas = Arrays.asList(new Venta(), new Venta());
        when(ventaRepository.findByClienteId(10L)).thenReturn(ventas);

        List<Venta> resultado = ventaService.obtenerVentasPorCliente(10L);

        assertEquals(2, resultado.size());
        verify(ventaRepository).findByClienteId(10L);
    }

    @Test
    void testObtenerTodasLasVentas() {
        List<Venta> ventas = Arrays.asList(new Venta(), new Venta());
        when(ventaRepository.findAll()).thenReturn(ventas);

        List<Venta> resultado = ventaService.obtenerTodasLasVentas();

        assertEquals(2, resultado.size());
        verify(ventaRepository).findAll();
    }
}
