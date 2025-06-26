package com.perfulandia.venta.assembler;

import com.perfulandia.venta.controller.PromocionController;
import com.perfulandia.venta.model.Promocion;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;



@Component
public class PromocionModelAssembler implements RepresentationModelAssembler<Promocion, EntityModel<Promocion>> {

    @Override
    public @NonNull EntityModel<Promocion> toModel(@NonNull Promocion promocion) {
        return EntityModel.of(
            promocion,
            linkTo(methodOn(PromocionController.class).getPromocionPorId(promocion.getId())).withSelfRel(),
            linkTo(methodOn(PromocionController.class).getTodasLasPromociones()).withRel("todas-las-promociones")
        );
    }
    
}
