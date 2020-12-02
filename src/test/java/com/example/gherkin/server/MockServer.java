package com.example.gherkin.server;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.LOCATION;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.created;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;

public class MockServer {

	private static final String STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND = "{\"status\":\"Error\",\"message\":\"Endpoint not found\"}";
	private static final String USER_NOT_FOUND_ERROR = "{ \"message\": \"user not found\" }";

	private final WireMockServer wireMockServer;
	private String host;
	private Map<Integer, User> users;

	public MockServer() {
		wireMockServer = new WireMockServer(options().dynamicPort());
	}

	public void startAndConfigureMockServer(final Map<Integer, User> users) {
		this.users = users;

		wireMockServer.start();
		host = "http://localhost:" + wireMockServer.port();

		configureFor("localhost", wireMockServer.port());
		setUpDefaultResponseTo404();

		setUpUsers();
	}

	private void setUpDefaultResponseTo404() {
		stubFor(any(anyUrl())
				.willReturn(aResponse().withStatus(NOT_FOUND_404).withBody(STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND)));
	}

	private void setUpUsers() {
		stubForAllUsers();

		stubFor(get(urlMatching("/users/.*"))
				.willReturn(notFound().withBody(USER_NOT_FOUND_ERROR).withHeader(CONTENT_TYPE, "application/json")));

		users.forEach((id, element) -> stubForFindById(id));

		stubFor(post(urlEqualTo("/users")).willReturn(created().withHeader(LOCATION, host + "/users/10")));
	}

	private void stubForAllUsers() {
		final List<User> users = new ArrayList<>();
		if (this.users.isEmpty()) {
			stubFor(get(urlEqualTo("/users")).willReturn(noContent()));
		} else {
			this.users.forEach((id, element) -> users.add(element));
			stubFor(get(urlEqualTo("/users")).willReturn(okForJson(users)));
		}
	}

	private void stubForFindById(final Integer id) {
		stubFor(get(urlEqualTo("/users/" + id)).willReturn(findUserById(id)));
	}

	private ResponseDefinitionBuilder findUserById(final Integer id) {
		return okForJson(users.get(id));
	}

	public String getHost() {
		return host;
	}
}
