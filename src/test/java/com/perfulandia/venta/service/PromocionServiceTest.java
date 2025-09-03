package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.repository.PromocionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PromocionServiceTest {

    private PromocionRepository promocionRepository;
    private PromocionService promocionService;

    @BeforeEach
    void setUp() {
        promocionRepository = mock(PromocionRepository.class);
        promocionService = new PromocionService(promocionRepository);
    }

    @Test
    void testObtenerTodas() {
        Promocion p1 = new Promocion();
        Promocion p2 = new Promocion();
        when(promocionRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Promocion> promociones = promocionService.obtenerTodas();

        assertEquals(2, promociones.size());
        verify(promocionRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Existente() {
        Promocion promocion = new Promocion();
        promocion.setId(1L);
        promocion.setCodigo("PROMO10");

        when(promocionRepository.findById(1L)).thenReturn(Optional.of(promocion));

        Optional<Promocion> resultado = promocionService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("PROMO10", resultado.get().getCodigo());
        verify(promocionRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoExistente() {
        when(promocionRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Promocion> resultado = promocionService.obtenerPorId(2L);

        assertFalse(resultado.isPresent());
        verify(promocionRepository, times(1)).findById(2L);
    }

    @Test
    void testCrear() {
        Promocion nueva = new Promocion();
        nueva.setCodigo("PROMO20");

        Promocion guardada = new Promocion();
        guardada.setId(10L);
        guardada.setCodigo("PROMO20");

        when(promocionRepository.save(nueva)).thenReturn(guardada);

        Promocion resultado = promocionService.crear(nueva);

        assertEquals(10L, resultado.getId());
        assertEquals("PROMO20", resultado.getCodigo());
        verify(promocionRepository, times(1)).save(nueva);
    }

    @Test
    void testEliminar() {
        promocionService.eliminar(5L);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(promocionRepository).deleteById(captor.capture());

        assertEquals(5L, captor.getValue());
    }
}

