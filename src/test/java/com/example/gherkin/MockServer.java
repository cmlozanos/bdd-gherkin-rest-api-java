package com.example.gherkin;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class MockServer {

	private static final String USER_1 = "{\"name\":\"user1\"}";
	private static final String USER_2 = "{\"name\":\"user2\"}";
	private static final String ALL_USERS = "[" + MockServer.USER_1 + ", " + MockServer.USER_2 + "]";

	private final WireMockServer wireMockServer;
	private String host;

	MockServer() {
		this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
	}

	public void startAndConfigureMockServer() {
		this.wireMockServer.start();
		WireMock.configureFor("localhost", this.wireMockServer.port());
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users")).willReturn(WireMock.okJson(MockServer.ALL_USERS)));
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users/1")).willReturn(WireMock.okJson(MockServer.USER_1)));
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users/2")).willReturn(WireMock.okJson(MockServer.USER_2)));
		this.host = "http://localhost:" + this.wireMockServer.port();
	}

	public String getHost() {
		return this.host;
	}

	public void stop() {
		if (this.wireMockServer.isRunning()) {
			this.wireMockServer.stop();
		}
	}

}
