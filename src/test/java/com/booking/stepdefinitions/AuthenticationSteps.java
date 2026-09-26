package com.booking.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

/** Step definitions for the admin login feature. */
public class AuthenticationSteps {

    private Response response;

    @When("the administrator logs in with valid credentials")
    public void theAdministratorLogsInWithValidCredentials() {
        com.booking.config.ApiConfig config = com.booking.config.ApiConfig.get();
        response = given()
                .baseUri(config.baseUrl())
                .contentType(ContentType.JSON)
                .body("""
                        {"username": "%s", "password": "%s"}
                        """.formatted(config.username(), config.password()))
                .when()
                .post("/auth/login");
    }

    @Then("the login is successful")
    public void theLoginIsSuccessful() {
        response.then().statusCode(200);
    }

    @Then("an authentication token is issued")
    public void anAuthenticationTokenIsIssued() {
        response.then().body("token", not(emptyOrNullString()));
    }
}