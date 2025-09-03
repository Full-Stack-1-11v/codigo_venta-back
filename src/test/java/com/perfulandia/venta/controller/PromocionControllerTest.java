package com.perfulandia.venta.controller;

import com.perfulandia.venta.assembler.PromocionModelAssembler;
import com.perfulandia.venta.model.Promocion;
import com.perfulandia.venta.service.PromocionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromocionController.class)
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromocionService promocionService;

    @MockBean
    private PromocionModelAssembler promocionModelAssembler;

    @Test
    void testGetTodasLasPromociones() throws Exception {
        Promocion promo = new Promocion();
        promo.setId(1L);

        when(promocionService.obtenerTodas()).thenReturn(List.of(promo));
        when(promocionModelAssembler.toModel(any(Promocion.class))).thenReturn(EntityModel.of(promo));

        mockMvc.perform(get("/promociones"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetPromocionPorId() throws Exception {
        Promocion promo = new Promocion();
        promo.setId(1L);

        when(promocionService.obtenerPorId(1L)).thenReturn(Optional.of(promo));
        when(promocionModelAssembler.toModel(any(Promocion.class))).thenReturn(EntityModel.of(promo));

        mockMvc.perform(get("/promociones/1"))
               .andExpect(status().isOk());
    }

    @Test
    void testCrearPromocion() throws Exception {
        Promocion promo = new Promocion();
        promo.setId(1L);
        promo.setCodigo("TEST10");
        promo.setTipo("PORCENTAJE");
        promo.setValor(10.0);

        when(promocionService.crear(any(Promocion.class))).thenReturn(promo);
        when(promocionModelAssembler.toModel(any(Promocion.class))).thenReturn(EntityModel.of(promo));

        mockMvc.perform(post("/promociones")
               .contentType("application/json")
               .content("{\"codigo\":\"TEST10\",\"tipo\":\"PORCENTAJE\",\"valor\":10.0}"))
               .andExpect(status().isCreated());
    }

    @Test
    void testEliminarPromocion() throws Exception {
        doNothing().when(promocionService).eliminar(1L);

        mockMvc.perform(delete("/promociones/1"))
               .andExpect(status().isNoContent());
    }
}

