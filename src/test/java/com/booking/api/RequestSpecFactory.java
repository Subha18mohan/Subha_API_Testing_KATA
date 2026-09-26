package com.booking.api;

import com.booking.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;

/**
 * Builds the request settings shared by every API call: base URL, JSON headers
 * and logging. Steps start from this template instead of repeating the setup
 * (Factory pattern).
 */
public final class RequestSpecFactory {
    /**
     * Jackson set up for this API: LocalDate is written as "yyyy-MM-dd" (not as
     * numbers), and unknown response fields do not break deserialisation.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /** Tells Rest-Assured to use our ObjectMapper for every request and response. */
    private static final RestAssuredConfig REST_ASSURED_CONFIG = RestAssuredConfig.config()
            .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                    .jackson2ObjectMapperFactory((type, charset) -> OBJECT_MAPPER));
    static {
        // Print the full request and response only when a check fails,
        // so the console stays readable but failures are easy to analyse.
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private RequestSpecFactory() {
        // static factory - not meant to be instantiated
    }

    /** @return a new request template for the configured API that sends and accepts JSON */
    public static RequestSpecification baseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiConfig.get().baseUrl())
                .setConfig(REST_ASSURED_CONFIG)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}