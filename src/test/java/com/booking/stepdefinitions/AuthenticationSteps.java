package com.booking.stepdefinitions;

import com.booking.api.Endpoints;
import com.booking.api.RequestSpecFactory;
import com.booking.config.ApiConfig;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

/** Step definitions for the admin login feature. */
public class AuthenticationSteps {

    private Response response;

    @When("the administrator logs in with valid credentials")
    public void theAdministratorLogsInWithValidCredentials() {
        ApiConfig config = ApiConfig.get();
        response = given()
                .spec(RequestSpecFactory.baseSpec())
                .body("""
                        {"username": "%s", "password": "%s"}
                        """.formatted(config.username(), config.password()))
                .when()
                .post(Endpoints.LOGIN);
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