package com.tenniscourts.config.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        value = {
                @ApiResponse(responseCode =  "200", description  = "Success."),
                @ApiResponse(responseCode =  "401", description  = "Unauthorized - check your credentials."),
                @ApiResponse(responseCode =  "403", description  = "Access Denied - you don't have access to this service. Contact ENTITLEMENTS team.")
        }
)
public @interface ApiDefaultResponses {
}

