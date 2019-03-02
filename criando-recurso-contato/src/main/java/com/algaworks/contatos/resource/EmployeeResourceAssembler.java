package com.algaworks.contatos.resource;

import com.algaworks.contatos.model.Contato;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Contato, Resource<Contato>> {


    @Override
    public Resource<Contato> toResource(Contato contato) {
        return new Resource<>(contato,
                linkTo(methodOn(ContatosResource.class).buscar(contato.getId())).withSelfRel(),
                linkTo(methodOn(ContatosResource.class).listar()).withRel("contatos"));
    }
}
