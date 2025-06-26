package com.perfulandia.venta.assembler;

import com.perfulandia.venta.controller.VentaController;
import com.perfulandia.venta.model.Venta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<Venta, EntityModel<Venta>> {

    @Override
    public @NonNull EntityModel<Venta> toModel(@NonNull Venta venta) {
        return EntityModel.of(venta,
            linkTo(methodOn(VentaController.class).getVentaPorId(venta.getId())).withSelfRel(),
            linkTo(methodOn(VentaController.class).getTodasLasVentas()).withRel("todas-las-ventas"),
            linkTo(methodOn(VentaController.class).eliminarVenta(venta.getId())).withRel("eliminar")
        );
    }
}

