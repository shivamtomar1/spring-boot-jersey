package com.microservice.nav.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.microservice.nav.ErrorHandling.*;
import com.microservice.nav.controller.*;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        
        register(NavController.class);
        register(FormatException.class);
        register(DateNotFoundException.class);
        register(GenericExceptionMapper.class);
    }
}
