package com.perfulandia.venta.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.perfulandia.venta.model.Factura;
import com.perfulandia.venta.repository.FacturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exampleTest() {
        // Reemplaza esto con un test real
        assertNotNull(facturaService);
    }

    @Test
    void testEmitirFactura() {
        Factura factura = new Factura();
        when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> {
            Factura f = i.getArgument(0);
            f.setId(1L);
            return f;
        });

        Factura resultado = facturaService.emitirFactura(factura);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void testObtenerFacturaPorId_Existente() {
        Factura factura = new Factura();
        factura.setId(1L);
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        Factura resultado = facturaService.obtenerFacturaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testObtenerFacturaPorId_NoExistente() {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> facturaService.obtenerFacturaPorId(99L));
    }

    @Test
    void testObtenerTodasLasFacturas() {
        List<Factura> lista = List.of(new Factura(), new Factura());
        when(facturaRepository.findAll()).thenReturn(lista);

        List<Factura> resultado = facturaService.obtenerTodasLasFacturas();

        assertEquals(2, resultado.size());
    }

    @Test
    void testEliminarFactura_Existente() {
        when(facturaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = facturaService.eliminarFactura(1L);

        assertTrue(resultado);
        verify(facturaRepository).deleteById(1L);
    }

    @Test
    void testEliminarFactura_NoExistente() {
        when(facturaRepository.existsById(1L)).thenReturn(false);

        boolean resultado = facturaService.eliminarFactura(1L);

        assertFalse(resultado);
    }




}
