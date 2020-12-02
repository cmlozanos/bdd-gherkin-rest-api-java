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

public class StepDefinitions {

	private static final String USERS_PATH = "/users";

	private Response response;
	private final MockServer server = new MockServer();
	private Integer id;
	private HashMap<Integer, User> users;
	private User user;

	@Given("server is started")
	public void server_is_started() {
		this.server.startAndConfigureMockServer(this.users);
	}

	@Given("users is")
	public void users_is(final Map<Integer, String> users) {
		this.users = new HashMap<Integer, User>();
		users.forEach((id, name) -> this.users.put(id, new User(id, name)));
	}

	@When("retrieve all users")
	public void retrieve_all_users() {
		this.response = RestAssured.get(this.server.getHost() + StepDefinitions.USERS_PATH).thenReturn();
	}

	@Then("all users will be returned")
	public void all_users_will_be_returned() {
		Assertions.assertNotNull(this.response);
		this.response.then().log().all();
		this.response.then().statusCode(HttpStatus.SC_OK);
		this.response.then().contentType(ContentType.JSON);
		final List<User> values = this.users.values().stream().collect(Collectors.toList());
		for (int i = 0; i < this.users.size(); i++) {
			this.response.then().body("[" + i + "].id", CoreMatchers.equalTo(values.get(i).getId()));
			this.response.then().body("[" + i + "].name", CoreMatchers.equalTo(values.get(i).getName()));
		}
	}

	@Given("user id is {int}")
	public void user_id_is(final Integer id) {
		this.id = id;
	}

	@When("retrieve user by id")
	public void retrieve_user_by_id() {
		this.response = RestAssured.get(this.server.getHost() + StepDefinitions.USERS_PATH + "/" + this.id)
				.thenReturn();
	}

	@Then("user will be found")
	public void user_will_be_found() {
		Assertions.assertNotNull(this.response);
		this.response.then().contentType(ContentType.JSON);
		this.response.then().body("id", CoreMatchers.equalTo(this.id));
		this.response.then().body("name", CoreMatchers.equalTo("user" + this.id));
	}

	@Then("user will be not found")
	public void user_will_be_not_found() {
		Assertions.assertNotNull(this.response);
		this.response.then().contentType(ContentType.JSON);
		this.response.then().body("message", CoreMatchers.equalTo("user not found"));
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
		default:
			statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			break;
		}
		Assertions.assertNotNull(this.response);
		this.response.then().statusCode(statusCode);
	}

	@Given("User with id {int} name {string}")
	public void user_with_id_name(final Integer id, final String name) {
		this.user = new User(id, name);
	}

	@When("add new user")
	public void add_new_user() {
		this.response = RestAssured.given().body(this.user).post(this.server.getHost() + StepDefinitions.USERS_PATH)
				.thenReturn();
	}

	@Then("user id will be returned")
	public void user_id_will_be_returned() {
		Assertions.assertNotNull(this.response);
		this.response.then().log().all().header("location",
				CoreMatchers.equalTo(this.server.getHost() + StepDefinitions.USERS_PATH + "/" + 10));
	}
}
