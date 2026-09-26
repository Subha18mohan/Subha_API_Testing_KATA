package com.booking.api;

import com.booking.config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Builds the request settings shared by every API call: base URL, JSON headers
 * and logging. Steps start from this template instead of repeating the setup
 * (Factory pattern).
 */
public final class RequestSpecFactory {

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
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}