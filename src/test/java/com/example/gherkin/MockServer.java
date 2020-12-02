package com.example.gherkin;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.common.net.HttpHeaders;

public class MockServer {

	private static final String USER_NOT_FOUND_ERROR = "{ \"message\": \"user not found\" }";
	private static final String USER_1 = "{\"name\":\"user1\"}";
	private static final String USER_2 = "{\"name\":\"user2\"}";
	private static final String ALL_USERS = String.format("[%s, %s]", MockServer.USER_1, MockServer.USER_2);

	private final WireMockServer wireMockServer;
	private String host;

	MockServer() {
		this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
	}

	public void startAndConfigureMockServer() {
		this.wireMockServer.start();
		this.host = "http://localhost:" + this.wireMockServer.port();

		WireMock.configureFor("localhost", this.wireMockServer.port());
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users")).willReturn(WireMock.okJson(MockServer.ALL_USERS)));
		WireMock.stubFor(WireMock.get(WireMock.urlMatching("/users/.*")).willReturn(WireMock.notFound()
				.withBody(MockServer.USER_NOT_FOUND_ERROR).withHeader(HttpHeaders.CONTENT_TYPE, "application/json")));
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users/1")).willReturn(WireMock.okJson(MockServer.USER_1)));
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users/2")).willReturn(WireMock.okJson(MockServer.USER_2)));
		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/users"))
				.withRequestBody(WireMock.equalToJson("{\"name\":\"user10\"}"))
				.willReturn(WireMock.created().withHeader(HttpHeaders.LOCATION, this.host + "/users/10")));
	}

	public String getHost() {
		return this.host;
	}
}
