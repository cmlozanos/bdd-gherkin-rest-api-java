package com.example.gherkin;

import static java.lang.Integer.valueOf;
import static org.eclipse.jetty.http.HttpStatus.INTERNAL_SERVER_ERROR_500;
import static org.eclipse.jetty.http.HttpStatus.NO_CONTENT_204;
import static org.eclipse.jetty.http.HttpStatus.OK_200;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gherkin.server.MockServer;
import com.example.gherkin.server.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.get;
import static io.restassured.http.ContentType.JSON;

import io.restassured.response.Response;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AsUserIWantRecoverTheFullListOfUsers {

	private static final String USERS_PATH = "/users";

	private Response response;
	private final MockServer server = new MockServer();
	private HashMap<Integer, User> users;

	@Given("web service with non users")
	public void web_service_with_non_users() {
		users = new HashMap<Integer, User>();
		server.startAndConfigureMockServer(users);
	}

	@When("retrieve all users")
	public void retrieve_all_users() {
		response = get(server.getHost() + USERS_PATH).thenReturn();
	}

	@Then("status will be {string}")
	public void status_will_be(final String status) {
		final int statusCode;
		switch (status) {
		case "success":
			statusCode = OK_200;
			break;
		case "no content":
			statusCode = NO_CONTENT_204;
			break;
		default:
			statusCode = INTERNAL_SERVER_ERROR_500;
			break;
		}

		response.then().statusCode(statusCode);
	}

	@Given("web service with users")
	public void web_service_with_users(final List<Map<String, String>> dataTable) {
		users = new HashMap<Integer, User>();
		dataTable.forEach(map -> users.put(valueOf(map.get("id")),
				new User(valueOf(map.get("id")), map.get("name"), map.get("dni"), map.get("email"))));
		server.startAndConfigureMockServer(users);
	}

	@Then("content type will be JSON")
	public void content_type_will_be_json() {
		response.then().contentType(JSON);
	}

	@Then("a list of {int} users will be returned")
	public void a_list_of_users_will_be_returned(final Integer length) {
		response.then().log().all();
		final var elements = response.body().as(List.class);
		assertEquals(length, elements.size());
	}

	@Then("each user will have id and name and dni and email")
	public void each_user_will_have_id_and_name_and_dni_and_email() {
		response.then().body(containsString("id"), containsString("name"), containsString("dni"),
				containsString("email"));
	}

	@Then("a list of {int} users paginated will be returned")
	public void a_list_of_users_paginated_will_be_returned(final Integer length) {
		response.then().log().all();
		final var list = response.then().extract().jsonPath().getList("data");
		assertEquals(length, list.size());
	}

	@Then("pagination will have {int} pages")
	public void pagination_will_have_pages(final Integer total) {
		response.then().body("total", equalTo(total));
	}

	@Then("page will be {int}")
	public void page_will_be(final Integer page) {
		response.then().body("page", equalTo(page));
	}

}
