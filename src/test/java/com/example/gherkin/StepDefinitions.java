package com.example.gherkin;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StepDefinitions {

	private Response response;
	private final MockServer server = new MockServer();
	private String path;
	private Integer id;

	@Given("server is started")
	public void server_is_started() {
		this.server.startAndConfigureMockServer();
	}

	@Given("path is {string}")
	public void path_is(final String path) {
		this.path = path;
	}

	@When("perform GET operation to retrieve users")
	public void perform_get_operation_to_retrieve_users() {
		this.response = RestAssured.get(this.server.getHost() + this.path).thenReturn();
	}

	@Then("all users will be returned")
	public void all_users_will_be_returned() {
		Assertions.assertNotNull(this.response);
		this.response.then().statusCode(HttpStatus.SC_OK);
		this.response.then().contentType(ContentType.JSON);
		this.response.then().body("[0].name", CoreMatchers.equalTo("user1"));
		this.response.then().body("[1].name", CoreMatchers.equalTo("user2"));
	}

	@Then("server will be stoped")
	public void server_will_be_stoped() {
		this.server.stop();
	}

	@Given("user id is {int}")
	public void user_id_is(final Integer id) {
		this.id = id;
	}

	@When("perform GET operation to retrieve user by id")
	public void perform_get_operation_to_retrieve_user_by_id() {
		this.response = RestAssured.get(this.server.getHost() + this.path + "/" + this.id).thenReturn();
	}

	@Then("user will be returned")
	public void user_will_be_returned() {
		Assertions.assertNotNull(this.response);
		this.response.then().statusCode(HttpStatus.SC_OK);
		this.response.then().contentType(ContentType.JSON);
		this.response.then().body("name", CoreMatchers.equalTo("user1"));
	}

}
