package com.perfulandia.venta.assembler;

import com.perfulandia.venta.controller.FacturaController;
import com.perfulandia.venta.model.Factura;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

class FacturaModelAssemblerTest {

    private final FacturaModelAssembler assembler = new FacturaModelAssembler();

    @Test
    void testToModel() {
        // Preparar factura
        Factura factura = new Factura();
        factura.setId(1L);

        // Ejecutar toModel
        EntityModel<Factura> model = assembler.toModel(factura);

        // Verificaciones básicas
        assertNotNull(model);
        assertEquals(factura, model.getContent());

        // Verificar enlaces
        Link selfLink = linkTo(methodOn(FacturaController.class).getFacturaPorId(1L)).withSelfRel();
        Link allLink = linkTo(methodOn(FacturaController.class).getTodasLasFacturas()).withRel("todas-las-facturas");
        Link deleteLink = linkTo(methodOn(FacturaController.class).eliminarFactura(1L)).withRel("eliminar");

        assertTrue(model.getLinks().contains(selfLink));
        assertTrue(model.getLinks().contains(allLink));
        assertTrue(model.getLinks().contains(deleteLink));
    }
}
