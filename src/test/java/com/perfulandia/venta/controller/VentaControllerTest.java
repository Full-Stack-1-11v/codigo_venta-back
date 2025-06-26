package com.perfulandia.venta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.venta.model.Venta;
import com.perfulandia.venta.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VentaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ventaRepository.deleteAll(); // Limpieza para test
    }

    @Test
    void testCrearYObtenerVenta() throws Exception {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        venta.setTotal(200.0);

        String ventaJson = objectMapper.writeValueAsString(venta);

        String responseJson = mockMvc.perform(post("/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ventaJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extraer ID directamente
        Long idCreado = objectMapper.readTree(responseJson).get("id").asLong();

        mockMvc.perform(get("/ventas/" + idCreado))
                .andExpect(status().isOk());
    }


    @Test
    void testEliminarVentaNoExistente() throws Exception {
        mockMvc.perform(delete("/ventas/9999"))
                .andExpect(status().isNotFound());
    }
}
