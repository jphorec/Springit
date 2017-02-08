package com.{{.Company}}.{{.ProjectName}}.service.hateoas;

import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ParameterMetadata;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.support.DefaultedPageable;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Wraps a collections of entities in a {@link org.springframework.hateoas.Resources} class.
 * Each individual entity will be wrapped in its own {@link org.springframework.hateoas.Resource}, pagination properties and collection
 * hypermedia links will also be automatically handled.
 *
 * @param <T> The type of entities being provided
 */
public class GenericResourcesAssembler<T> extends PagedResourcesAssembler<T> {
    private RepositoryEntityLinks entityLinks;
    private HateoasSortHandlerMethodArgumentResolver sortResolver;
    private ResourceMappings mappings;
    private ResourceAssembler<T, Resource<T>> assembler;
    private Class<?> domainType;

    public GenericResourcesAssembler(final HateoasPageableHandlerMethodArgumentResolver resolver, final RepositoryEntityLinks entityLinks,
                                     final ResourceAssembler<T, Resource<T>> assembler, final HateoasSortHandlerMethodArgumentResolver sortResolver,
                                     final ResourceMappings mappings, final Class<?> domainType) {
        super(resolver, null);
        this.entityLinks = entityLinks;
        this.sortResolver = sortResolver;
        this.mappings = mappings;
        this.assembler = assembler;
        this.domainType = domainType;
    }

    public Resources<Resource<T>> toResource(final Iterable<T> entities, final DefaultedPageable pageable) {
        Resources<Resource<T>> resources;
        if (entities instanceof Page) {
            Link baseLink = entityLinks.linkToPagedResource(domainType, (pageable == null || pageable.isDefault()) ? null
                    : pageable.getPageable());
            resources = super.toResource((Page<T>) entities, assembler, baseLink);
        } else if (entities != null) {
            resources = toResource(entities);
        } else {
            resources = new Resources<>(Collections.<Resource<T>>emptyList());
        }

        SearchResourceMappings searchMappings = mappings.getSearchResourceMappings(domainType);
        resources.add(entityLinks.linkFor(domainType).slash(searchMappings.getPath()).withRel(searchMappings.getRel()));

        return resources;
    }

    public Resources<Resource<T>> toResource(final Iterable<T> entities) {
        List<Resource<T>> resources = new ArrayList<>();

        for (T entity : entities) {
            resources.add(assembler.toResource(entity));
        }

        return new Resources<>(resources, entityLinks.linkToCollectionResource(domainType).withSelfRel());
    }

    /**
     * Returns {@link org.springframework.hateoas.Links} to the individual searches exposed.
     *
     * @return
     */
    public Links getSearchLinks() {

        List<Link> links = new ArrayList<>();

        SearchResourceMappings searchMappings = mappings.getSearchResourceMappings(domainType);
        LinkBuilder builder = entityLinks.linkFor(domainType).slash(searchMappings.getPath());

        for (MethodResourceMapping mapping : searchMappings) {

            if (!mapping.isExported()) {
                continue;
            }

            TemplateVariables variables = new TemplateVariables();

            for (ParameterMetadata metadata : mapping.getParametersMetadata()) {
                variables = variables.concat(new TemplateVariable(metadata.getName(), TemplateVariable.VariableType.REQUEST_PARAM));
            }

            String href = builder.slash(mapping.getPath()).toString().concat(variables.toString());

            Link link = new Link(href, mapping.getRel());

            if (mapping.isPagingResource()) {
                link = super.appendPaginationParameterTemplates(link);
            } else if (mapping.isSortableResource()) {

                TemplateVariables sortVariable = sortResolver.getSortTemplateVariables(null, UriComponentsBuilder
                        .fromUriString(link.expand().getHref()).build());
                link = new Link(new UriTemplate(link.getHref()).with(sortVariable), link.getRel());
            }

            links.add(link);
        }

        return new Links(links);
    }
}
