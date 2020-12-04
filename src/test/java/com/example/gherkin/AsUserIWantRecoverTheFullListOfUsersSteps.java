package com.example.gherkin;

import static java.lang.Integer.valueOf;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;
import org.hamcrest.Matchers;

import com.example.gherkin.server.MockServer;
import com.example.gherkin.server.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AsUserIWantRecoverTheFullListOfUsersSteps {

	private static final String USERS_PATH = "/users";

	private Response response;
	private final MockServer server = new MockServer();

	@Given("web service with no users")
	public void web_service_with_no_users() {
		server.startAndConfigureMockServer(new HashMap<Integer, User>());
	}

	@When("retrieve all users")
	public void retrieve_all_users() {
		response = RestAssured.get(server.getHost() + USERS_PATH).thenReturn();
	}

	@Then("status will be {string}")
	public void status_will_be(final String status) {
		final var statusCode = "success".equals(status) ? HttpStatus.OK_200 : HttpStatus.NO_CONTENT_204;
		response.then().statusCode(statusCode);
	}

	@Given("web service with users")
	public void web_service_with_users(final List<Map<String, String>> dataTable) {
		final var users = new HashMap<Integer, User>();
		dataTable.forEach(map -> users.put(valueOf(map.get("id")),
				new User(valueOf(map.get("id")), map.get("name"), map.get("dni"), map.get("email"))));
		server.startAndConfigureMockServer(users);
	}

	@Then("a list of {int} users will be returned")
	public void a_list_of_users_will_be_returned(final Integer length) {
		response.then().log().all();
		final var elements = response.body().as(List.class);
		assertEquals(length, elements.size());
	}

	@Then("each user will have id and name and dni and email")
	public void each_user_will_have_id_and_name_and_dni_and_email() {
		response.then().body(Matchers.containsString("id"), containsString("name"), containsString("dni"),
				containsString("email"));
	}

}
