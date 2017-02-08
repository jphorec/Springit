package com.{{.Company}}.{{.ProjectName}}.config;

import com.{{.Company}}.{{.ProjectName}}.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Class to initialize the spring boot servlet when run from a container
 */
public class WebConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
