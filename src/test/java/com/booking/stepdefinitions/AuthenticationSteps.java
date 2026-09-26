package com.booking.stepdefinitions;

import com.booking.api.Endpoints;
import com.booking.api.RequestSpecFactory;
import com.booking.config.ApiConfig;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/** Step definitions for the admin login feature. */
public class AuthenticationSteps {

    private static final int HTTP_OK = 200;
    private static final int HTTP_UNAUTHORIZED = 401;

    private Response response;

    @When("the administrator logs in with valid credentials")
    public void theAdministratorLogsInWithValidCredentials() {
        ApiConfig config = ApiConfig.get();
        logIn(config.username(), config.password());
    }

    @When("someone logs in with username {string} and password {string}")
    public void someoneLogsInWith(String username, String password) {
        logIn(username, password);
    }

    @Then("the login is successful")
    public void theLoginIsSuccessful() {
        response.then().statusCode(HTTP_OK);
    }

    @Then("the login is refused")
    public void theLoginIsRefused() {
        response.then().statusCode(HTTP_UNAUTHORIZED);
    }

    @Then("the reason given is {string}")
    public void theReasonGivenIs(String reason) {
        response.then().body("error", equalTo(reason));
    }

    @Then("an authentication token is issued")
    public void anAuthenticationTokenIsIssued() {
        response.then().body("token", not(emptyOrNullString()));
    }

    @Then("no authentication token is issued")
    public void noAuthenticationTokenIsIssued() {
        response.then().body("token", nullValue());
    }

    /** Sends the login request; shared by both When steps so the request is built in one place. */
    private void logIn(String username, String password) {
        response = given()
                .spec(RequestSpecFactory.baseSpec())
                .body("""
                        {"username": "%s", "password": "%s"}
                        """.formatted(username, password))
                .when()
                .post(Endpoints.LOGIN);
    }
}