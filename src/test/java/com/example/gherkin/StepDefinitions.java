package com.example.gherkin;

import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StepDefinitions {

	private Response response;
	private final MockServer server = new MockServer();

	@When("users want to get information on the {string} project")
	public void users_want_to_get_information_on_the_project(final String string) {
		this.server.startAndConfigureMockServer();
		this.response = RestAssured.get(this.server.getHost() + "/users").thenReturn();
		this.server.stop();
	}

	@Then("the requested data is returned")
	public void the_requested_data_is_returned() {
		Assertions.assertNotNull(this.response);
		this.response.then().statusCode(HttpStatus.OK_200);
		this.response.then().contentType(ContentType.JSON);
		this.response.then().body("[0].name", CoreMatchers.equalTo("user1"));
		this.response.then().body("[1].name", CoreMatchers.equalTo("user2"));
	}

}
