package com.{{.Company}}.{{.ProjectName}}.config;

import com.{{.Company}}.{{.ProjectName}}.domain.{{.ClassName}};
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourceAssembler;
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourcesAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;

/**
 * JavaConfig for beans related to hypermedia formatting
 */
@SpringBootApplication
public class HateoasConfig {

    @Bean
    @Autowired
    public GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> resourceAssembler(final RepositoryEntityLinks entityLinks) {
        return new GenericResourceAssembler<>(entityLinks);
    }

    @Bean
    @Autowired
    public GenericResourcesAssembler<{{.ClassName}}> resourcesAssembler(final HateoasPageableHandlerMethodArgumentResolver resolver,
                                                                final RepositoryEntityLinks entityLinks,
                                                                final HateoasSortHandlerMethodArgumentResolver sortResolver,
                                                                final ResourceMappings mappings) {
        return new GenericResourcesAssembler<>(resolver, entityLinks, resourceAssembler(entityLinks), sortResolver, mappings, {{.ClassName}}.class);
    }

}
