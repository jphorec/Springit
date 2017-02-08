package com.{{.Company}}.{{.ProjectName}}.service.hateoas;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

import java.io.Serializable;

/**
 * Used to wrap a specific entity in a {@link org.springframework.hateoas.Resource}.
 * Will automatically set any relevant hypermedia links on the generated resource
 *
 * @param <T>
 *     The type of entity being wrapped
 * @param <ID>
 *     The type of ID returned by getId() on the entity
 */
public class GenericResourceAssembler<T extends Identifiable<ID>, ID extends Serializable> implements ResourceAssembler<T, Resource<T>> {

    private EntityLinks entityLinks;

    public GenericResourceAssembler(final EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @Override
    public Resource<T> toResource(final T entity) {
        Link self = entityLinks.linkToSingleResource(entity.getClass(), entity.getId()).withSelfRel();
        return new Resource<T>(entity, self);
    }

    public Resource<T> toResource(final T entity, final Link... links) {
        Resource<T> resource = this.toResource(entity);
        resource.add(links);

        return resource;
    }
}
