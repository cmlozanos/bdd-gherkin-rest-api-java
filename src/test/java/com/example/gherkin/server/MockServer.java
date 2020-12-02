package com.example.gherkin.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.common.net.HttpHeaders;

public class MockServer {

	private static final String STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND = "{\"status\":\"Error\",\"message\":\"Endpoint not found\"}";
	private static final String USER_NOT_FOUND_ERROR = "{ \"message\": \"user not found\" }";

	private final WireMockServer wireMockServer;
	private String host;
	private Map<Integer, User> users;

	public MockServer() {
		this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
	}

	public void startAndConfigureMockServer(final Map<Integer, User> users) {
		this.users = users;

		this.wireMockServer.start();
		this.host = "http://localhost:" + this.wireMockServer.port();

		WireMock.configureFor("localhost", this.wireMockServer.port());
		this.setUpDefaultResponseTo404();

		this.setUpUsers();
	}

	private void setUpDefaultResponseTo404() {
		WireMock.stubFor(WireMock.any(WireMock.anyUrl()).willReturn(WireMock.aResponse()
				.withStatus(HttpStatus.NOT_FOUND_404).withBody(MockServer.STATUS_ERROR_MESSAGE_ENDPOINT_NOT_FOUND)));
	}

	private void setUpUsers() {
		this.stubForAllUsers();

		WireMock.stubFor(WireMock.get(WireMock.urlMatching("/users/.*")).willReturn(WireMock.notFound()
				.withBody(MockServer.USER_NOT_FOUND_ERROR).withHeader(HttpHeaders.CONTENT_TYPE, "application/json")));

		this.users.forEach((id, element) -> this.stubForFindById(id));

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/users"))
				.willReturn(WireMock.created().withHeader(HttpHeaders.LOCATION, this.host + "/users/10")));
	}

	private void stubForAllUsers() {
		final List<User> users = new ArrayList<>();
		this.users.forEach((id, element) -> users.add(element));
		WireMock.stubFor(
				WireMock.get(WireMock.urlEqualTo("/users")).willReturn(ResponseDefinitionBuilder.okForJson(users)));
	}

	private void stubForFindById(final Integer id) {
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users/" + id)).willReturn(this.findUserById(id)));
	}

	private ResponseDefinitionBuilder findUserById(final Integer id) {
		return ResponseDefinitionBuilder.okForJson(this.users.get(id));
	}

	public String getHost() {
		return this.host;
	}
}
