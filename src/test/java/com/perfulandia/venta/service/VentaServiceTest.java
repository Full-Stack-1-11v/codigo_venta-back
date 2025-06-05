package com.perfulandia.venta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.VentaRepository;

@SpringBootTest
@ActiveProfiles("test")
public class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;

    @Test
    public void testGetVentas() {
        // Preparar datos
        List<Venta> ventas = new ArrayList<>();

        // Cuando alguien llame al repo, que devuelva esa lista vacía
        when(ventaRepository.findAll()).thenReturn(ventas);

        // Ejecutar el método a testear
        List<Venta> result = ventaService.obtenerTodasLasVentas();

        // Verificar resultados
        assertEquals(ventas, result);
        assertEquals(0, result.size());
    }
}
