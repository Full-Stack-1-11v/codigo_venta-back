package com.perfulandia.venta.service;

import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.repository.PromocionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearPromocion() {
        Promocion promocion = new Promocion();
        when(promocionRepository.save(any(Promocion.class))).thenReturn(promocion);

        Promocion resultado = promocionService.crearPromocion(promocion);

        assertNotNull(resultado);
        assertTrue(promocion.isActiva()); // ✅ usar isActiva() por boolean
        verify(promocionRepository, times(1)).save(promocion);
    }

    @Test
    void testListarPromociones() {
        List<Promocion> promociones = Arrays.asList(new Promocion(), new Promocion());
        when(promocionRepository.findAll()).thenReturn(promociones);

        List<Promocion> resultado = promocionService.listarPromociones();

        assertEquals(2, resultado.size());
        verify(promocionRepository, times(1)).findAll();
    }
}
