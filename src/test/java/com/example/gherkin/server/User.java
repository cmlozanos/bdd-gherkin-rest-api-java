package com.example.gherkin.server;

public class User {
	private final Integer id;
	private final String name;

	public User(final Integer id, final String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

}
