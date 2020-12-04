package com.example.gherkin.server;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.LOCATION;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;

import java.util.Map;
import java.util.stream.Collectors;

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

public class MockServer {

	private static final String STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND = "{\"status\":\"Error\",\"message\":\"Endpoint not found\"}";
	private static final String USER_NOT_FOUND_ERROR = "{ \"message\": \"user not found\" }";

	private final WireMockServer wireMockServer;
	private String host;

	public MockServer() {
		wireMockServer = new WireMockServer(options().dynamicPort());
	}

	public void startAndConfigureMockServer(final Map<Integer, User> users) {

		wireMockServer.start();
		host = "http://localhost:" + wireMockServer.port();

		configureFor("localhost", wireMockServer.port());

		setUpDefaultResponseTo404();
		setUpUsers(users);
	}

	private void setUpDefaultResponseTo404() {
		stubFor(any(anyUrl())
				.willReturn(aResponse().withStatus(NOT_FOUND_404).withBody(STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND)));
	}

	private void setUpUsers(final Map<Integer, User> users) {
		stubForAllUsers(users);
		stubForUserById(users);
		stubForAddNewUser();
	}

	private void stubForAddNewUser() {
		stubFor(post(urlEqualTo("/users")).willReturn(created().withHeader(LOCATION, host + "/users/10")));
	}

	private void stubForUserById(final Map<Integer, User> users) {
		stubFor(get(urlMatching("/users/.*"))
				.willReturn(notFound().withBody(USER_NOT_FOUND_ERROR).withHeader(CONTENT_TYPE, "application/json")));

		users.forEach((id, element) -> stubFor(get(urlEqualTo("/users/" + id)).willReturn(okForJson(users.get(id)))));
	}

	private void stubForAllUsers(final Map<Integer, User> users) {
		if (users.isEmpty()) {
			stubFor(get(urlEqualTo("/users")).willReturn(noContent()));
		} else {
			final var usersAsList = users.values().stream().collect(Collectors.toList());
			stubFor(get(urlEqualTo("/users")).willReturn(okForJson(usersAsList)));

			final var usersAsListLimited = users.values().stream().limit(5).collect(Collectors.toList());
			final var total = Double.valueOf(Math.ceil(users.values().size() / 5D)).intValue();
			final var paginatedUsers = new Pagination(1, total, usersAsListLimited);
			stubFor(get(urlEqualTo("/users?page=1")).willReturn(okForJson(paginatedUsers)));
		}
	}

	public String getHost() {
		return host;
	}
}
