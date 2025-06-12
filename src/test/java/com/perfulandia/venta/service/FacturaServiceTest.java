package com.perfulandia.venta.service;


import static org.junit.jupiter.api.Assertions.*;

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
}
