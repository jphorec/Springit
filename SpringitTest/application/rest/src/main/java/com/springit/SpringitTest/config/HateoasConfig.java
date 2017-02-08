package com.springit.SpringitTest.config;

import com.springit.SpringitTest.domain.Films;
import com.springit.SpringitTest.service.hateoas.GenericResourceAssembler;
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
    public GenericResourceAssembler<Films, String> detailAssembler(final RepositoryEntityLinks entityLinks) {
        return new GenericResourceAssembler<>(entityLinks);
    }


}
