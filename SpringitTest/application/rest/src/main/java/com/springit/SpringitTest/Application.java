package com.springit.SpringitTest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@SpringBootApplication
@ComponentScan(basePackages = {"com.springit.SpringitTest"})
@EnableJpaRepositories(basePackages = "com.springit.SpringitTest.repository")
public class Application extends RepositoryRestMvcConfiguration {
    /** Logger available to subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
