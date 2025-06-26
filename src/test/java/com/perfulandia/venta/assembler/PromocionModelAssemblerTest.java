package com.perfulandia.venta.assembler;

import com.perfulandia.venta.controller.PromocionController;
import com.perfulandia.venta.model.Promocion;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

class PromocionModelAssemblerTest {

    private final PromocionModelAssembler assembler = new PromocionModelAssembler();

    @Test
    void testToModel() {
        // Preparar promoción
        Promocion promo = new Promocion();
        promo.setId(10L);
        promo.setCodigo("DESC10");
        promo.setTipo("PORCENTAJE");
        promo.setValor(10.0);

        // Ejecutar assembler
        EntityModel<Promocion> model = assembler.toModel(promo);

        // Validaciones
        assertNotNull(model);
        assertEquals(promo, model.getContent());

        // Enlaces esperados
        Link selfLink = linkTo(methodOn(PromocionController.class).getPromocionPorId(10L)).withSelfRel();
        Link allLink = linkTo(methodOn(PromocionController.class).getTodasLasPromociones()).withRel("todas-las-promociones");

        assertTrue(model.getLinks().contains(selfLink));
        assertTrue(model.getLinks().contains(allLink));
    }
}
