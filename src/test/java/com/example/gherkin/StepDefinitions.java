package com.example.gherkin;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class StepDefinitions {

	private Response response;
	private final MockServer server = new MockServer();

	@When("users want to get information on the {string} project")
	public void users_want_to_get_information_on_the_project(final String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}

	@Then("the requested data is returned")
	public void the_requested_data_is_returned() {
		Assertions.assertNotNull(this.response);
	}

}
