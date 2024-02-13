package com.tenniscourts.config;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


public class BaseRestController {
    @Hidden
    protected URI locationByEntity(Long entityId) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(entityId).toUri();
    }
}
