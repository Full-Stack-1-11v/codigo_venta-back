package com.perfulandia.venta.controller;

import com.perfulandia.venta.service.PromocionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


public class PromocionControllerTest {

    @Mock
    private PromocionService promocionService;

    @InjectMocks
    private PromocionController promocionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exampleTest() {
        // Reemplaza esto con un test real
        assertNotNull(promocionController);
    }
}
