package com.example.gherkin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;

import com.example.gherkin.server.MockServer;
import com.example.gherkin.server.User;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class VerifyRequestsOfUserOperationsSteps {

	private static final String USERS_PATH = "/users";

	private Response response;
	private final MockServer server = new MockServer();
	private HashMap<Integer, User> users;

	@Given("web service with non users")
	public void web_service_with_non_users() {
		users = new HashMap<Integer, User>();
		server.startAndConfigureMockServer(users);
	}

	@Given("web service with users")
	public void web_service_with_users(final Map<Integer, String> users) {
		this.users = new HashMap<Integer, User>();
		users.forEach((id, name) -> this.users.put(id, new User(id, name)));
		server.startAndConfigureMockServer(this.users);
	}

	@When("retrieve all users")
	public void retrieve_all_users() {
		response = RestAssured.get(server.getHost() + USERS_PATH).thenReturn();
	}

	@Then("status will be {string}")
	public void status_will_be(final String status) {
		final int statusCode;
		switch (status) {
		case "success":
			statusCode = HttpStatus.SC_OK;
			break;
		case "not found":
			statusCode = HttpStatus.SC_NOT_FOUND;
			break;
		case "created":
			statusCode = HttpStatus.SC_CREATED;
			break;
		case "no content":
			statusCode = HttpStatus.SC_NO_CONTENT;
			break;
		default:
			statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			break;
		}
		Assertions.assertNotNull(response);
		response.then().statusCode(statusCode);
	}

	@Then("all users will be returned")
	public void all_users_will_be_returned() {
		Assertions.assertNotNull(response);
		response.then().log().all();
		response.then().statusCode(HttpStatus.SC_OK);
		response.then().contentType(ContentType.JSON);
		final List<User> values = users.values().stream().collect(Collectors.toList());
		for (int i = 0; i < users.size(); i++) {
			response.then().body("[" + i + "].id", CoreMatchers.equalTo(values.get(i).getId()));
			response.then().body("[" + i + "].name", CoreMatchers.equalTo(values.get(i).getName()));
		}
	}

}
