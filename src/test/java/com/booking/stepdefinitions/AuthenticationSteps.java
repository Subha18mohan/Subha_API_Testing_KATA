package com.booking.stepdefinitions;

import com.booking.api.AuthClient;
import com.booking.config.ApiConfig;
import com.booking.context.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.hamcrest.Matchers.*;

@RequiredArgsConstructor
public class AuthenticationSteps {
    private static final int HTTP_OK = 200;
    private static final int HTTP_UNAUTHORIZED = 401;

    private final ScenarioContext context;
    private final AuthClient authClient;

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
        context.getLastResponse().then().statusCode(HTTP_OK);
    }

    @Then("the login is refused")
    public void theLoginIsRefused() {
        context.getLastResponse().then().statusCode(HTTP_UNAUTHORIZED);
    }

    @Then("the reason given is {string}")
    public void theReasonGivenIs(String reason) {
        context.getLastResponse().then().body("error", equalTo(reason));
    }

    @Then("an authentication token is issued")
    public void anAuthenticationTokenIsIssued() {
        context.getLastResponse().then().body("token", not(emptyOrNullString()));
    }

    @Then("no authentication token is issued")
    public void noAuthenticationTokenIsIssued() {
        context.getLastResponse().then().body("token", nullValue());
    }

    private void logIn(String username, String password) {
        context.setLastResponse(authClient.login(username, password));
    }
}
