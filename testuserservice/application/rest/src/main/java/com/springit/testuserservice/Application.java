package com.springit.testuserservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@SpringBootApplication
@ComponentScan(basePackages = {"com.springit.testuserservice"})
@EnableJpaRepositories(basePackages = "com.springit.testuserservice.repository")
public class Application extends RepositoryRestMvcConfiguration {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
