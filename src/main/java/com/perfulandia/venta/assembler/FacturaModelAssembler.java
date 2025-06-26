package com.perfulandia.venta.assembler;

import com.perfulandia.venta.controller.FacturaController;
import com.perfulandia.venta.model.Factura;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FacturaModelAssembler implements RepresentationModelAssembler<Factura, EntityModel<Factura>> {

    @Override
    public @NonNull EntityModel<Factura> toModel(@NonNull Factura factura) {
        return EntityModel.of(factura,
            linkTo(methodOn(FacturaController.class).getFacturaPorId(factura.getId())).withSelfRel(),
            linkTo(methodOn(FacturaController.class).getTodasLasFacturas()).withRel("todas-las-facturas"),
            linkTo(methodOn(FacturaController.class).eliminarFactura(factura.getId())).withRel("eliminar")
        );
    }
}

