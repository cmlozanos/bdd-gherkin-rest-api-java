package com.example.gherkin;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class MockServer {

	private final WireMockServer wireMockServer;
	private String host;

	MockServer() {
		this.wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
		this.configureMockServer();
	}

	private void configureMockServer() {
		this.wireMockServer.start();
		WireMock.configureFor("localhost", this.wireMockServer.port());
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/users")).willReturn(WireMock.aResponse()));
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
