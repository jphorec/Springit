package com.{{.Company}}.{{.ProjectName}}.config;

import com.{{.Company}}.{{.ProjectName}}.domain.{{.ClassName}};
import com.{{.Company}}.{{.ProjectName}}.service.hateoas.GenericResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;

/**
 * JavaConfig for beans related to hypermedia formatting
 */
@SpringBootApplication
public class HateoasConfig {

    @Bean
    @Autowired
    public GenericResourceAssembler<{{.ClassName}}, {{.PrimaryType}}> detailAssembler(final RepositoryEntityLinks entityLinks) {
        return new GenericResourceAssembler<>(entityLinks);
    }


}
