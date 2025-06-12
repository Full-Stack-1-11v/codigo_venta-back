package com.perfulandia.venta.controller;

import com.perfulandia.venta.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;


public class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void exampleTest() {
        // Reemplaza esto con un test real
        assertNotNull(ventaController);
    }
}
